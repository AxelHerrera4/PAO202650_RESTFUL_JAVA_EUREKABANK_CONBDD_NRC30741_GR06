package com.eurekabank.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestor de conexión a la base de datos MySQL (Hilos seguros/Thread-safe).
 * Provee conexiones individuales para evitar interferencias en entornos concurrentes.
 * @author Antigravity
 */
public class ConexionDB {

    // Parámetros de configuración
    private static final String URL = "jdbc:mysql://localhost:3306/eureka_bank_restful?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "root"; // Ajustar según configuración local
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    private ConexionDB() {
        // Constructor privado para evitar instanciación
    }

    /**
     * Obtiene una nueva conexión a la base de datos de manera independiente.
     * @return Connection objeto de conexión.
     */
    public static Connection getConexion() {
        try {
            Class.forName(DRIVER);
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
            return null;
        }
    }
}
