package com.campusland.respository.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.campusland.utils.Formato;
import com.fasterxml.jackson.annotation.JsonFormat;


public class Factura {

    private int numeroFactura;
    int id_descuento;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")    
    private LocalDateTime fecha;
    private Cliente cliente;
    private List<ItemFactura> items;
    private static int nextNumeroFactura;

    public Factura(){

    }



    public Factura(int id_descuento, int numeroFactura, LocalDateTime fecha, Cliente cliente) {
        this.numeroFactura = numeroFactura;
        this.fecha = fecha;
        this.cliente = cliente;
        this.items = new ArrayList<>();
        this.id_descuento = id_descuento;
    }

    public Factura(LocalDateTime fecha, Cliente cliente) {
        this.numeroFactura = ++nextNumeroFactura;
        this.fecha = fecha;
        this.cliente = cliente;
        this.items = new ArrayList<>();
    }
    

    public int getNumeroFactura() {
        return numeroFactura;
    }



    public void setNumeroFactura(int numeroFactura) {
        this.numeroFactura = numeroFactura;
    }



    public int getId_descuento() {
        return id_descuento;
    }



    public void setId_descuento(int id_descuento) {
        this.id_descuento = id_descuento;
    }



    public LocalDateTime getFecha() {
        return fecha;
    }



    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }



    public Cliente getCliente() {
        return cliente;
    }



    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }



    public List<ItemFactura> getItems() {
        return items;
    }



    public void setItems(List<ItemFactura> items) {
        this.items = items;
    }



    public static int getNextNumeroFactura() {
        return nextNumeroFactura;
    }



    public double getTotalFactura() {
        double totalFactura = 0;
        for (ItemFactura item : items) {
            totalFactura += item.getImporte();
        }
        return totalFactura;
    }

    public void agregarItem(ItemFactura item){
        this.items.add(item);
    }

    public void display() {
        System.out.println();
        System.out.println("-------------------------------------------------");

        System.out.println("Factura: " + this.numeroFactura);
        System.out.println("Cliente: " + this.cliente.getFullName());
        System.out.println("Fecha: " + Formato.formatoFechaHora(this.getFecha()));
        System.out.println("-----------Detalle Factura----------------------");
    
        double totalSinDescuento = 0.0;
    
        for (ItemFactura item : this.items) {
            System.out.println(item.getProducto().getCodigoNombre() + " " + item.getCantidad() + " "
                    + Formato.formatoMonedaPesos(item.getImporte()));
    
            // Sumar al total sin descuento
            totalSinDescuento += item.getImporte();
        }
        System.out.println("-------------------------------------------------");

        // Mostrar el total sin descuento
        System.out.println();
        System.out.println("Total sin iva          " + Formato.formatoMonedaPesos(totalSinDescuento));
        System.out.println("-------------------------------------------------");

        // Aplicar IVA
        double tasaIVA = 0.19; // Supongamos que la tasa de IVA es del 16%
        double montoIVA = totalSinDescuento * tasaIVA;
        double totalConIVA = totalSinDescuento + montoIVA;
        System.out.println("-------------------------------------------------");

        // Mostrar el total con descuento y IVA
        System.out.println("Monto IVA              " + Formato.formatoMonedaPesos(montoIVA));
        System.out.println("Total con iva          " + Formato.formatoMonedaPesos(totalConIVA));
        System.out.println("-------------------------------------------------");

    }
    
    
    public void setNextNumeroFactura(int nextNumeroFactura) {
        Factura.nextNumeroFactura = nextNumeroFactura;
    }

}
