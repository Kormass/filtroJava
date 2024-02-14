package com.campusland.services.impl;

import java.util.List;

import com.campusland.respository.RepositoryDescuentos;
import com.campusland.respository.models.Descuento;
import com.campusland.services.ServiceDescuento;
import com.campusland.utils.conexionpersistencia.conexionbdmysql.ConexionBDMysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ServiceDescuentoImpl implements ServiceDescuento {

    @SuppressWarnings("unused")
    private RepositoryDescuentos repositoryDescuentos;
    private Connection connection;

    public ServiceDescuentoImpl(RepositoryDescuentos repositoryDescuentos) {
        this.repositoryDescuentos = repositoryDescuentos;
        this.connection = ConexionBDMysql.getInstance();
    }

    @Override
    public void agregarDescuento(Descuento descuento) {
        String sql = "INSERT INTO Descuentos (tipo_descuento, CondicionesDeAplicacion, Producto, Estado) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, descuento.getTipo_descuento());
            statement.setString(2, descuento.getCondicionesDeAplicacion());
            statement.setInt(3, descuento.getId_Producto());
            statement.setString(4, descuento.getEstado());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
public Descuento obtenerDescuentoPorId(int id) {
    Descuento descuento = null;
    String sql = "SELECT * FROM Descuentos WHERE id = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
        statement.setInt(1, id);

        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                descuento = new Descuento();
                descuento.setId(resultSet.getInt("id"));
                descuento.setTipo_descuento(resultSet.getString("tipo_descuento"));
                descuento.setCondicionesDeAplicacion(resultSet.getString("CondicionesDeAplicacion"));
                descuento.setId_Producto(resultSet.getInt("Producto"));
                descuento.setEstado(resultSet.getString("Estado"));
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    
}
    return descuento;
}


    @Override
    public List<Descuento> obtenerTodosLosDescuentos() {
        List<Descuento> descuentos = new ArrayList<>();
        String sql = "SELECT * FROM Descuentos";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Descuento descuento = new Descuento();
                descuento.setId(resultSet.getInt("id"));
                descuento.setTipo_descuento(resultSet.getString("tipo_descuento"));
                descuento.setCondicionesDeAplicacion(resultSet.getString("CondicionesDeAplicacion"));
                descuento.setId_Producto(resultSet.getInt("id_Productos"));
                descuento.setEstado(resultSet.getString("Estado"));
                descuentos.add(descuento);
            }
        } catch (SQLException e) {
            e.printStackTrace();

    }
        return descuentos;
}

    @Override
    public List<Descuento> obtenerDescuentosActivos() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'obtenerDescuentosActivos'");
    }
}



