package com.campusland.views;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import com.campusland.exceptiones.facturaexceptions.FacturaExceptionInsertDataBase;
import com.campusland.exceptiones.productoexceptions.ProductoNullException;
import com.campusland.respository.models.Cliente;
import com.campusland.respository.models.Factura;
import com.campusland.respository.models.ItemFactura;
import com.campusland.respository.models.Producto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ViewFactura extends ViewMain {

    public static void startMenu() throws ProductoNullException {

        int op = 0;

        do {
            List<Factura> facturas = new ArrayList<>();

            op = mostrarMenu();
            switch (op) {
                case 1:
                    crearFactura();
                    break;
                case 2:
                    listarFactura();
                    break;
                case 3:
                    generarArchivo(serviceFactura.listar());
                default:
                    System.out.println("Opcion no valida");
                    break;
            }

        } while (op >= 1 && op < 3);

    }


    public static int mostrarMenu() {
        System.out.println("----Menu--Factura----");
        System.out.println("1. Crear factura.");
        System.out.println("2. Listar factura.");
        System.out.println("3. Generar Archivo DIAN año 2024");
        System.out.println("4. Salir ");    
        return leer.nextInt();
    }

    private static void generarArchivo(List<Factura> facturas) {
        // Filtrar las facturas del año 2024
        int year = 2024;
        List<Factura> facturasDelAnio = facturas.stream()
                .filter(factura -> factura.getFecha().getYear() == year)
                .collect(Collectors.toList());
    
        if (!facturasDelAnio.isEmpty()) {
            // Crear el objeto JSON
            JsonObject jsonFacturas = new JsonObject();
            jsonFacturas.add("facturas", toJsonArray(facturasDelAnio));
    
            // Escribir el JSON en el archivo facturafecha.json
            try (FileWriter fileWriter = new FileWriter("facturafecha.json")) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                gson.toJson(jsonFacturas, fileWriter);
                System.out.println("Se ha generado el archivo facturafecha.json");
            } catch (IOException e) {
                System.out.println("Error al escribir en el archivo facturafecha.json: " + e.getMessage());
            }
        } else {
            System.out.println("No hay facturas para el año " + year);
        }
    }

private static JsonArray toJsonArray(List<Factura> facturas) {
    JsonArray jsonArray = new JsonArray();
    for (Factura factura : facturas) {
        JsonObject jsonFactura = new JsonObject();
        jsonFactura.addProperty("numeroFactura", factura.getNumeroFactura());
        jsonFactura.addProperty("id_descuento", factura.getId_descuento());
        jsonFactura.addProperty("fecha", factura.getFecha().toString());
        jsonFactura.add("cliente", toJsonCliente(factura.getCliente()));
        jsonFactura.add("items", toJsonArrayItems(factura.getItems()));
        jsonFactura.addProperty("totalFactura", factura.getTotalFactura());
        jsonArray.add(jsonFactura);
    }
    return jsonArray;
}

private static JsonObject toJsonCliente(Cliente cliente) {
    JsonObject jsonCliente = new JsonObject();
    jsonCliente.addProperty("id", cliente.getId());
    jsonCliente.addProperty("nombre", cliente.getNombre());
    jsonCliente.addProperty("apellido", cliente.getApellido());
    jsonCliente.addProperty("email", cliente.getEmail());
    jsonCliente.addProperty("direccion", cliente.getDireccion());
    jsonCliente.addProperty("celular", cliente.getCelular());
    jsonCliente.addProperty("documento", cliente.getDocumento());
    jsonCliente.addProperty("fullName", cliente.getFullName());
    return jsonCliente;
}

private static JsonArray toJsonArrayItems(List<ItemFactura> items) {
    JsonArray jsonArray = new JsonArray();
    for (ItemFactura item : items) {
        JsonObject jsonItem = new JsonObject();
        jsonItem.addProperty("cantidad", item.getCantidad());
        jsonItem.add("producto", toJsonProducto(item.getProducto()));
        jsonItem.addProperty("importe", item.getImporte());
        jsonArray.add(jsonItem);
    }
    return jsonArray;
}

private static JsonObject toJsonProducto(Producto producto) {
    JsonObject jsonProducto = new JsonObject();
    jsonProducto.addProperty("codigo", producto.getCodigo());
    jsonProducto.addProperty("nombre", producto.getNombre());
    jsonProducto.addProperty("descripcion", producto.getDescripcion());
    jsonProducto.addProperty("precioVenta", producto.getPrecioVenta());
    jsonProducto.addProperty("precioCompra", producto.getPrecioCompra());
    jsonProducto.addProperty("codigoNombre", producto.getCodigoNombre());
    jsonProducto.addProperty("utilidad", producto.getUtilidad());
    return jsonProducto;
}

    private static List<Factura> filtrarFacturasPorAno(List<Factura> facturas, int ano) {
        // Crear una lista para almacenar las facturas del año especificado
        List<Factura> facturasDelAno = new ArrayList<>();

        // Filtrar las facturas
        for (Factura factura : facturas) {
            int anoFactura = factura.getFecha().getYear();

            if (anoFactura == ano) {
                facturasDelAno.add(factura);
            }
        }

        return facturasDelAno;
    }

    public static void listarFactura() {
        System.out.println("Lista de Facturas");
        for (Factura factura : serviceFactura.listar()) {
            factura.display();
            System.out.println();
        }
    }

    public static void crearFactura() throws ProductoNullException {
        System.out.println("-- Creación de Factura ---");
    
        Cliente cliente;
        do {
            cliente = ViewCliente.buscarGetCliente();
        } while (cliente == null);
    
        Factura factura = new Factura(LocalDateTime.now(), cliente);
        System.out.println("-- Se creó la factura -----------------");
    
        List<Producto> listaProductos = serviceProducto.listar();
    
        if (listaProductos.isEmpty()) {
            System.out.println("-------------------------------------------------");

            System.out.println("No hay productos disponibles para seleccionar.");
            System.out.println("-------------------------------------------------");

        } else {
            System.out.println("-------------------------------------------------");

            System.out.println("Lista de productos disponibles:");
            System.out.println("-------------------------------------------------");

            for (Producto producto : listaProductos) {
                System.out.println(producto.getCodigo() + ". " + producto.getNombre() + " - Precio: " + producto.getPrecioVenta());
            }
    
            do {
                System.out.println("-------------------------------------------------");

                System.out.print("Seleccione el código del producto a agregar (0 para salir): ");
                String inputCodigoProducto = leer.next();
        
                if (inputCodigoProducto.equals("0")) {
                    break;
                }
        
                try {
                    int codigoProducto = Integer.parseInt(inputCodigoProducto);
                    Producto producto = serviceProducto.porCodigo(codigoProducto);
        
                    if (producto != null) {
                        System.out.print("Cantidad: ");
                        int cantidad = leer.nextInt();
                        ItemFactura item = new ItemFactura(cantidad, producto);
                        factura.agregarItem(item);
        
                        // Mostrar opciones para agregar otro producto
                        System.out.println("-------------------------------------------------");

                        System.out.println("Agregar otro producto:");
                        System.out.println("1. Sí");
                        System.out.println("2. No");
                        System.out.println("Elija una opción (1 o 2): ");
                        System.out.println("-------------------------------------------------");

                        int opcion = leer.nextInt();
        
                        if (opcion == 2) {
                            break; 
                        }
                    } else {
                        throw new ProductoNullException("Código de producto no válido. Intente nuevamente.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Por favor, ingrese un número entero válido.");
                    leer.next(); 
                }
        
            } while (true);
        
            try {
                serviceFactura.crear(factura);
    
                System.out.println("Se creó la factura");
                factura.display();
    
    
            } catch (FacturaExceptionInsertDataBase e) {
                System.out.println(e.getMessage());
            }
        }
    }
    

}
