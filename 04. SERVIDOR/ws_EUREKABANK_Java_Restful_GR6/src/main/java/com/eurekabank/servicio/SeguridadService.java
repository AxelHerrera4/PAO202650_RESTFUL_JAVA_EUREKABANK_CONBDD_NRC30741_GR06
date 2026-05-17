package com.eurekabank.servicio;

import com.eurekabank.dao.ClienteDAOImpl;
import com.eurekabank.dao.EmpleadoDAOImpl;
import com.eurekabank.dao.IClienteDAO;
import com.eurekabank.dao.IEmpleadoDAO;
import com.eurekabank.modelo.Cliente;
import com.eurekabank.modelo.Empleado;

/**
 * Servicio para la gestión de seguridad y autenticación.
 */
public class SeguridadService {

    private final IEmpleadoDAO empleadoDAO;
    private final IClienteDAO clienteDAO;

    public SeguridadService() {
        this.empleadoDAO = new EmpleadoDAOImpl();
        this.clienteDAO = new ClienteDAOImpl();
    }

    /**
     * Valida el acceso del empleado al sistema.
     */
    public Empleado login(String usuario, String password) {
        return empleadoDAO.validarLogin(usuario, password);
    }

    /**
     * Valida el acceso del cliente por DNI.
     */
    public Cliente loginCliente(String dni) {
        return clienteDAO.validarLogin(dni);
    }
}
