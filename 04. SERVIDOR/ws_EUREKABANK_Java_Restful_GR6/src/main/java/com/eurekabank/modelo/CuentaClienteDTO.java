package com.eurekabank.modelo;

/**
 * Data Transfer Object para la radiografía operativa de cuentas y clientes.
 * Combina información de Cuenta, Cliente y el estado del GestorConcurrencia.
 */
public class CuentaClienteDTO {
    private String numeroCuenta;
    private double saldo;
    private String disponibilidad; // VERDE o ROJO (del GestorConcurrencia)
    private int idCliente;
    private String nombreCliente;
    private String apellidoCliente;

    public CuentaClienteDTO() {}

    public CuentaClienteDTO(String numeroCuenta, double saldo, String disponibilidad, int idCliente, String nombreCliente, String apellidoCliente) {
        this.numeroCuenta = numeroCuenta;
        this.saldo = saldo;
        this.disponibilidad = disponibilidad;
        this.idCliente = idCliente;
        this.nombreCliente = nombreCliente;
        this.apellidoCliente = apellidoCliente;
    }

    // Getters y Setters
    public String getNumeroCuenta() { return numeroCuenta; }
    public void setNumeroCuenta(String numeroCuenta) { this.numeroCuenta = numeroCuenta; }

    public double getSaldo() { return saldo; }
    public void setSaldo(double saldo) { this.saldo = saldo; }

    public String getDisponibilidad() { return disponibilidad; }
    public void setDisponibilidad(String disponibilidad) { this.disponibilidad = disponibilidad; }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getApellidoCliente() { return apellidoCliente; }
    public void setApellidoCliente(String apellidoCliente) { this.apellidoCliente = apellidoCliente; }
}
