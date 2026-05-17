package com.eurekabank.dao;

import com.eurekabank.modelo.Cuenta;
import com.eurekabank.modelo.CuentaClienteDTO;
import com.eurekabank.util.ConexionDB;
import com.eurekabank.util.GestorConcurrencia;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de ICuentaDAO usando JDBC.
 */
public class CuentaDAOImpl implements ICuentaDAO {

    @Override
    public Cuenta obtenerCuenta(String numeroCuenta) {
        try (Connection con = ConexionDB.getConexion()) {
            return obtenerCuentaForUpdate(con, numeroCuenta); // Por defecto sin transacción activa
        } catch (SQLException e) {
            System.err.println("Error en obtenerCuenta: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Cuenta obtenerCuentaForUpdate(Connection con, String numeroCuenta) throws SQLException {
        Cuenta cuenta = null;
        // RQF-004: Implementación de SELECT FOR UPDATE
        String sql = "SELECT * FROM cuenta WHERE numero_cuenta = ? FOR UPDATE";
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, numeroCuenta);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    cuenta = new Cuenta();
                    cuenta.setNumeroCuenta(rs.getString("numero_cuenta"));
                    cuenta.setSaldo(rs.getDouble("saldo"));
                    cuenta.setIdCliente(rs.getInt("id_cliente"));
                    cuenta.setIdSucursal(rs.getInt("id_sucursal"));
                    cuenta.setEstado(rs.getString("estado"));
                }
            }
        }
        return cuenta;
    }

    @Override
    public boolean actualizarSaldo(Connection con, String numeroCuenta, double nuevoSaldo) throws SQLException {
        String sql = "UPDATE cuenta SET saldo = ? WHERE numero_cuenta = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, nuevoSaldo);
            ps.setString(2, numeroCuenta);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public List<CuentaClienteDTO> obtenerCuentasConClientes(int sucursalId) {
        List<CuentaClienteDTO> lista = new ArrayList<>();
        // RQF-002: INNER JOIN entre cuenta y cliente
        String sql = "SELECT cu.numero_cuenta, cu.saldo, cu.id_cliente, cl.nombre, cl.apellido " +
                     "FROM cuenta cu " +
                     "INNER JOIN cliente cl ON cu.id_cliente = cl.id_cliente " +
                     "WHERE cu.id_sucursal = ?";
        
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, sucursalId);
            try (ResultSet rs = ps.executeQuery()) {
                GestorConcurrencia gestor = GestorConcurrencia.getInstancia();
                while (rs.next()) {
                    CuentaClienteDTO dto = new CuentaClienteDTO();
                    String nroCuenta = rs.getString("numero_cuenta");
                    dto.setNumeroCuenta(nroCuenta);
                    dto.setSaldo(rs.getDouble("saldo"));
                    dto.setIdCliente(rs.getInt("id_cliente"));
                    dto.setNombreCliente(rs.getString("nombre"));
                    dto.setApellidoCliente(rs.getString("apellido"));
                    // Integración con el semáforo en memoria
                    dto.setDisponibilidad(gestor.getEstadoCuenta(nroCuenta));
                    lista.add(dto);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en obtenerCuentasConClientes: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean actualizarSaldo(String numeroCuenta, double nuevoSaldo) {
        try (Connection con = ConexionDB.getConexion()) {
            return actualizarSaldo(con, numeroCuenta, nuevoSaldo);
        } catch (SQLException e) {
            System.err.println("Error en actualizarSaldo: " + e.getMessage());
            return false;
        }
    }

    @Override
    public CuentaClienteDTO obtenerCuentaPorDni(String dni) {
        CuentaClienteDTO dto = null;
        String sql = "SELECT cu.numero_cuenta, cu.saldo, cu.id_cliente, cl.nombre, cl.apellido " +
                     "FROM cuenta cu " +
                     "INNER JOIN cliente cl ON cu.id_cliente = cl.id_cliente " +
                     "WHERE cl.dni = ?";
        
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    dto = new CuentaClienteDTO();
                    String nroCuenta = rs.getString("numero_cuenta");
                    dto.setNumeroCuenta(nroCuenta);
                    dto.setSaldo(rs.getDouble("saldo"));
                    dto.setIdCliente(rs.getInt("id_cliente"));
                    dto.setNombreCliente(rs.getString("nombre"));
                    dto.setApellidoCliente(rs.getString("apellido"));
                    // Integración con el semáforo en memoria
                    GestorConcurrencia gestor = GestorConcurrencia.getInstancia();
                    dto.setDisponibilidad(gestor.getEstadoCuenta(nroCuenta));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en obtenerCuentaPorDni: " + e.getMessage());
        }
        return dto;
    }
}
