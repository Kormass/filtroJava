package com.campusland.utils.conexionpersistencia.conexionbdmysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConexionBDMysql {
    private static Connection dbConnection; // Cambiado el nombre de la variable
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/Factura?serverTimezone=UTC";
    private static final String DATABASE_USERNAME = "root";
    private static final String DATABASE_PASSWORD = "campus2024";

    private ConexionBDMysql() {}

    public static synchronized Connection getInstance() {
        try {
            if (dbConnection == null || dbConnection.isClosed()) {
                dbConnection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
            }
            return dbConnection;
        } catch (SQLException ex) {
            // Manejar la excepción o lanzar una excepción personalizada
            throw new RuntimeException("Error al obtener la conexión a la base de datos", ex);
        }
    }
    public static void closeConnection() {
        try {
            if (dbConnection != null && !dbConnection.isClosed()) {
                dbConnection.close();
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al cerrar la conexión a la base de datos", ex);
        }
    }
}
