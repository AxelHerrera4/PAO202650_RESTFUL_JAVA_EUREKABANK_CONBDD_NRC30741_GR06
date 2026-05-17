package com.eurekabank.cliente;

import com.eurekabank.cliente.controlador.LoginController;
import com.eurekabank.cliente.vista.LoginView;
import com.formdev.flatlaf.FlatIntelliJLaf;
import javax.swing.*;

/**
 * Clase principal para iniciar la aplicación Eureka Bank Cliente RESTful.
 */
public class Main {

    public static void main(String[] args) {
        // Configurar el Look and Feel Moderno (FlatLaf)
        try {
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
            
            // Personalización adicional de FlatLaf
            UIManager.put("Button.arc", 10);
            UIManager.put("Component.arc", 10);
            UIManager.put("TextComponent.arc", 10);
            
        } catch (UnsupportedLookAndFeelException e) {
            System.err.println("No se pudo cargar el Look and Feel: " + e.getMessage());
        }

        // Ejecutar en el Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            LoginView vista = new LoginView();
            new LoginController(vista);
            vista.setVisible(true);
        });
    }
}
