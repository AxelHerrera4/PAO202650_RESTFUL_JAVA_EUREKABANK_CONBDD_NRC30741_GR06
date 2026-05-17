package com.eurekabank.dao;

import com.eurekabank.modelo.Empleado;

/**
 * Interfaz para la persistencia de Empleados.
 */
public interface IEmpleadoDAO {
    /**
     * Valida las credenciales de un empleado.
     * @param usuario
     * @param password
     * @return El objeto Empleado si es válido, null en caso contrario.
     */
    Empleado validarLogin(String usuario, String password);
}
