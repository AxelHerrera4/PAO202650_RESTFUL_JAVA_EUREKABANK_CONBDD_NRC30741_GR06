package com.monster.ws_eurekabank_java_restful_gr6.resources;

import com.eurekabank.controlador.OperacionesController;
import com.eurekabank.modelo.CuentaClienteDTO;
import com.eurekabank.modelo.Movimiento;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

/**
 * Recurso RESTful de Eureka Bank.
 * Refactorizado bajo estándares Senior para utilizar cuerpos JSON en peticiones POST.
 * @author Antigravity
 */
@Path("eurekabank")
public class EurekaBankResource {

    private final OperacionesController controlador;

    public EurekaBankResource() {
        this.controlador = new OperacionesController();
    }

    // ==========================================
    // DTOs de Petición (Request Bodies)
    // ==========================================

    public static class LoginRequest {
        private String usuario;
        private String clave;

        public LoginRequest() {}
        public LoginRequest(String usuario, String clave) {
            this.usuario = usuario;
            this.clave = clave;
        }

        public String getUsuario() { return usuario; }
        public void setUsuario(String usuario) { this.usuario = usuario; }
        public String getClave() { return clave; }
        public void setClave(String clave) { this.clave = clave; }
    }

    public static class TransaccionRequest {
        private String cuentaId;
        private double monto;
        private int empleadoId;

        public TransaccionRequest() {}
        public TransaccionRequest(String cuentaId, double monto, int empleadoId) {
            this.cuentaId = cuentaId;
            this.monto = monto;
            this.empleadoId = empleadoId;
        }

        public String getCuentaId() { return cuentaId; }
        public void setCuentaId(String cuentaId) { this.cuentaId = cuentaId; }
        public double getMonto() { return monto; }
        public void setMonto(double monto) { this.monto = monto; }
        public int getEmpleadoId() { return empleadoId; }
        public void setEmpleadoId(int empleadoId) { this.empleadoId = empleadoId; }
    }

    public static class TransferenciaRequest {
        private String cuentaOrigenId;
        private String cuentaDestinoId;
        private double monto;
        private int empleadoId;

        public TransferenciaRequest() {}
        public TransferenciaRequest(String cuentaOrigenId, String cuentaDestinoId, double monto, int empleadoId) {
            this.cuentaOrigenId = cuentaOrigenId;
            this.cuentaDestinoId = cuentaDestinoId;
            this.monto = monto;
            this.empleadoId = empleadoId;
        }

        public String getCuentaOrigenId() { return cuentaOrigenId; }
        public void setCuentaOrigenId(String cuentaOrigenId) { this.cuentaOrigenId = cuentaOrigenId; }
        public String getCuentaDestinoId() { return cuentaDestinoId; }
        public void setCuentaDestinoId(String cuentaDestinoId) { this.cuentaDestinoId = cuentaDestinoId; }
        public double getMonto() { return monto; }
        public void setMonto(double monto) { this.monto = monto; }
        public int getEmpleadoId() { return empleadoId; }
        public void setEmpleadoId(int empleadoId) { this.empleadoId = empleadoId; }
    }

    // ==========================================
    // Endpoints RESTful
    // ==========================================

    /**
     * Autenticación de usuario (Empleado o Cliente).
     * POST /resources/eurekabank/autenticar
     */
    @POST
    @Path("autenticar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN + ";charset=UTF-8")
    public Response autenticarUsuario(LoginRequest req) {
        if (req == null || req.getUsuario() == null || req.getClave() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("ERROR: El cuerpo JSON debe contener 'usuario' y 'clave'.")
                    .build();
        }
        String resultado = controlador.loginEmpleado(req.getUsuario(), req.getClave());
        if (resultado.startsWith("SUCCESS")) {
            return Response.ok(resultado).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity(resultado).build();
        }
    }

    /**
     * Procesa un depósito en una cuenta bancaria.
     * POST /resources/eurekabank/depositar
     */
    @POST
    @Path("depositar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN + ";charset=UTF-8")
    public Response depositar(TransaccionRequest req) {
        if (req == null || req.getCuentaId() == null || req.getMonto() <= 0 || req.getEmpleadoId() <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("ERROR: Parámetros del depósito inválidos en el JSON.")
                    .build();
        }
        String resultado = controlador.procesarDeposito(req.getCuentaId(), req.getMonto(), req.getEmpleadoId());
        if (resultado.startsWith("SUCCESS")) {
            return Response.ok(resultado).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity(resultado).build();
        }
    }

    /**
     * Procesa un retiro de una cuenta bancaria.
     * POST /resources/eurekabank/retirar
     */
    @POST
    @Path("retirar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN + ";charset=UTF-8")
    public Response retirar(TransaccionRequest req) {
        if (req == null || req.getCuentaId() == null || req.getMonto() <= 0 || req.getEmpleadoId() <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("ERROR: Parámetros del retiro inválidos en el JSON.")
                    .build();
        }
        String resultado = controlador.procesarRetiro(req.getCuentaId(), req.getMonto(), req.getEmpleadoId());
        if (resultado.startsWith("SUCCESS")) {
            return Response.ok(resultado).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity(resultado).build();
        }
    }

    /**
     * Procesa una transferencia atómica entre cuentas (RF-04).
     * POST /resources/eurekabank/transferir
     */
    @POST
    @Path("transferir")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN + ";charset=UTF-8")
    public Response transferir(TransferenciaRequest req) {
        if (req == null || req.getCuentaOrigenId() == null || req.getCuentaDestinoId() == null || req.getMonto() <= 0 || req.getEmpleadoId() <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("ERROR: Parámetros de transferencia inválidos en el JSON.")
                    .build();
        }
        String resultado = controlador.procesarTransferencia(req.getCuentaOrigenId(), req.getCuentaDestinoId(), req.getMonto(), req.getEmpleadoId());
        if (resultado.startsWith("SUCCESS")) {
            return Response.ok(resultado).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity(resultado).build();
        }
    }

    /**
     * Lista las cuentas operativas por Sucursal (RQF-002).
     * GET /resources/eurekabank/listarCuentasPorSucursal
     */
    @GET
    @Path("listarCuentasPorSucursal")
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    public Response listarCuentasPorSucursal(
            @QueryParam("sucursalId") int sucursalId) {
        List<CuentaClienteDTO> cuentas = controlador.obtenerCuentasSucursal(sucursalId);
        return Response.ok(cuentas).build();
    }

    /**
     * Consulta el extracto de movimientos de una cuenta.
     * GET /resources/eurekabank/consultarExtracto
     */
    @GET
    @Path("consultarExtracto")
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    public Response consultarExtracto(
            @QueryParam("cuentaId") String cuentaId) {
        List<Movimiento> extracto = controlador.obtenerHistorial(cuentaId);
        return Response.ok(extracto).build();
    }

    /**
     * Consulta la cuenta asociada a un cliente por su DNI.
     * GET /resources/eurekabank/consultarCuentasPorCliente
     */
    @GET
    @Path("consultarCuentasPorCliente")
    @Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
    public Response consultarCuentasPorCliente(
            @QueryParam("dni") String dni) {
        CuentaClienteDTO cuenta = controlador.obtenerCuentaPorDni(dni);
        if (cuenta != null) {
            return Response.ok(cuenta).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"DNI no registrado o cuenta no encontrada.\"}")
                    .build();
        }
    }
}
