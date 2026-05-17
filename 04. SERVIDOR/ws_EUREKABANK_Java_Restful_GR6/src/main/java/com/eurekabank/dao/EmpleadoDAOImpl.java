package com.eurekabank.dao;

import com.eurekabank.modelo.Empleado;
import com.eurekabank.util.ConexionDB;
import com.eurekabank.util.SeguridadUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Implementación de IEmpleadoDAO usando JDBC y seguridad BCrypt.
 * @author Antigravity
 */
public class EmpleadoDAOImpl implements IEmpleadoDAO {

    @Override
    public Empleado validarLogin(String usuario, String password) {
        Empleado empleado = null;
        // Buscamos únicamente por el nombre de usuario
        String sql = "SELECT * FROM empleado WHERE usuario = ?";
        
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, usuario);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String hashedPassword = rs.getString("password");
                    
                    // RQF-Seguridad: Validación segura mediante BCrypt
                    if (SeguridadUtil.checkPassword(password, hashedPassword)) {
                        empleado = new Empleado();
                        empleado.setIdEmpleado(rs.getInt("id_empleado"));
                        empleado.setUsuario(rs.getString("usuario"));
                        empleado.setPassword(hashedPassword);
                        empleado.setNombreCompleto(rs.getString("nombre_completo"));
                        empleado.setRol(rs.getString("rol"));
                    } else {
                        System.out.println("[Seguridad] Intento de login fallido: Contraseña incorrecta para el usuario: " + usuario);
                    }
                } else {
                    System.out.println("[Seguridad] Intento de login fallido: Usuario inexistente: " + usuario);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en validarLogin: " + e.getMessage());
        }
        return empleado;
    }
}
