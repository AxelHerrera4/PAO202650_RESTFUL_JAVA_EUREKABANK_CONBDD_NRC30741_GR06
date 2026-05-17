package com.eurekabank.modelo;

/**
 * Entidad Empleado.
 * @author Antigravity
 */
public class Empleado {
    private int idEmpleado;
    private String usuario;
    private String password;
    private String nombreCompleto;
    private String rol;

    public Empleado() {}

    public Empleado(int idEmpleado, String usuario, String password, String nombreCompleto, String rol) {
        this.idEmpleado = idEmpleado;
        this.usuario = usuario;
        this.password = password;
        this.nombreCompleto = nombreCompleto;
        this.rol = rol;
    }

    // Getters y Setters
    public int getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(int idEmpleado) { this.idEmpleado = idEmpleado; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    @Override
    public String toString() {
        return "Empleado{" + "idEmpleado=" + idEmpleado + ", usuario=" + usuario + ", nombreCompleto=" + nombreCompleto + ", rol=" + rol + '}';
    }
}
