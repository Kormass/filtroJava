package com.campusland.services;

import java.util.List;

import com.campusland.respository.models.Descuento;

public interface ServiceDescuento {
     void agregarDescuento(Descuento descuento);

    Descuento obtenerDescuentoPorId(int id);

    List<Descuento> obtenerTodosLosDescuentos();

    List<Descuento> obtenerDescuentosActivos();
}
