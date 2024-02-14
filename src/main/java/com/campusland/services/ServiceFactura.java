package com.campusland.services;

import java.util.List;

import com.campusland.exceptiones.facturaexceptions.FacturaExceptionInsertDataBase;
import com.campusland.respository.models.Factura;
import java.time.LocalDateTime;

public interface ServiceFactura {

    List<Factura> listar();

    void crear(Factura factura)throws FacturaExceptionInsertDataBase;
    List<Factura> obtenerFacturasEnRango(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    Factura obtenerUltimaFactura();
    
}
