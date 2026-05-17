package com.eurekabank.servicio;

import com.eurekabank.dao.CuentaDAOImpl;
import com.eurekabank.dao.ICuentaDAO;
import com.eurekabank.dao.IMovimientoDAO;
import com.eurekabank.dao.MovimientoDAOImpl;
import com.eurekabank.modelo.Cuenta;
import com.eurekabank.modelo.CuentaClienteDTO;
import com.eurekabank.modelo.Movimiento;
import com.eurekabank.util.ConexionDB;
import com.eurekabank.util.GestorConcurrencia;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Servicio de Transacciones Refactorizado (ERS RQF-004 / RQF-002).
 * Implementa transaccionalidad ACID manual y bloqueo de registros en BD.
 */
public class TransaccionService {

    private final ICuentaDAO cuentaDAO;
    private final IMovimientoDAO movimientoDAO;
    private final GestorConcurrencia gestorConcurrencia;

    public TransaccionService() {
        this.cuentaDAO = new CuentaDAOImpl();
        this.movimientoDAO = new MovimientoDAOImpl();
        this.gestorConcurrencia = GestorConcurrencia.getInstancia();
    }

    public String realizarDeposito(String numeroCuenta, double monto, int idEmpleado) throws Exception {
        if (!gestorConcurrencia.bloquearCuenta(numeroCuenta)) {
            throw new Exception("CONCURRENCIA_ERROR: Cuenta ocupada.");
        }

        Connection con = null;
        try {
            con = ConexionDB.getConexion();
            con.setAutoCommit(false);

            Cuenta cuenta = cuentaDAO.obtenerCuentaForUpdate(con, numeroCuenta);
            if (cuenta == null) throw new Exception("Cuenta no existe.");

            System.out.println("🔒 [MASHUP] Cuenta " + numeroCuenta + " bloqueada. Simulando latencia bancaria de 5 segundos...");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ie) {
                System.err.println("Latencia interrumpida: " + ie.getMessage());
                Thread.currentThread().interrupt();
            }
            System.out.println("🔓 Candado liberado, finalizando transacción...");

            double nuevoSaldo = cuenta.getSaldo() + monto;
            cuentaDAO.actualizarSaldo(con, numeroCuenta, nuevoSaldo);

            String nroOp = "DEP-" + System.currentTimeMillis();
            movimientoDAO.registrarMovimiento(con, new Movimiento(0, nroOp, numeroCuenta, idEmpleado, "Deposito", monto, null));

            con.commit();
            return nroOp;
        } catch (Exception e) {
            if (con != null) con.rollback();
            throw e;
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException se) {
                    System.err.println("[JDBC] Error al liberar y cerrar conexión: " + se.getMessage());
                }
            }
            gestorConcurrencia.liberarCuenta(numeroCuenta);
        }
    }

    public String realizarRetiro(String numeroCuenta, double monto, int idEmpleado) throws Exception {
        if (!gestorConcurrencia.bloquearCuenta(numeroCuenta)) {
            throw new Exception("CONCURRENCIA_ERROR: Cuenta ocupada.");
        }

        Connection con = null;
        try {
            con = ConexionDB.getConexion();
            con.setAutoCommit(false);

            Cuenta cuenta = cuentaDAO.obtenerCuentaForUpdate(con, numeroCuenta);
            if (cuenta == null) throw new Exception("Cuenta no existe.");
            if (cuenta.getSaldo() < monto) throw new Exception("SALDO_INSUFICIENTE.");

            System.out.println("🔒 [MASHUP] Cuenta " + numeroCuenta + " bloqueada. Simulando latencia bancaria de 5 segundos...");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ie) {
                System.err.println("Latencia interrumpida: " + ie.getMessage());
                Thread.currentThread().interrupt();
            }
            System.out.println("🔓 Candado liberado, finalizando transacción...");

            double nuevoSaldo = cuenta.getSaldo() - monto;
            cuentaDAO.actualizarSaldo(con, numeroCuenta, nuevoSaldo);

            String nroOp = "RET-" + System.currentTimeMillis();
            movimientoDAO.registrarMovimiento(con, new Movimiento(0, nroOp, numeroCuenta, idEmpleado, "Retiro", monto, null));

            con.commit();
            return nroOp;
        } catch (Exception e) {
            if (con != null) con.rollback();
            throw e;
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException se) {
                    System.err.println("[JDBC] Error al liberar y cerrar conexión: " + se.getMessage());
                }
            }
            gestorConcurrencia.liberarCuenta(numeroCuenta);
        }
    }

    public String realizarTransferencia(String cuentaOrigen, String cuentaDestino, double monto, int idEmpleado) throws Exception {
        String first = cuentaOrigen.compareTo(cuentaDestino) < 0 ? cuentaOrigen : cuentaDestino;
        String second = first.equals(cuentaOrigen) ? cuentaDestino : cuentaOrigen;

        if (!gestorConcurrencia.bloquearCuenta(first)) throw new Exception("CONCURRENCIA_ERROR: " + first);
        try {
            if (!gestorConcurrencia.bloquearCuenta(second)) throw new Exception("CONCURRENCIA_ERROR: " + second);
            try {
                Connection con = null;
                try {
                    con = ConexionDB.getConexion();
                    con.setAutoCommit(false);

                    Cuenta cOrig = cuentaDAO.obtenerCuentaForUpdate(con, cuentaOrigen);
                    Cuenta cDest = cuentaDAO.obtenerCuentaForUpdate(con, cuentaDestino);

                    if (cOrig == null || cDest == null) throw new Exception("Una de las cuentas no existe.");
                    if (cOrig.getSaldo() < monto) throw new Exception("SALDO_INSUFICIENTE.");

                    System.out.println("🔒 [MASHUP] Cuenta " + cuentaOrigen + " bloqueada. Simulando latencia bancaria de 5 segundos...");
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ie) {
                        System.err.println("Latencia interrumpida: " + ie.getMessage());
                        Thread.currentThread().interrupt();
                    }
                    System.out.println("🔓 Candado liberado, finalizando transacción...");

                    cuentaDAO.actualizarSaldo(con, cuentaOrigen, cOrig.getSaldo() - monto);
                    cuentaDAO.actualizarSaldo(con, cuentaDestino, cDest.getSaldo() + monto);

                    // Registro de movimientos con IDs ÚNICOS para cumplir con la restricción de la BD
                    String nroBase = "TRA-" + System.currentTimeMillis();
                    movimientoDAO.registrarMovimiento(con, new Movimiento(0, nroBase + "-R", cuentaOrigen, idEmpleado, "Retiro", monto, null));
                    movimientoDAO.registrarMovimiento(con, new Movimiento(0, nroBase + "-D", cuentaDestino, idEmpleado, "Deposito", monto, null));

                    con.commit();
                    return nroBase;
                } catch (Exception e) {
                    if (con != null) con.rollback();
                    throw e;
                } finally {
                    if (con != null) {
                        try {
                            con.setAutoCommit(true);
                            con.close();
                        } catch (SQLException se) {
                            System.err.println("[JDBC] Error al liberar y cerrar conexión: " + se.getMessage());
                        }
                    }
                }
            } finally {
                gestorConcurrencia.liberarCuenta(second);
            }
        } finally {
            gestorConcurrencia.liberarCuenta(first);
        }
    }

    public List<CuentaClienteDTO> obtenerCuentasSucursal(int sucursalId) {
        return cuentaDAO.obtenerCuentasConClientes(sucursalId);
    }

    public List<Movimiento> obtenerExtracto(String numeroCuenta) {
        return movimientoDAO.listarExtracto(numeroCuenta);
    }

    public CuentaClienteDTO obtenerCuentaClientePorDni(String dni) {
        return cuentaDAO.obtenerCuentaPorDni(dni);
    }
}
