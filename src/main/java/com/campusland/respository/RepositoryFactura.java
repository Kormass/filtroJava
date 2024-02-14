package com.campusland.respository;

import java.util.List;

import com.campusland.exceptiones.facturaexceptions.FacturaExceptionInsertDataBase;
import com.campusland.respository.models.Factura;
import java.time.LocalDateTime;

public interface RepositoryFactura {

    List<Factura> listar();

    void crear(Factura factura)throws FacturaExceptionInsertDataBase;

    List<Factura> obtenerFacturasEnRango(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
}
