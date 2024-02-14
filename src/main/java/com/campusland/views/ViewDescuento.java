package com.campusland.views;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.campusland.exceptiones.productoexceptions.ProductoNullException;
import com.campusland.respository.models.Descuento;
import com.campusland.respository.models.Factura;
import com.campusland.utils.conexionpersistencia.conexionbdmysql.ConexionBDMysql;

public class ViewDescuento extends ViewMain{
    public static void startMenu() throws ProductoNullException {
        int op = 0;

        do {

            op = mostrarMenu();
            switch (op) {
                case 1:
                listarDescuentos();
                    break;
                case 2:
                aplicarDescuento();
                    break;
                default:
                    System.out.println("Opcion no valida");
                    break;
            }

        } while (op >= 1 && op < 3);

    }

    public static int mostrarMenu() {
        System.out.println("----Menu--Descuentos----");
        System.out.println("1. Listar Descuentos.");
        System.out.println("2. Aplicar Descuentos");
        System.out.println("3. Salir Descuentos");
        return leer.nextInt();
    }

    private static void listarDescuentos() {
        List<Descuento> listaDescuentos = serviceDescuento.obtenerTodosLosDescuentos();

        if (!listaDescuentos.isEmpty()) {
            System.out.println("Listado de descuentos:");

            for (Descuento descuento : listaDescuentos) {
                System.out.println("-----------------------------------------");
                System.out.println("/  ID: " + descuento.getId());
                System.out.println("/  Tipo de descuento: " + descuento.getTipo_descuento());
                System.out.println("/  Condiciones de aplicación: " + descuento.getCondicionesDeAplicacion());
                System.out.println("/  ID del producto: " + descuento.getId_Producto());
                System.out.println("/  Estado: " + descuento.getEstado());
                System.out.println("-----------------------------------------");
            }
        } else {
            System.out.println("No hay descuentos para mostrar.");
        }
    }

    private static void aplicarDescuento() throws ProductoNullException {
        List<Descuento> descuentosActivos = obtenerDescuentosActivos();
        Factura factura = serviceFactura.obtenerUltimaFactura();
    
        if (factura != null) {
            for (Descuento descuento : descuentosActivos) {
                if (cumpleCondiciones(descuento, factura)) {
                    aplicarDescuento(descuento, factura);
                }
            }
    
            System.out.println("Factura con descuentos aplicados:");
            factura.display();
        } else {
            System.out.println("No se encontró ninguna factura para aplicar descuentos.");
        }
    }
    public static List<Descuento> obtenerDescuentosActivos() {
        List<Descuento> descuentosActivos = new ArrayList<>();

        try {
            Connection connection = ConexionBDMysql.getInstance(); // Utiliza tu clase de conexión

            String sql = "SELECT * FROM Descuentos WHERE Estado = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, "activo");

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Descuento descuento = new Descuento();
                        descuento.setId(resultSet.getInt("id"));
                        descuento.setTipo_descuento(resultSet.getString("tipo_descuento"));
                        descuento.setDescuento_cliente(resultSet.getBoolean("descuento_cliente"));
                        descuento.setCondicionesDeAplicacion(resultSet.getString("CondicionesDeAplicacion"));
                        descuento.setMonto(resultSet.getDouble("Monto"));
                        descuento.setId_Producto(resultSet.getInt("id_Productos"));
                        descuento.setEstado(resultSet.getString("Estado"));

                        descuentosActivos.add(descuento);
                    }
                }
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return descuentosActivos;
    }





    private static boolean cumpleCondiciones(Descuento descuento, Factura factura) {
        String condiciones = descuento.getCondicionesDeAplicacion();
    
        switch (descuento.getTipo_descuento()) {
            case "Porcentaje":
                return cumpleCondicionesPorcentaje(condiciones, factura);
            // Agrega lógica para otros tipos de descuento según sea necesario
            default:
                // Si el tipo de descuento no está reconocido, se asume que no cumple las condiciones
                return false;
        }
    }



    private static void aplicarDescuento(Descuento descuento, Factura factura) {
        // Lógica para aplicar el descuento a la factura
        double montoDescuento = 0.0;
        switch (descuento.getTipo_descuento()) {
            case "Porcentaje":
                if (cumpleCondicionesPorcentaje(descuento.getCondicionesDeAplicacion(), factura)) {
                    montoDescuento = factura.getTotalFactura() * (descuento.getMonto() / 100.0);
                }
                break;
            // Agregar lógica para otros tipos de descuento según sea necesario
            default:
                break;
        }
    }
    


        private static boolean cumpleCondicionesPorcentaje(String condicionesDeAplicacion, Factura factura) {
        if (condicionesDeAplicacion.contains("Compra durante las temporadas navideñas")) {
            LocalDateTime fechaFactura = factura.getFecha();

            LocalDate inicioNavidad = LocalDate.parse("2024-12-01");
            LocalDate finNavidad = LocalDate.parse("2024-12-31");

            LocalDate fechaFacturaLocalDate = fechaFactura.toLocalDate();
            return fechaFacturaLocalDate.isAfter(inicioNavidad) && fechaFacturaLocalDate.isBefore(finNavidad);
        }

        return true;
    }



    
    }





