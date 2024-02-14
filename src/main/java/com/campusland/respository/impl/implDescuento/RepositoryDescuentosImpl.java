package com.campusland.respository.impl.implDescuento;

import java.util.List;

import com.campusland.respository.RepositoryDescuentos;
import com.campusland.respository.models.Descuento;

import java.util.ArrayList;

public class RepositoryDescuentosImpl implements RepositoryDescuentos {

    private List<Descuento> descuentos;

    public RepositoryDescuentosImpl() {
        descuentos = new ArrayList<>();
    }

    @Override
    public void agregarDescuento(Descuento descuento) {
        descuentos.add(descuento);
    }

    @Override
    public Descuento obtenerDescuentoPorId(int id) {
        for (Descuento descuento : descuentos) {
            if (descuento.getId() == id) {
                return descuento;
            }
        }
        return null; 
    }

    @Override
    public List<Descuento> obtenerTodosLosDescuentos() {
        return descuentos;
    }

}

