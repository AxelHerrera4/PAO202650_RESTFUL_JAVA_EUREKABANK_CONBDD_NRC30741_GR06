package com.eurekabank.cliente.controlador;

import com.eurekabank.cliente.vista.CajeroDashboardView;
import com.eurekabank.cliente.modelo.CuentaClienteDTO;
import com.eurekabank.cliente.modelo.Movimiento;
import com.eurekabank.cliente.servicio.EurekaBankRestClient;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Controlador para la Pantalla Principal del Cajero.
 * Gestiona la lógica de búsqueda de cuentas y procesamiento de transacciones consumiendo la API REST.
 */
public class CajeroDashboardController implements ActionListener {

    private final CajeroDashboardView vista;
    private final EurekaBankRestClient restClient;
    private final int idEmpleado;
    private final String nombreEmpleado;
    private String cuentaActivaId = null;

    public CajeroDashboardController(CajeroDashboardView vista, int idEmpleado, String nombreEmpleado) {
        this.vista = vista;
        this.idEmpleado = idEmpleado;
        this.nombreEmpleado = nombreEmpleado;
        
        // Inicializar el cliente REST
        this.restClient = new EurekaBankRestClient();
        
        // Asignar listeners
        this.vista.getBtnBuscarCuenta().addActionListener(this);
        this.vista.getBtnProcesarDeposito().addActionListener(this);
        this.vista.getBtnProcesarRetiro().addActionListener(this);
        this.vista.getBtnProcesarTransferencia().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.getBtnBuscarCuenta()) {
            buscarCuenta();
        } else if (e.getSource() == vista.getBtnProcesarDeposito()) {
            procesarDeposito();
        } else if (e.getSource() == vista.getBtnProcesarRetiro()) {
            procesarRetiro();
        } else if (e.getSource() == vista.getBtnProcesarTransferencia()) {
            procesarTransferencia();
        }
    }

    private void buscarCuenta() {
        String numCuenta = vista.getNumCuentaBusqueda();
        if (numCuenta.isEmpty()) {
            vista.mostrarMensaje("Ingrese un número de cuenta.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            CuentaClienteDTO cuenta = null;
            // Recorrer sucursales del 1 al 5 como en el cliente original
            for (int i = 1; i <= 5; i++) {
                List<CuentaClienteDTO> cuentas = restClient.listarCuentasPorSucursal(i);
                if (cuentas != null) {
                    cuenta = cuentas.stream()
                            .filter(c -> c.getNumeroCuenta().equals(numCuenta))
                            .findFirst()
                            .orElse(null);
                    if (cuenta != null) break;
                }
            }

            if (cuenta != null) {
                cuentaActivaId = cuenta.getNumeroCuenta();
                vista.setTitular(cuenta.getNombreCliente() + " " + cuenta.getApellidoCliente());
                vista.setSaldo("S/ " + String.format("%.2f", cuenta.getSaldo()));
                vista.setEstado(cuenta.getDisponibilidad());
                cargarMovimientos(cuentaActivaId);
            } else {
                vista.mostrarMensaje("Cuenta no encontrada.", "Error", JOptionPane.ERROR_MESSAGE);
                limpiarDatosCuenta();
            }
        } catch (Exception ex) {
            vista.mostrarMensaje("Error: " + ex.getMessage(), "Error de Red", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarMovimientos(String cuentaId) {
        try {
            List<Movimiento> movimientos = restClient.consultarExtracto(cuentaId);
            vista.getModelMovimientos().setRowCount(0);
            if (movimientos != null) {
                for (Movimiento mov : movimientos) {
                    vista.getModelMovimientos().addRow(new Object[]{
                        mov.getNumeroOperacion(),
                        mov.getFechaHora() != null ? mov.getFechaHora() : "N/A",
                        mov.getTipo(),
                        "S/ " + String.format("%.2f", mov.getMonto()),
                        mov.getNombreEmpleado() != null ? mov.getNombreEmpleado() : "Desconocido"
                    });
                }
            }
        } catch (Exception ex) {
            vista.mostrarMensaje("Error al cargar historial: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void procesarDeposito() {
        if (cuentaActivaId == null) return;
        try {
            double monto = Double.parseDouble(vista.getMontoDeposito());
            manejarRespuesta(restClient.depositar(cuentaActivaId, monto, idEmpleado));
        } catch (NumberFormatException e) {
            vista.mostrarMensaje("Monto inválido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void procesarRetiro() {
        if (cuentaActivaId == null) return;
        try {
            double monto = Double.parseDouble(vista.getMontoRetiro());
            manejarRespuesta(restClient.retirar(cuentaActivaId, monto, idEmpleado));
        } catch (NumberFormatException e) {
            vista.mostrarMensaje("Monto inválido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void procesarTransferencia() {
        if (cuentaActivaId == null) return;
        try {
            String destino = vista.getCuentaDestino();
            double monto = Double.parseDouble(vista.getMontoTransferencia());
            manejarRespuesta(restClient.transferir(cuentaActivaId, destino, monto, idEmpleado));
        } catch (NumberFormatException e) {
            vista.mostrarMensaje("Datos inválidos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void manejarRespuesta(String respuesta) {
        if (respuesta != null && respuesta.contains("SUCCESS")) {
            vista.mostrarMensaje("Operación exitosa.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            vista.limpiarCamposTransaccion();
            buscarCuenta();
        } else {
            String errorMsg = (respuesta != null && respuesta.contains("ERROR")) 
                    ? respuesta.replace("ERROR:", "").trim() 
                    : "No se pudo realizar la transacción.";
            vista.mostrarMensaje(errorMsg, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarDatosCuenta() {
        cuentaActivaId = null;
        vista.setTitular("-");
        vista.setSaldo("S/ 0.00");
        vista.setEstado("-");
        vista.getModelMovimientos().setRowCount(0);
    }
}
