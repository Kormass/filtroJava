package com.campusland.respository.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Descuento {

    int id;
    private String tipo_descuento;
    private String CondicionesDeAplicacion;
    private boolean descuento_cliente;
    private int id_Producto;
    private String Estado;
    private double Monto;
}
