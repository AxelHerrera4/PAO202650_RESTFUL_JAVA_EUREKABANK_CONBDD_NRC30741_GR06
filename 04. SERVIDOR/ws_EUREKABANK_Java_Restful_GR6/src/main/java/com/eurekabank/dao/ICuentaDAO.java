package com.eurekabank.dao;

import com.eurekabank.modelo.Cuenta;
import com.eurekabank.modelo.CuentaClienteDTO;
import java.util.List;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Interfaz para la persistencia de Cuentas.
 */
public interface ICuentaDAO {
    Cuenta obtenerCuenta(String numeroCuenta);
    
    // Métodos para transacciones ACID con SELECT FOR UPDATE
    Cuenta obtenerCuentaForUpdate(Connection con, String numeroCuenta) throws SQLException;
    boolean actualizarSaldo(Connection con, String numeroCuenta, double nuevoSaldo) throws SQLException;
    
    // Método para RQF-002: INNER JOIN
    List<CuentaClienteDTO> obtenerCuentasConClientes(int sucursalId);
    
    // Versión no transaccional (opcional)
    boolean actualizarSaldo(String numeroCuenta, double nuevoSaldo);
    
    // Obtener cuenta asociada a un cliente por su DNI
    CuentaClienteDTO obtenerCuentaPorDni(String dni);
}
