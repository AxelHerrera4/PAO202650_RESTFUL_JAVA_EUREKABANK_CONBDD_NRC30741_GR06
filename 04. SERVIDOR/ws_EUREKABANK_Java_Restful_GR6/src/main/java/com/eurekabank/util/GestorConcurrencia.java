package com.eurekabank.util;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Gestor de Concurrencia (Semáforo Verde/Rojo) para Cuentas.
 * Utiliza el patrón Singleton y un ConcurrentHashMap para hilos seguros.
 * @author Antigravity
 */
public class GestorConcurrencia {

    private static GestorConcurrencia instancia;
    
    // Almacena el estado de la cuenta: Key=NumeroCuenta, Value=Estado ("VERDE" o "ROJO")
    private final ConcurrentHashMap<String, String> estadosCuentas;

    private static final String VERDE = "VERDE";
    private static final String ROJO = "ROJO";

    private GestorConcurrencia() {
        estadosCuentas = new ConcurrentHashMap<>();
    }

    /**
     * Obtiene la instancia única del gestor.
     * @return GestorConcurrencia
     */
    public static synchronized GestorConcurrencia getInstancia() {
        if (instancia == null) {
            instancia = new GestorConcurrencia();
        }
        return instancia;
    }

    /**
     * Intenta bloquear una cuenta para una operación transaccional.
     * @param numeroCuenta Identificador de la cuenta.
     * @return true si se bloqueó con éxito (pasó de Verde a Rojo), false si ya estaba en Rojo.
     */
    public synchronized boolean bloquearCuenta(String numeroCuenta) {
        String estadoActual = estadosCuentas.getOrDefault(numeroCuenta, VERDE);
        
        if (estadoActual.equals(VERDE)) {
            estadosCuentas.put(numeroCuenta, ROJO);
            System.out.println("[Concurrencia] Cuenta " + numeroCuenta + " bloqueada (ROJO).");
            return true;
        }
        
        System.out.println("[Concurrencia] Intento fallido: Cuenta " + numeroCuenta + " ya está ocupada (ROJO).");
        return false;
    }

    /**
     * Libera una cuenta después de finalizar la operación.
     * @param numeroCuenta Identificador de la cuenta.
     */
    public void liberarCuenta(String numeroCuenta) {
        estadosCuentas.put(numeroCuenta, VERDE);
        System.out.println("[Concurrencia] Cuenta " + numeroCuenta + " liberada (VERDE).");
    }

    /**
     * Consulta el estado actual de una cuenta.
     * @param numeroCuenta Identificador de la cuenta.
     * @return "VERDE" o "ROJO"
     */
    public String getEstadoCuenta(String numeroCuenta) {
        return estadosCuentas.getOrDefault(numeroCuenta, VERDE);
    }
}
