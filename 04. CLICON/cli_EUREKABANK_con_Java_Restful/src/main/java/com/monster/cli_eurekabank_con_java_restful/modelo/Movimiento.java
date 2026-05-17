package com.monster.cli_eurekabank_con_java_restful.modelo;

/**
 * Entidad/DTO Movimiento para auditoría.
 * Adaptado para el cliente RESTful.
 */
public class Movimiento {
    private int idMovimiento;
    private String numeroOperacion;
    private String numeroCuenta;
    private int idEmpleado;
    private String nombreEmpleado; // Campo extra para el reporte
    private String tipo;
    private double monto;
    private String fechaHora;

    public Movimiento() {}

    public Movimiento(int idMovimiento, String numeroOperacion, String numeroCuenta, int idEmpleado, String tipo, double monto, String fechaHora) {
        this.idMovimiento = idMovimiento;
        this.numeroOperacion = numeroOperacion;
        this.numeroCuenta = numeroCuenta;
        this.idEmpleado = idEmpleado;
        this.tipo = tipo;
        this.monto = monto;
        this.fechaHora = fechaHora;
    }

    // Getters y Setters
    public int getIdMovimiento() { return idMovimiento; }
    public void setIdMovimiento(int idMovimiento) { this.idMovimiento = idMovimiento; }

    public String getNumeroOperacion() { return numeroOperacion; }
    public void setNumeroOperacion(String numeroOperacion) { this.numeroOperacion = numeroOperacion; }

    public String getNumeroCuenta() { return numeroCuenta; }
    public void setNumeroCuenta(String numeroCuenta) { this.numeroCuenta = numeroCuenta; }

    public int getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(int idEmpleado) { this.idEmpleado = idEmpleado; }

    public String getNombreEmpleado() { return nombreEmpleado; }
    public void setNombreEmpleado(String nombreEmpleado) { this.nombreEmpleado = nombreEmpleado; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }

    public String getFechaHora() { return fechaHora; }
    public void setFechaHora(String fechaHora) { this.fechaHora = fechaHora; }
}
