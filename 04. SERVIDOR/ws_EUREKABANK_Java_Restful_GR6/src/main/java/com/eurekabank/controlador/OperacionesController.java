package com.eurekabank.controlador;

import com.eurekabank.modelo.Cliente;
import com.eurekabank.modelo.Empleado;
import com.eurekabank.modelo.Movimiento;
import com.eurekabank.modelo.CuentaClienteDTO;
import com.eurekabank.servicio.SeguridadService;
import com.eurekabank.servicio.TransaccionService;
import java.util.List;

/**
 * Orquestador del sistema Eureka Bank.
 * Responsable de capturar excepciones de negocio y retornar respuestas amigables.
 */
public class OperacionesController {

    private final SeguridadService seguridadService;
    private final TransaccionService transaccionService;

    public OperacionesController() {
        this.seguridadService = new SeguridadService();
        this.transaccionService = new TransaccionService();
    }

    /**
     * Gestiona la autenticación de usuarios (Empleado o Cliente).
     */
    public String loginEmpleado(String usuario, String clave) {
        // 1. Intentar login como Empleado (Gerente/Cajero)
        Empleado emp = seguridadService.login(usuario, clave);
        if (emp != null) {
            return "SUCCESS: Bienvenido " + emp.getNombreCompleto() + " (" + emp.getRol() + ")";
        }
        
        // 2. Si falla, intentar login como Cliente usando el DNI
        Cliente cli = seguridadService.loginCliente(usuario);
        if (cli != null) {
            return "SUCCESS: Bienvenido " + cli.getNombre() + " " + cli.getApellido() + " (Cliente)";
        }
        
        return "ERROR: Credenciales inválidas o DNI no registrado.";
    }

    /**
     * Orquesta la operación de depósito.
     */
    public String procesarDeposito(String cuentaId, double monto, int empleadoId) {
        try {
            String operacion = transaccionService.realizarDeposito(cuentaId, monto, empleadoId);
            return "SUCCESS: Depósito realizado. Nro Operación: " + operacion;
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }

    /**
     * Orquesta la operación de retiro.
     */
    public String procesarRetiro(String cuentaId, double monto, int empleadoId) {
        try {
            String operacion = transaccionService.realizarRetiro(cuentaId, monto, empleadoId);
            return "SUCCESS: Retiro realizado. Nro Operación: " + operacion;
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }

    /**
     * Orquesta la transferencia entre cuentas.
     */
    public String procesarTransferencia(String cuentaOrigen, String cuentaDestino, double monto, int empleadoId) {
        try {
            String operacion = transaccionService.realizarTransferencia(cuentaOrigen, cuentaDestino, monto, empleadoId);
            return "SUCCESS: Transferencia exitosa. Nro Operación: " + operacion;
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }

    /**
     * Obtiene la radiografía de la sucursal.
     */
    public List<CuentaClienteDTO> obtenerCuentasSucursal(int sucursalId) {
        return transaccionService.obtenerCuentasSucursal(sucursalId);
    }

    /**
     * Obtiene el historial de movimientos.
     */
    public List<Movimiento> obtenerHistorial(String cuentaId) {
        return transaccionService.obtenerExtracto(cuentaId);
    }

    /**
     * Obtiene la cuenta de un cliente en base a su DNI.
     */
    public CuentaClienteDTO obtenerCuentaPorDni(String dni) {
        return transaccionService.obtenerCuentaClientePorDni(dni);
    }
}
