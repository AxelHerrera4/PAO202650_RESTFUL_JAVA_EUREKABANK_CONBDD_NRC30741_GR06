package com.eurekabank.dao;

import com.eurekabank.modelo.Movimiento;
import java.util.List;

/**
 * Interfaz para la persistencia de Movimientos (Auditoría).
 */
public interface IMovimientoDAO {
    boolean registrarMovimiento(Movimiento movimiento);
    // Para transacciones ACID
    boolean registrarMovimiento(java.sql.Connection con, Movimiento movimiento) throws java.sql.SQLException;
    List<Movimiento> listarExtracto(String numeroCuenta);
}
