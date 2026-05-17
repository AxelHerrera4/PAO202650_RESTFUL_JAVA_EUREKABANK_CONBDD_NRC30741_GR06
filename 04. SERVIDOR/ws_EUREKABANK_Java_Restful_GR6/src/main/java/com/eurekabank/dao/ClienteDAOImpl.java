package com.eurekabank.dao;

import com.eurekabank.modelo.Cliente;
import com.eurekabank.util.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClienteDAOImpl implements IClienteDAO {

    @Override
    public Cliente validarLogin(String dni) {
        Cliente cliente = null;
        String sql = "SELECT * FROM cliente WHERE dni = ?";
        
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, dni);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    cliente = new Cliente();
                    cliente.setIdCliente(rs.getInt("id_cliente"));
                    cliente.setDni(rs.getString("dni"));
                    cliente.setNombre(rs.getString("nombre"));
                    cliente.setApellido(rs.getString("apellido"));
                    cliente.setDireccion(rs.getString("direccion"));
                    cliente.setTelefono(rs.getString("telefono"));
                    cliente.setEmail(rs.getString("email"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en validarLoginCliente: " + e.getMessage());
        }
        return cliente;
    }
}
