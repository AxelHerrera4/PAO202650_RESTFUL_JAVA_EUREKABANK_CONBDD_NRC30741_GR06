package com.monster.cli_eurekabank_con_java_restful.vista;

import com.monster.cli_eurekabank_con_java_restful.modelo.CuentaClienteDTO;
import com.monster.cli_eurekabank_con_java_restful.modelo.Movimiento;

import java.util.List;
import java.util.Scanner;

/**
 * Vista de Consola. Centraliza todos los aspectos visuales del CLI (colores ANSI,
 * formato ASCII, banners y tablas) y la captura interactiva del teclado sin caracteres especiales.
 */
public class ConsoleView {

    // Códigos ANSI para mejorar los aspectos estéticos del CLI
    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String CYAN = "\u001B[36m";
    private static final String BLUE = "\u001B[34m";
    private static final String PURPLE = "\u001B[35m";
    private static final String BOLD = "\u001B[1m";

    private final Scanner scanner = new Scanner(System.in);

    /**
     * Muestra el banner corporativo en terminal.
     */
    public void imprimirBanner() {
        System.out.println(BLUE + "=========================================================================" + RESET);
        System.out.println(CYAN + BOLD + "  ███████╗██╗   ██╗██████╗ ███████╗██╗  ██╗ █████╗ ██████╗  █████╗ ███╗   ██╗" + RESET);
        System.out.println(CYAN + BOLD + "  ██╔════╝██║   ██║██╔══██╗██╔════╝██║ ██╔╝██╔══██╗██╔══██╗██╔══██║████╗  ██║" + RESET);
        System.out.println(CYAN + BOLD + "  █████╗  ██║   ██║██████╔╝█████╗  █████╔╝ ███████║██████╔╝███████║██╔██╗ ██║" + RESET);
        System.out.println(CYAN + BOLD + "  ██╔══╝  ██║   ██║██╔══██╗██╔══╝  ██╔═██╗ ██╔══██║██╔══██╗██╔══██║██║╚██╗██║" + RESET);
        System.out.println(CYAN + BOLD + "  ███████╗╚██████╔╝██║  ██║███████╗██║  ██╗██║  ██║██████╔╝██║  ██║██║ ╚████║" + RESET);
        System.out.println(CYAN + BOLD + "  ╚══════╝ ╚═════╝ ╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═══╝" + RESET);
        System.out.println(PURPLE + BOLD + "       HERRAMIENTA DE DIAGNOSTICO Y SOPORTE DE TI RESTFUL (CLI)" + RESET);
        System.out.println(BLUE + "=========================================================================" + RESET);
    }

    /**
     * Muestra el encabezado de autenticación.
     */
    public void imprimirHeaderAutenticacion() {
        System.out.println(BLUE + "╔═══════════════════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(BLUE + "║                     FASE DE AUTENTICACION REQUERIDA                   ║" + RESET);
        System.out.println(BLUE + "╚═══════════════════════════════════════════════════════════════════════╝" + RESET);
    }

    /**
     * Pinta el menú principal en la consola.
     */
    public void imprimirMenuPrincipal() {
        System.out.println(BLUE + "╔═══════════════════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(BLUE + "║                     MENU PRINCIPAL DE DIAGNOSTICO                     ║" + RESET);
        System.out.println(BLUE + "╚═══════════════════════════════════════════════════════════════════════╝" + RESET);
        System.out.println(CYAN + " [1] " + RESET + BOLD + "Consultar Estado de Cuenta (Health Check)" + RESET);
        System.out.println(CYAN + " [2] " + RESET + BOLD + "Listar Cuentas de Sucursal (Raw Data)" + RESET);
        System.out.println(CYAN + " [3] " + RESET + BOLD + "Simular Transaccion de Prueba" + RESET);
        System.out.println(CYAN + " [4] " + RESET + BOLD + "Consultar Historial de Transacciones (Audit Log)" + RESET);
        System.out.println(RED + " [5] " + RESET + BOLD + "Salir" + RESET);
        System.out.println(BLUE + "─────────────────────────────────────────────────────────────────────────" + RESET);
    }

    /**
     * Solicita una entrada de texto al usuario mostrando un indicador.
     */
    public String leerTexto(String prompt) {
        System.out.print(BOLD + prompt + RESET);
        return scanner.nextLine().trim();
    }

    /**
     * Muestra un mensaje informativo estándar.
     */
    public void imprimirInfo(String msg) {
        System.out.println(msg);
    }

    /**
     * Muestra una advertencia o mensaje de espera de forma clara.
     */
    public void imprimirAdvertencia(String msg) {
        System.out.println(YELLOW + msg + RESET);
    }

    /**
     * Muestra un bloque de éxito.
     */
    public void imprimirExito(String msg) {
        System.out.println(GREEN + msg + RESET);
    }

    /**
     * Muestra un bloque de error destacado.
     */
    public void imprimirError(String msg) {
        System.out.println(RED + BOLD + msg + RESET);
    }

    /**
     * Muestra un salto de línea.
     */
    public void imprimirSaltoLinea() {
        System.out.println();
    }

    /**
     * Muestra el diagnóstico de una cuenta en un recuadro premium.
     */
    public void imprimirDiagnosticoCuenta(CuentaClienteDTO cuenta, String estadoStr) {
        System.out.println("\n" + GREEN + "┌──────────────────────────────────────────────────────────┐" + RESET);
        System.out.println(GREEN + "│             DIAGNOSTICO DEL ESTADO DE LA CUENTA          │" + RESET);
        System.out.println(GREEN + "├──────────────────────────────────────────────────────────┤" + RESET);
        System.out.printf(GREEN + "│ " + RESET + BOLD + "%-18s" + RESET + ": %-35s " + GREEN + "│\n" + RESET, "Numero de Cuenta", cuenta.getNumeroCuenta());
        System.out.printf(GREEN + "│ " + RESET + BOLD + "%-18s" + RESET + ": %-35s " + GREEN + "│\n" + RESET, "Cliente/Titular", cuenta.getNombreCliente() + " " + cuenta.getApellidoCliente());
        System.out.printf(GREEN + "│ " + RESET + BOLD + "%-18s" + RESET + ": S/ %-32.2f " + GREEN + "│\n" + RESET, "Saldo Registrado", cuenta.getSaldo());
        System.out.printf(GREEN + "│ " + RESET + BOLD + "%-18s" + RESET + ": %-35s " + GREEN + "│\n" + RESET, "Estado Operativo", estadoStr);
        System.out.println(GREEN + "└──────────────────────────────────────────────────────────┘" + RESET + "\n");
    }

    /**
     * Muestra un cuadro de éxito de transacción.
     */
    public void imprimirExitoTransaccion(String resultado) {
        System.out.println("\n" + GREEN + "╔═══════════════════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(GREEN + "║                    TRANSACCION DE PRUEBA EXITOSA                      ║" + RESET);
        System.out.println(GREEN + "╚═══════════════════════════════════════════════════════════════════════╝" + RESET);
        System.out.println(GREEN + resultado + RESET);
        System.out.println(GREEN + "La base de datos y la conexion transaccional responden correctamente." + RESET + "\n");
    }

    /**
     * Muestra un cuadro de error de transacción.
     */
    public void imprimirErrorTransaccion(String resultado) {
        System.out.println("\n" + RED + "╔═══════════════════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(RED + "║                  ERROR EN LA TRANSACCION DE PRUEBA                   ║" + RESET);
        System.out.println(RED + "╚═══════════════════════════════════════════════════════════════════════╝" + RESET);
        System.out.println(RED + resultado + RESET + "\n");
    }

    /**
     * Muestra una tabla ASCII premium con las cuentas de una sucursal.
     */
    public void imprimirTablaCuentas(List<CuentaClienteDTO> cuentas) {
        String separador = "+--------------+---------------------------+--------------+------------+";
        System.out.println(BLUE + separador + RESET);
        System.out.printf(BLUE + "| " + RESET + BOLD + "%-12s" + RESET + BLUE + " | " + RESET + BOLD + "%-25s" + RESET + BLUE + " | " + RESET + BOLD + "%-12s" + RESET + BLUE + " | " + RESET + BOLD + "%-10s" + RESET + BLUE + " |\n" + RESET,
                "Nro Cuenta", "Cliente / Titular", "Saldo (S/)", "Estado");
        System.out.println(BLUE + separador + RESET);

        for (CuentaClienteDTO c : cuentas) {
            String estadoClean = "VERDE".equalsIgnoreCase(c.getDisponibilidad()) ? "Libre" : "Bloqueada";
            String colorEstado = "VERDE".equalsIgnoreCase(c.getDisponibilidad()) ? GREEN : RED;

            System.out.printf(BLUE + "| " + RESET + "%-12s" + BLUE + " | " + RESET + "%-25s" + BLUE + " | " + RESET + "%12.2f" + BLUE + " | " + colorEstado + "%-10s" + RESET + BLUE + " |\n" + RESET,
                    c.getNumeroCuenta(),
                    c.getNombreCliente() + " " + c.getApellidoCliente(),
                    c.getSaldo(),
                    estadoClean);
        }
        System.out.println(BLUE + separador + RESET + "\n");
    }

    /**
     * Muestra una tabla ASCII de movimientos (extracto/historial).
     */
    public void imprimirTablaMovimientos(List<Movimiento> movimientos) {
        String separador = "+--------------------+----------------------+------------+--------------+----------------------+";
        System.out.println(BLUE + separador + RESET);
        System.out.printf(BLUE + "| " + RESET + BOLD + "%-18s" + RESET + BLUE + " | " + RESET + BOLD + "%-20s" + RESET + BLUE + " | " + RESET + BOLD + "%-10s" + RESET + BLUE + " | " + RESET + BOLD + "%-12s" + RESET + BLUE + " | " + RESET + BOLD + "%-20s" + RESET + BLUE + " |\n" + RESET,
                "Nro Operacion", "Fecha y Hora", "Tipo", "Monto (S/)", "Emp. Autorizador");
        System.out.println(BLUE + separador + RESET);

        for (Movimiento m : movimientos) {
            String colorMonto = "Deposito".equalsIgnoreCase(m.getTipo()) ? GREEN : RED;
            String fecha = m.getFechaHora() != null ? m.getFechaHora() : "N/A";
            String emp = m.getNombreEmpleado() != null ? m.getNombreEmpleado() : "Desconocido";

            System.out.printf(BLUE + "| " + RESET + "%-18s" + BLUE + " | " + RESET + "%-20s" + BLUE + " | " + RESET + "%-10s" + BLUE + " | " + colorMonto + "%12.2f" + RESET + BLUE + " | " + RESET + "%-20s" + BLUE + " |\n" + RESET,
                    m.getNumeroOperacion(),
                    fecha,
                    m.getTipo(),
                    m.getMonto(),
                    emp);
        }
        System.out.println(BLUE + separador + RESET + "\n");
    }

    /**
     * Retorna la versión formateada del estado de semáforo de disponibilidad.
     */
    public String mapearEstadoDisponibilidad(String disponibilidad) {
        if ("VERDE".equalsIgnoreCase(disponibilidad)) {
            return GREEN + BOLD + "Libre" + RESET + " (Semaforo Verde)";
        } else if ("ROJO".equalsIgnoreCase(disponibilidad)) {
            return RED + BOLD + "Bloqueada" + RESET + " (Semaforo Rojo / Ocupada)";
        }
        return YELLOW + BOLD + "Desconocido" + RESET;
    }

    /**
     * Muestra un mensaje amigable de error de red sin exponer trazas.
     */
    public void imprimirErrorConexion() {
        System.out.println(RED + BOLD + "\nError critico: No se pudo conectar con el servidor RESTful." + RESET);
        System.out.println("Por favor, asegurese de que el servidor de aplicaciones Payara/Glassfish esta encendido.");
        System.out.println("Si el servidor esta activo, compruebe que no existan bloqueos de red o firewall.\n");
    }
}
