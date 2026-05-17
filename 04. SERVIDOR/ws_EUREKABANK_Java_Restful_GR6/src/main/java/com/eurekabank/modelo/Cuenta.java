package com.eurekabank.modelo;

/**
 * Entidad Cuenta.
 * @author Antigravity
 */
public class Cuenta {
    private String numeroCuenta;
    private double saldo;
    private int idCliente;
    private int idSucursal;
    private String estado; // Activa, Bloqueada

    public Cuenta() {}

    public Cuenta(String numeroCuenta, double saldo, int idCliente, int idSucursal, String estado) {
        this.numeroCuenta = numeroCuenta;
        this.saldo = saldo;
        this.idCliente = idCliente;
        this.idSucursal = idSucursal;
        this.estado = estado;
    }

    // Getters y Setters
    public String getNumeroCuenta() { return numeroCuenta; }
    public void setNumeroCuenta(String numeroCuenta) { this.numeroCuenta = numeroCuenta; }

    public double getSaldo() { return saldo; }
    public void setSaldo(double saldo) { this.saldo = saldo; }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public int getIdSucursal() { return idSucursal; }
    public void setIdSucursal(int idSucursal) { this.idSucursal = idSucursal; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() {
        return "Cuenta{" + "numeroCuenta=" + numeroCuenta + ", saldo=" + saldo + ", estado=" + estado + '}';
    }
}
