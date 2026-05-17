package com.eurekabank.cliente.controlador;

import com.eurekabank.cliente.vista.LoginView;
import com.eurekabank.cliente.vista.CajeroDashboardView;
import com.eurekabank.cliente.servicio.EurekaBankRestClient;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controlador para la gestión del inicio de sesión.
 * Conecta la vista con el servicio RESTful y lanza el Dashboard.
 */
public class LoginController implements ActionListener {

    private final LoginView vista;
    private final EurekaBankRestClient restClient;

    public LoginController(LoginView vista) {
        this.vista = vista;
        
        // Inicializar el cliente RESTful
        this.restClient = new EurekaBankRestClient();
        
        // Asignar eventos
        this.vista.getBtnIngresar().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.getBtnIngresar()) {
            ejecutarLogin();
        }
    }

    private void ejecutarLogin() {
        String usuario = vista.getUsuario();
        String clave = vista.getClave();

        if (usuario.isEmpty() || clave.isEmpty()) {
            vista.mostrarMensaje("Por favor, complete todos los campos.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            vista.getBtnIngresar().setEnabled(false);
            
            // Llamada al servicio REST
            String respuesta = restClient.autenticarUsuario(usuario, clave);

            if (respuesta != null && respuesta.contains("SUCCESS")) {
                int idEmp = 1; // Valor por defecto
                String nomEmp = "Usuario Eureka";

                try {
                    // Intento de parseo robusto de respuestas estilo:
                    // "SUCCESS: Bienvenido NOMBRE COMPLETO (Rol)"
                    String[] partes = respuesta.split(":");
                    
                    if (partes.length > 1) {
                        String posibleId = partes[1].trim();
                        if (posibleId.matches("\\d+")) {
                            idEmp = Integer.parseInt(posibleId);
                            if (partes.length > 2) {
                                nomEmp = partes[2].trim();
                            }
                        } else {
                            // Si no contiene un ID numérico explícito, limpiaremos el texto de bienvenida
                            nomEmp = posibleId.replace("Bienvenido", "").replace("monster", "").trim();
                        }
                    }
                } catch (Exception ex) {
                    System.err.println("Aviso: Error al parsear respuesta, usando valores por defecto.");
                }

                vista.mostrarMensaje("Acceso Concedido\n" + nomEmp, "Eureka Bank", JOptionPane.INFORMATION_MESSAGE);
                vista.dispose();
                
                // Abrir Dashboard
                final int finalId = idEmp;
                final String finalNom = nomEmp;
                SwingUtilities.invokeLater(() -> {
                    CajeroDashboardView dashView = new CajeroDashboardView(finalNom);
                    new CajeroDashboardController(dashView, finalId, finalNom);
                    dashView.setVisible(true);
                });
            } else {
                String errorMsg = (respuesta != null && respuesta.contains("ERROR")) 
                        ? respuesta.replace("ERROR:", "").trim() 
                        : "Credenciales inválidas o error de sistema.";
                vista.mostrarMensaje(errorMsg, "Error de Autenticación", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            vista.mostrarMensaje("Error de conexión: " + ex.getMessage(), "Error de Red", JOptionPane.ERROR_MESSAGE);
        } finally {
            vista.getBtnIngresar().setEnabled(true);
        }
    }
}
