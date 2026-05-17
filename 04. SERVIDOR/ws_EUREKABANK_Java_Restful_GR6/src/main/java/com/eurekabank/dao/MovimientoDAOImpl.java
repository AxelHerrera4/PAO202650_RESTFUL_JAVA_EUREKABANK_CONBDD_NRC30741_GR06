package com.eurekabank.dao;

import com.eurekabank.modelo.Movimiento;
import com.eurekabank.util.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de IMovimientoDAO con soporte para nombres de empleado y fechas formateadas.
 */
public class MovimientoDAOImpl implements IMovimientoDAO {

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    @Override
    public boolean registrarMovimiento(Movimiento movimiento) {
        try (Connection con = ConexionDB.getConexion()) {
            return registrarMovimiento(con, movimiento);
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean registrarMovimiento(Connection con, Movimiento movimiento) throws SQLException {
        String sql = "INSERT INTO movimiento (numero_operacion, numero_cuenta, id_empleado, tipo, monto) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, movimiento.getNumeroOperacion());
            ps.setString(2, movimiento.getNumeroCuenta());
            ps.setInt(3, movimiento.getIdEmpleado());
            ps.setString(4, movimiento.getTipo());
            ps.setDouble(5, movimiento.getMonto());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public List<Movimiento> listarExtracto(String numeroCuenta) {
        List<Movimiento> movimientos = new ArrayList<>();
        // JOIN con la tabla empleado para obtener el nombre del responsable
        String sql = "SELECT m.*, e.nombre_completo FROM movimiento m " +
                     "JOIN empleado e ON m.id_empleado = e.id_empleado " +
                     "WHERE m.numero_cuenta = ? ORDER BY m.fecha_hora DESC";
        
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, numeroCuenta);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Movimiento mov = new Movimiento();
                    mov.setIdMovimiento(rs.getInt("id_movimiento"));
                    mov.setNumeroOperacion(rs.getString("numero_operacion"));
                    mov.setNumeroCuenta(rs.getString("numero_cuenta"));
                    mov.setIdEmpleado(rs.getInt("id_empleado"));
                    mov.setNombreEmpleado(rs.getString("nombre_completo"));
                    mov.setTipo(rs.getString("tipo"));
                    mov.setMonto(rs.getDouble("monto"));
                    
                    // Formatear la fecha a String antes de enviarla por SOAP
                    if (rs.getTimestamp("fecha_hora") != null) {
                        mov.setFechaHora(sdf.format(rs.getTimestamp("fecha_hora")));
                    }
                    
                    movimientos.add(mov);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en listarExtracto: " + e.getMessage());
        }
        return movimientos;
    }
}
