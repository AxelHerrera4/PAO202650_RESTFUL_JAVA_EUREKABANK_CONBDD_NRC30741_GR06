package com.monster.cli_eurekabank_con_java_restful.controlador;

import com.monster.cli_eurekabank_con_java_restful.modelo.ClienteRestfulModel;
import com.monster.cli_eurekabank_con_java_restful.modelo.CuentaClienteDTO;
import com.monster.cli_eurekabank_con_java_restful.modelo.Movimiento;
import com.monster.cli_eurekabank_con_java_restful.vista.ConsoleView;

import java.util.List;

/**
 * Controlador del CLI RESTful. Conecta el Modelo (ClienteRestfulModel) y la Vista (ConsoleView),
 * orquestando el flujo del programa, las transacciones y la gestión de excepciones de red.
 */
public class ConsoleController {

    private final ClienteRestfulModel model;
    private final ConsoleView view;

    public ConsoleController(ClienteRestfulModel model, ConsoleView view) {
        this.model = model;
        this.view = view;
    }

    /**
     * Inicia la ejecución de la herramienta interactiva de diagnóstico.
     */
    public void iniciar() {
        view.imprimirBanner();
        inicializarConexionREST();
        faseAutenticacion();
        ejecutarMenuPrincipal();
    }

    /**
     * Asegura la conexión inicial con el servidor RESTful de Eureka Bank.
     */
    private void inicializarConexionREST() {
        boolean conectado = false;
        while (!conectado) {
            try {
                view.imprimirAdvertencia("Estableciendo conexion con el servidor RESTful de Eureka Bank...");
                model.conectar();
                conectado = true;
                view.imprimirExito("* Conexion establecida con exito.\n");
            } catch (Exception e) {
                view.imprimirError("\nError critico: No se pudo conectar con el servidor RESTful.");
                view.imprimirInfo("El servidor Payara/Glassfish podria estar apagado o fuera de linea.");
                String respuesta = view.leerTexto("Desea reintentar la conexion? (S/N): ");
                if (!respuesta.equalsIgnoreCase("S")) {
                    view.imprimirInfo("Terminando el programa de soporte de forma limpia. Adios!");
                    System.exit(0);
                }
                view.imprimirSaltoLinea();
            }
        }
    }

    /**
     * Fase obligatoria de autenticación restringida para Superadmins.
     */
    private void faseAutenticacion() {
        boolean autenticado = false;
        view.imprimirHeaderAutenticacion();

        while (!autenticado) {
            String usuario = view.leerTexto("Usuario: ");
            String clave = view.leerTexto("Contrasena: ");

            if (usuario.isEmpty() || clave.isEmpty()) {
                view.imprimirError("El usuario y contrasena no pueden estar vacios. Reintente.\n");
                continue;
            }

            try {
                String respuesta = model.autenticar(usuario, clave);

                if (respuesta != null && respuesta.startsWith("SUCCESS")) {
                    if (respuesta.contains("Superadmin")) {
                        view.imprimirExito("\n" + respuesta);
                        view.imprimirExito("Acceso Concedido. Bienvenido al panel de administracion de TI.\n");
                        autenticado = true;
                    } else {
                        view.imprimirError("\nAcceso Denegado: Su rol no cuenta con privilegios de Soporte de TI.");
                        view.imprimirInfo("Este CLI esta restringido para administradores de sistemas (Superadmin).\n");
                    }
                } else {
                    String cleanRes = (respuesta != null) ? respuesta.replace("ERROR: ", "") : "Respuesta nula del servidor";
                    view.imprimirError("\nAcceso Denegado: " + cleanRes + "\n");
                }
            } catch (Exception e) {
                view.imprimirError("\nError critico: No se pudo conectar con el servidor RESTful al autenticar.");
                view.imprimirInfo("Por favor, asegurese de que el servidor Payara/Glassfish esta activo.");
                String respuesta = view.leerTexto("Desea reintentar la autenticacion? (S/N): ");
                if (!respuesta.equalsIgnoreCase("S")) {
                    view.imprimirInfo("Terminando el programa de forma limpia.");
                    System.exit(0);
                }
                view.imprimirSaltoLinea();
            }
        }
    }

    /**
     * Bucle del menú interactivo de soporte.
     */
    private void ejecutarMenuPrincipal() {
        while (true) {
            view.imprimirMenuPrincipal();
            String opcion = view.leerTexto("Seleccione una opcion (1-5): ");
            view.imprimirSaltoLinea();

            switch (opcion) {
                case "1":
                    consultarEstadoCuenta();
                    break;
                case "2":
                    listarCuentasSucursal();
                    break;
                case "3":
                    simularTransaccionPrueba();
                    break;
                case "4":
                    consultarHistorialTransacciones();
                    break;
                case "5":
                    view.imprimirExito("Cierre de sesion limpio. Gracias por usar la herramienta de diagnostico Eureka Bank!");
                    System.exit(0);
                    break;
                default:
                    view.imprimirError("Opcion invalida. Por favor, seleccione un numero entre 1 y 5.\n");
            }
        }
    }

    /**
     * Opción 1: Consultar Estado de Cuenta (Health Check)
     */
    private void consultarEstadoCuenta() {
        view.imprimirInfo("=== [1] CONSULTAR ESTADO DE CUENTA (HEALTH CHECK) ===");
        String numCuenta = view.leerTexto("Ingrese el numero de cuenta: ");

        if (numCuenta.isEmpty()) {
            view.imprimirError("El numero de cuenta no puede estar vacio.\n");
            return;
        }

        try {
            view.imprimirInfo("Buscando en la red de sucursales...");
            CuentaClienteDTO cuenta = model.buscarCuentaEnRed(numCuenta);

            if (cuenta != null) {
                String estadoStr = view.mapearEstadoDisponibilidad(cuenta.getDisponibilidad());
                view.imprimirDiagnosticoCuenta(cuenta, estadoStr);
            } else {
                view.imprimirError("Cuenta no encontrada en ninguna sucursal activa del banco.\n");
            }
        } catch (Exception e) {
            view.imprimirErrorConexion();
        }
    }

    /**
     * Opción 2: Listar Cuentas de Sucursal (Raw Data)
     */
    private void listarCuentasSucursal() {
        view.imprimirInfo("=== [2] LISTAR CUENTAS DE SUCURSAL (RAW DATA) ===");
        String sucursalStr = view.leerTexto("Ingrese el ID de la sucursal: ");

        if (sucursalStr.isEmpty()) {
            view.imprimirError("El ID de sucursal no puede estar vacio.\n");
            return;
        }

        int sucursalId;
        try {
            sucursalId = Integer.parseInt(sucursalStr);
        } catch (NumberFormatException e) {
            view.imprimirError("El ID de la sucursal debe ser un valor numerico entero.\n");
            return;
        }

        try {
            view.imprimirInfo("Solicitando radiografia operativa de la sucursal " + sucursalId + "...");
            List<CuentaClienteDTO> cuentas = model.listarCuentasPorSucursal(sucursalId);

            if (cuentas != null && !cuentas.isEmpty()) {
                view.imprimirInfo("\nCuentas encontradas: " + cuentas.size());
                view.imprimirTablaCuentas(cuentas);
            } else {
                view.imprimirAdvertencia("No se encontraron cuentas o la sucursal no existe en el sistema.\n");
            }
        } catch (Exception e) {
            view.imprimirErrorConexion();
        }
    }

    /**
     * Opción 3: Simular Transaccion de Prueba (DB Health Check)
     */
    private void simularTransaccionPrueba() {
        view.imprimirInfo("=== [3] SIMULAR TRANSACCION DE PRUEBA (DB HEALTH CHECK) ===");
        String numCuenta = view.leerTexto("Ingrese el numero de cuenta destino: ");

        if (numCuenta.isEmpty()) {
            view.imprimirError("El numero de cuenta no puede estar vacio.\n");
            return;
        }

        view.imprimirInfo("Seleccione el tipo de transaccion:");
        view.imprimirInfo("  1. Deposito (Abono)");
        view.imprimirInfo("  2. Retiro (Cargo)");
        String tipoOp = view.leerTexto("Opcion (1 o 2): ");

        if (!tipoOp.equals("1") && !tipoOp.equals("2")) {
            view.imprimirError("Seleccion de operacion invalida. Abortando transaccion.\n");
            return;
        }

        String montoStr = view.leerTexto("Ingrese el monto de prueba: S/ ");

        if (montoStr.isEmpty()) {
            view.imprimirError("El monto no puede estar vacio.\n");
            return;
        }

        double monto;
        try {
            monto = Double.parseDouble(montoStr);
            if (monto <= 0) {
                view.imprimirError("El monto debe ser estrictamente superior a cero.\n");
                return;
            }
        } catch (NumberFormatException e) {
            view.imprimirError("El monto ingresado es invalido.\n");
            return;
        }

        // ID de empleado de prueba (Superadmin Monster / ID 1)
        int empleadoIdPrueba = 1;

        try {
            view.imprimirInfo("Enviando orden de transaccion al servidor RESTful...");
            String resultado;

            if (tipoOp.equals("1")) {
                resultado = model.depositar(numCuenta, monto, empleadoIdPrueba);
            } else {
                resultado = model.retirar(numCuenta, monto, empleadoIdPrueba);
            }

            if (resultado != null) {
                if (resultado.startsWith("SUCCESS")) {
                    view.imprimirExitoTransaccion(resultado);
                } else {
                    view.imprimirErrorTransaccion(resultado);
                }
            } else {
                view.imprimirError("El servidor retorno una respuesta vacia.\n");
            }
        } catch (Exception e) {
            view.imprimirErrorConexion();
        }
    }

    /**
     * Opción 4: Consultar Historial de Transacciones (Audit Log)
     */
    private void consultarHistorialTransacciones() {
        view.imprimirInfo("=== [4] CONSULTAR HISTORIAL DE TRANSACCIONES (AUDIT LOG) ===");
        String numCuenta = view.leerTexto("Ingrese el numero de cuenta: ");

        if (numCuenta.isEmpty()) {
            view.imprimirError("El numero de cuenta no puede estar vacio.\n");
            return;
        }

        try {
            view.imprimirInfo("Solicitando historial de movimientos...");
            List<Movimiento> movimientos = model.consultarExtracto(numCuenta);

            if (movimientos != null && !movimientos.isEmpty()) {
                view.imprimirInfo("\nMovimientos registrados para la cuenta " + numCuenta + ": " + movimientos.size());
                view.imprimirTablaMovimientos(movimientos);
            } else {
                view.imprimirAdvertencia("No se encontraron transacciones registradas para esta cuenta.\n");
            }
        } catch (Exception e) {
            view.imprimirErrorConexion();
        }
    }
}
