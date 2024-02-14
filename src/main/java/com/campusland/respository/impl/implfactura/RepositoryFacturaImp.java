package com.campusland.respository.impl.implfactura;

import java.util.List;

import com.campusland.respository.RepositoryFactura;
import com.campusland.respository.models.Factura;
import com.campusland.utils.conexionpersistencia.conexiondblist.ConexionBDList;
import java.time.LocalDateTime;

public class RepositoryFacturaImp implements RepositoryFactura {

    ConexionBDList conexion = ConexionBDList.getConexion();

    @Override
    public List<Factura> listar() {
        return conexion.getListFacturas();
        
    }

    @Override
    public void crear(Factura factura) {
       conexion.getListFacturas().add(factura);
        
    }

    @Override
    public List<Factura> obtenerFacturasEnRango(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }


}
