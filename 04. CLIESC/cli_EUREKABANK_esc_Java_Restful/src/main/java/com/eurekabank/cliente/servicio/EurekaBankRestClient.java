package com.eurekabank.cliente.servicio;

import com.eurekabank.cliente.modelo.CuentaClienteDTO;
import com.eurekabank.cliente.modelo.Movimiento;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Cliente RESTful de Eureka Bank.
 * Responsable de la comunicación HTTP con ws_EUREKABANK_Java_Restful_GR6.
 */
public class EurekaBankRestClient {

    private final String BASE_URL = "http://localhost:8080/ws_EUREKABANK_Java_Restful_GR6/resources/eurekabank";
    private final HttpClient client;
    private final Gson gson;

    public EurekaBankRestClient() {
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.gson = new Gson();
    }

    /**
     * Envía una petición POST con cuerpo JSON y retorna el texto plano de respuesta.
     */
    private String postJson(String endpoint, Object requestBody) throws Exception {
        String jsonPayload = gson.toJson(requestBody);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json")
                .header("Accept", "text/plain")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        
        // Retornar el cuerpo directamente. Si es un código de error, el controlador manejará el contenido del string (como "ERROR: ...")
        return response.body();
    }

    /**
     * Envía una petición GET y retorna la respuesta en texto plano.
     */
    private String getJson(String endpointAndParams) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpointAndParams))
                .timeout(Duration.ofSeconds(10))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        if (response.statusCode() != 200) {
            throw new RuntimeException("Error en servidor (" + response.statusCode() + "): " + response.body());
        }
        return response.body();
    }

    /**
     * Autenticar usuario.
     * POST /autenticar
     */
    public String autenticarUsuario(String usuario, String clave) {
        try {
            Map<String, String> body = new HashMap<>();
            body.put("usuario", usuario);
            body.put("clave", clave);
            return postJson("/autenticar", body);
        } catch (Exception e) {
            return "ERROR: Conexión fallida con el servidor REST. " + e.getMessage();
        }
    }

    /**
     * Procesar un depósito.
     * POST /depositar
     */
    public String depositar(String cuentaId, double monto, int empleadoId) {
        try {
            Map<String, Object> body = new HashMap<>();
            body.put("cuentaId", cuentaId);
            body.put("monto", monto);
            body.put("empleadoId", empleadoId);
            return postJson("/depositar", body);
        } catch (Exception e) {
            return "ERROR: Conexión fallida. " + e.getMessage();
        }
    }

    /**
     * Procesar un retiro.
     * POST /retirar
     */
    public String retirar(String cuentaId, double monto, int empleadoId) {
        try {
            Map<String, Object> body = new HashMap<>();
            body.put("cuentaId", cuentaId);
            body.put("monto", monto);
            body.put("empleadoId", empleadoId);
            return postJson("/retirar", body);
        } catch (Exception e) {
            return "ERROR: Conexión fallida. " + e.getMessage();
        }
    }

    /**
     * Procesar una transferencia.
     * POST /transferir
     */
    public String transferir(String cuentaOrigen, String cuentaDestino, double monto, int empleadoId) {
        try {
            Map<String, Object> body = new HashMap<>();
            body.put("cuentaOrigenId", cuentaOrigen);
            body.put("cuentaDestinoId", cuentaDestino);
            body.put("monto", monto);
            body.put("empleadoId", empleadoId);
            return postJson("/transferir", body);
        } catch (Exception e) {
            return "ERROR: Conexión fallida. " + e.getMessage();
        }
    }

    /**
     * Listar cuentas operativas por sucursal.
     * GET /listarCuentasPorSucursal
     */
    public List<CuentaClienteDTO> listarCuentasPorSucursal(int sucursalId) {
        try {
            String responseBody = getJson("/listarCuentasPorSucursal?sucursalId=" + sucursalId);
            Type listType = new TypeToken<ArrayList<CuentaClienteDTO>>(){}.getType();
            return gson.fromJson(responseBody, listType);
        } catch (Exception e) {
            System.err.println("Error al listar cuentas por sucursal REST: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Consultar extracto de movimientos de una cuenta.
     * GET /consultarExtracto
     */
    public List<Movimiento> consultarExtracto(String cuentaId) {
        try {
            String responseBody = getJson("/consultarExtracto?cuentaId=" + cuentaId);
            Type listType = new TypeToken<ArrayList<Movimiento>>(){}.getType();
            return gson.fromJson(responseBody, listType);
        } catch (Exception e) {
            System.err.println("Error al consultar extracto REST: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
