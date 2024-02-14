package com.campusland.services.impl;

import java.util.List;

import com.campusland.exceptiones.facturaexceptions.FacturaExceptionInsertDataBase;
import com.campusland.respository.RepositoryFactura;
import com.campusland.respository.models.Cliente;
import com.campusland.respository.models.Factura;
import com.campusland.services.ServiceFactura;
import com.campusland.utils.conexionpersistencia.conexionbdmysql.ConexionBDMysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class ServiceFacturaImpl implements ServiceFactura {

  private final RepositoryFactura repositoryFacturaMysql;
  private final RepositoryFactura repositoryFacturaJson;

  public ServiceFacturaImpl(RepositoryFactura repositoryFacturaMysql, RepositoryFactura repositoryFacturaJson) {
    this.repositoryFacturaMysql = repositoryFacturaMysql;
    this.repositoryFacturaJson = repositoryFacturaJson;
  }

  @Override
  public List<Factura> listar() {
    return this.repositoryFacturaMysql.listar();

  }

  @Override
  public void crear(Factura factura) {
      try {
          guardarEnMysqlYJson(factura);
      } catch (FacturaExceptionInsertDataBase e) {
         e.printStackTrace();
      }
  }
  
  private void guardarEnMysqlYJson(Factura factura) throws FacturaExceptionInsertDataBase {
      guardarEnMysql(factura);
      guardarEnJson(factura);
  }
  
  private void guardarEnMysql(Factura factura) throws FacturaExceptionInsertDataBase {
      this.repositoryFacturaMysql.crear(factura);
  }
  
  private void guardarEnJson(Factura factura) throws FacturaExceptionInsertDataBase {
      try {
          this.repositoryFacturaJson.crear(factura);
      } catch (FacturaExceptionInsertDataBase eJson) {
          eJson.printStackTrace();
      }
  }

    @Override
    public List<Factura> obtenerFacturasEnRango(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return this.repositoryFacturaMysql.obtenerFacturasEnRango(fechaInicio, fechaFin);
    }

    @Override
    public Factura obtenerUltimaFactura() {
        try (Connection connection = ConexionBDMysql.getInstance()) {
            String query = "SELECT * FROM factura ORDER BY numeroFactura DESC LIMIT 1";
            try (PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return construirFacturaDesdeResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Factura construirFacturaDesdeResultSet(ResultSet resultSet) throws SQLException {
        int id_descuento = resultSet.getInt("id_descuento");
        int numeroFactura = resultSet.getInt("numeroFactura");
        LocalDateTime fecha = resultSet.getTimestamp("fecha").toLocalDateTime();
        Cliente cliente = obtenerClienteDesdeResultSet(resultSet); 
        return new Factura(id_descuento, numeroFactura, fecha, cliente);
    }

    public Cliente obtenerClienteDesdeResultSet(ResultSet resultSet) throws SQLException {
        int idCliente = resultSet.getInt("id_cliente");
        String nombre = resultSet.getString("nombre_cliente");
        String apellido = resultSet.getString("apellido_cliente");
        String email = resultSet.getString("email_cliente");
        String direccion = resultSet.getString("direccion_cliente");
        String celular = resultSet.getString("celular_cliente");
        String documento = resultSet.getString("documento_cliente");

        return new Cliente(nombre, apellido, email, direccion, celular, documento);
    }
  

}
