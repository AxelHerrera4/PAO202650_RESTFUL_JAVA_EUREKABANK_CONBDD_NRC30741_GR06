package com.eurekabank.cliente.vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Vista de Inicio de Sesión para Eureka Bank.
 * Diseño moderno con Layout dividido: Formulario a la izquierda e Imagen a la derecha.
 * Corrección de alineación: Todos los elementos centrados.
 */
public class LoginView extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtClave;
    private JButton btnIngresar;
    private JPanel mainPanel;
    private JLabel lblTitulo;
    private JLabel lblSubtitulo;

    public LoginView() {
        initComponents();
        configureFrame();
    }

    private void initComponents() {
        // Colores corporativos
        Color colorPrimario = new Color(0, 102, 204);
        Color colorTexto = new Color(51, 51, 51);
        Color colorBlanco = Color.WHITE;

        // Fuentes
        Font fontTitulo = new Font("Segoe UI", Font.BOLD, 32);
        Font fontSubtitulo = new Font("Segoe UI", Font.PLAIN, 16);
        Font fontLabel = new Font("Segoe UI", Font.BOLD, 14);
        Font fontInput = new Font("Segoe UI", Font.PLAIN, 15);
        Font fontBoton = new Font("Segoe UI", Font.BOLD, 16);

        // Panel Principal - Split Layout
        mainPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.setBackground(colorBlanco);

        // --- PANEL IZQUIERDO: FORMULARIO ---
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(colorBlanco);
        
        JPanel formContainer = new JPanel();
        formContainer.setLayout(new BoxLayout(formContainer, BoxLayout.Y_AXIS));
        formContainer.setBackground(colorBlanco);
        formContainer.setMaximumSize(new Dimension(350, 600));

        // Títulos
        lblTitulo = new JLabel("Eureka Bank");
        lblTitulo.setFont(fontTitulo);
        lblTitulo.setForeground(colorPrimario);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblSubtitulo = new JLabel("Acceso al Sistema de Cajeros");
        lblSubtitulo.setFont(fontSubtitulo);
        lblSubtitulo.setForeground(new Color(102, 102, 102));
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSubtitulo.setBorder(new EmptyBorder(0, 0, 50, 0));

        // Usuario
        JLabel lblUser = new JLabel("Usuario");
        lblUser.setFont(fontLabel);
        lblUser.setForeground(colorTexto);
        lblUser.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtUsuario = new JTextField();
        txtUsuario.setFont(fontInput);
        txtUsuario.setPreferredSize(new Dimension(320, 45));
        txtUsuario.setMaximumSize(new Dimension(320, 45));
        txtUsuario.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtUsuario.putClientProperty("JTextField.placeholderText", "Nombre de usuario");
        txtUsuario.putClientProperty("JComponent.roundRect", true);

        // Contraseña
        JLabel lblPass = new JLabel("Contraseña");
        lblPass.setFont(fontLabel);
        lblPass.setForeground(colorTexto);
        lblPass.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblPass.setBorder(new EmptyBorder(25, 0, 0, 0));

        txtClave = new JPasswordField();
        txtClave.setFont(fontInput);
        txtClave.setPreferredSize(new Dimension(320, 45));
        txtClave.setMaximumSize(new Dimension(320, 45));
        txtClave.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtClave.putClientProperty("JTextField.placeholderText", "••••••••");
        txtClave.putClientProperty("JComponent.roundRect", true);

        // Botón
        btnIngresar = new JButton("INICIAR SESIÓN");
        btnIngresar.setFont(fontBoton);
        btnIngresar.setBackground(colorPrimario);
        btnIngresar.setForeground(colorBlanco);
        btnIngresar.setFocusPainted(false);
        btnIngresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnIngresar.setPreferredSize(new Dimension(320, 50));
        btnIngresar.setMaximumSize(new Dimension(320, 50));
        btnIngresar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnIngresar.putClientProperty("JButton.buttonType", "roundRect");
        
        // Espaciadores y Ensamblado
        formContainer.add(lblTitulo);
        formContainer.add(Box.createRigidArea(new Dimension(0, 5)));
        formContainer.add(lblSubtitulo);
        formContainer.add(lblUser);
        formContainer.add(Box.createRigidArea(new Dimension(0, 10)));
        formContainer.add(txtUsuario);
        formContainer.add(lblPass);
        formContainer.add(Box.createRigidArea(new Dimension(0, 10)));
        formContainer.add(txtClave);
        formContainer.add(Box.createRigidArea(new Dimension(0, 45)));
        formContainer.add(btnIngresar);

        leftPanel.add(formContainer);

        // --- PANEL DERECHO: IMAGEN ---
        JPanel rightPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    java.net.URL imgUrl = getClass().getResource("/images/login_side.jpg");
                    if (imgUrl != null) {
                        Image img = new ImageIcon(imgUrl).getImage();
                        g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
                    } else {
                        g.setColor(new Color(0, 102, 204));
                        g.fillRect(0, 0, getWidth(), getHeight());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);
        setContentPane(mainPanel);
    }

    private void configureFrame() {
        setTitle("Eureka Bank - Gestión de Cajeros");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(800, 600));
    }

    public String getUsuario() {
        return txtUsuario.getText();
    }

    public String getClave() {
        return new String(txtClave.getPassword());
    }

    public JButton getBtnIngresar() {
        return btnIngresar;
    }

    public void mostrarMensaje(String mensaje, String titulo, int tipo) {
        UIManager.put("OptionPane.background", Color.WHITE);
        UIManager.put("Panel.background", Color.WHITE);
        JOptionPane.showMessageDialog(this, mensaje, titulo, tipo);
    }
}
