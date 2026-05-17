package com.eurekabank.cliente.vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Pantalla Principal del Cajero para Eureka Bank.
 * Diseño profesional, corporativo y organizado utilizando componentes FlatLaf.
 * 
 * @author Antigravity
 */
public class CajeroDashboardView extends JFrame {

    // Componentes de Cabecera
    private JLabel lblBienvenida;
    
    // Componentes de Búsqueda
    private JTextField txtNumCuentaBusqueda;
    private JButton btnBuscarCuenta;
    private JLabel lblSaldoVal, lblTitularVal, lblEstadoVal;
    
    // Componentes de Transacciones
    private JTabbedPane tabbedPane;
    private JTextField txtMontoDeposito, txtMontoRetiro, txtMontoTransferencia, txtCuentaDestino;
    private JButton btnProcesarDeposito, btnProcesarRetiro, btnProcesarTransferencia;
    
    // Componentes de Auditoría
    private JTable tblMovimientos;
    private DefaultTableModel modelMovimientos;

    public CajeroDashboardView(String nombreCajero) {
        initComponents(nombreCajero);
        configureFrame();
    }

    private void initComponents(String nombreCajero) {
        Color colorPrimario = new Color(0, 102, 204);
        Color colorFondo = new Color(245, 247, 250);
        Color colorBlanco = Color.WHITE;
        
        Font fontLabelHeader = new Font("Segoe UI", Font.BOLD, 18);
        Font fontLabelInfo = new Font("Segoe UI", Font.BOLD, 14);
        Font fontValorInfo = new Font("Segoe UI", Font.PLAIN, 14);

        // --- PANEL PRINCIPAL ---
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(colorFondo);
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // 1. CABECERA
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(colorPrimario);
        headerPanel.setPreferredSize(new Dimension(0, 60));
        headerPanel.setBorder(new EmptyBorder(0, 20, 0, 20));
        
        lblBienvenida = new JLabel("Cajero: " + nombreCajero);
        lblBienvenida.setFont(fontLabelHeader);
        lblBienvenida.setForeground(colorBlanco);
        
        JLabel lblLogo = new JLabel("EUREKA BANK - SISTEMA DE CAJEROS");
        lblLogo.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblLogo.setForeground(colorBlanco);
        
        headerPanel.add(lblBienvenida, BorderLayout.WEST);
        headerPanel.add(lblLogo, BorderLayout.EAST);

        // 2. SECCIÓN CENTRAL (BÚSQUEDA Y TRANSACCIONES)
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 10, 0);

        // --- Sub-panel de Búsqueda ---
        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBackground(colorBlanco);
        searchPanel.setBorder(BorderFactory.createTitledBorder("Búsqueda de Cuenta"));
        
        GridBagConstraints gbcSearch = new GridBagConstraints();
        gbcSearch.insets = new Insets(10, 10, 10, 10);
        
        gbcSearch.gridx = 0; gbcSearch.gridy = 0;
        searchPanel.add(new JLabel("Número de Cuenta:"), gbcSearch);
        
        txtNumCuentaBusqueda = new JTextField(15);
        gbcSearch.gridx = 1;
        searchPanel.add(txtNumCuentaBusqueda, gbcSearch);
        
        btnBuscarCuenta = new JButton("Buscar Cuenta");
        btnBuscarCuenta.setBackground(colorPrimario);
        btnBuscarCuenta.setForeground(colorBlanco);
        gbcSearch.gridx = 2;
        searchPanel.add(btnBuscarCuenta, gbcSearch);
        
        // Info de la cuenta
        JPanel infoPanel = new JPanel(new GridLayout(1, 6, 10, 0));
        infoPanel.setOpaque(false);
        infoPanel.add(new JLabel("Titular:"));
        lblTitularVal = new JLabel("-"); lblTitularVal.setFont(fontValorInfo);
        infoPanel.add(lblTitularVal);
        
        infoPanel.add(new JLabel("Saldo:"));
        lblSaldoVal = new JLabel("S/ 0.00"); lblSaldoVal.setFont(fontValorInfo); lblSaldoVal.setForeground(colorPrimario);
        infoPanel.add(lblSaldoVal);
        
        infoPanel.add(new JLabel("Estado:"));
        lblEstadoVal = new JLabel("-"); lblEstadoVal.setFont(fontValorInfo);
        infoPanel.add(lblEstadoVal);
        
        gbcSearch.gridx = 0; gbcSearch.gridy = 1; gbcSearch.gridwidth = 3;
        searchPanel.add(infoPanel, gbcSearch);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0; gbc.weighty = 0.3;
        centerPanel.add(searchPanel, gbc);

        // --- Sub-panel de Transacciones (TabbedPane) ---
        tabbedPane = new JTabbedPane();
        
        // Pestaña Depósito
        JPanel pnlDeposito = createTransactionPanel("Procesar Depósito", "Monto a depositar:", txtMontoDeposito = new JTextField(10), btnProcesarDeposito = new JButton("Depositar"));
        tabbedPane.addTab("Depósito", pnlDeposito);
        
        // Pestaña Retiro
        JPanel pnlRetiro = createTransactionPanel("Procesar Retiro", "Monto a retirar:", txtMontoRetiro = new JTextField(10), btnProcesarRetiro = new JButton("Retirar"));
        tabbedPane.addTab("Retiro", pnlRetiro);
        
        // Pestaña Transferencia
        JPanel pnlTransferencia = new JPanel(new GridBagLayout());
        pnlTransferencia.setBackground(colorBlanco);
        GridBagConstraints gbcT = new GridBagConstraints();
        gbcT.insets = new Insets(10, 10, 10, 10);
        gbcT.gridx = 0; gbcT.gridy = 0;
        pnlTransferencia.add(new JLabel("Cuenta Destino:"), gbcT);
        txtCuentaDestino = new JTextField(15);
        gbcT.gridx = 1;
        pnlTransferencia.add(txtCuentaDestino, gbcT);
        gbcT.gridx = 0; gbcT.gridy = 1;
        pnlTransferencia.add(new JLabel("Monto:"), gbcT);
        txtMontoTransferencia = new JTextField(10);
        gbcT.gridx = 1;
        pnlTransferencia.add(txtMontoTransferencia, gbcT);
        btnProcesarTransferencia = new JButton("Transferir");
        btnProcesarTransferencia.setBackground(colorPrimario);
        btnProcesarTransferencia.setForeground(colorBlanco);
        gbcT.gridx = 0; gbcT.gridy = 2; gbcT.gridwidth = 2;
        pnlTransferencia.add(btnProcesarTransferencia, gbcT);
        tabbedPane.addTab("Transferencia", pnlTransferencia);

        gbc.gridy = 1; gbc.weighty = 0.7;
        centerPanel.add(tabbedPane, gbc);

        // 3. SECCIÓN INFERIOR (AUDITORÍA)
        JPanel auditPanel = new JPanel(new BorderLayout());
        auditPanel.setBackground(colorBlanco);
        auditPanel.setBorder(BorderFactory.createTitledBorder("Historial de Movimientos"));
        
        String[] columnNames = {"Op. #", "Fecha", "Tipo", "Monto", "Responsable"};
        modelMovimientos = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tblMovimientos = new JTable(modelMovimientos);
        tblMovimientos.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(tblMovimientos);
        scrollPane.setPreferredSize(new Dimension(0, 200));
        
        auditPanel.add(scrollPane, BorderLayout.CENTER);

        // Ensamblar todo
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(auditPanel, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
    }

    private JPanel createTransactionPanel(String title, String labelText, JTextField textField, JButton button) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel(labelText), gbc);
        
        gbc.gridx = 1;
        panel.add(textField, gbc);
        
        button.setBackground(new Color(0, 102, 204));
        button.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        panel.add(button, gbc);
        
        return panel;
    }

    private void configureFrame() {
        setTitle("Eureka Bank - Panel de Gestión del Cajero");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 800);
        setLocationRelativeTo(null);
    }

    // Getters y Setters de UI
    public String getNumCuentaBusqueda() { return txtNumCuentaBusqueda.getText(); }
    public void setTitular(String val) { lblTitularVal.setText(val); }
    public void setSaldo(String val) { lblSaldoVal.setText(val); }
    public void setEstado(String val) { lblEstadoVal.setText(val); }
    
    public String getMontoDeposito() { return txtMontoDeposito.getText(); }
    public String getMontoRetiro() { return txtMontoRetiro.getText(); }
    public String getMontoTransferencia() { return txtMontoTransferencia.getText(); }
    public String getCuentaDestino() { return txtCuentaDestino.getText(); }
    
    public JButton getBtnBuscarCuenta() { return btnBuscarCuenta; }
    public JButton getBtnProcesarDeposito() { return btnProcesarDeposito; }
    public JButton getBtnProcesarRetiro() { return btnProcesarRetiro; }
    public JButton getBtnProcesarTransferencia() { return btnProcesarTransferencia; }
    
    public DefaultTableModel getModelMovimientos() { return modelMovimientos; }
    
    public void limpiarCamposTransaccion() {
        txtMontoDeposito.setText("");
        txtMontoRetiro.setText("");
        txtMontoTransferencia.setText("");
        txtCuentaDestino.setText("");
    }

    public void mostrarMensaje(String mensaje, String titulo, int tipo) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, tipo);
    }
}
