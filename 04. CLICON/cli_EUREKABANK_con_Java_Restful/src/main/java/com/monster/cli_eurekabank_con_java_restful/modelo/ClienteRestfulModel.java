package com.monster.cli_eurekabank_con_java_restful.modelo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

/**
 * Modelo que encapsula toda la lógica de acceso a datos y llamadas al
 * Web Service RESTful de Eureka Bank.
 */
public class ClienteRestfulModel {

    private final HttpClient client;
    private final Gson gson;
    private static final String BASE_URL = "http://localhost:8080/ws_EUREKABANK_Java_Restful_GR6/resources/eurekabank";
    private boolean conectado = false;

    public ClienteRestfulModel() {
        this.client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        this.gson = new Gson();
    }

    /**
     * Establece la conexión inicial y hace ping con el Web Service.
     * @throws IOException Si ocurre un error de red.
     * @throws InterruptedException Si se interrumpe la operación.
     */
    public void conectar() throws IOException, InterruptedException {
        String url = BASE_URL + "/listarCuentasPorSucursal?sucursalId=1";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .timeout(Duration.ofSeconds(3))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            conectado = true;
        } else {
            throw new IOException("El servidor respondió con código de error: " + response.statusCode());
        }
    }

    /**
     * Verifica si el modelo está actualmente conectado.
     */
    public boolean estaConectado() {
        return conectado;
    }

    /**
     * Invoca el método REST para autenticar un usuario en el backend.
     */
    public String autenticar(String usuario, String clave) throws IOException, InterruptedException {
        if (!conectado) {
            conectar();
        }
        String url = BASE_URL + "/autenticar";
        LoginRequest req = new LoginRequest(usuario, clave);
        String jsonPayload = gson.toJson(req);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Accept", "text/plain")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // JAX-RS puede retornar código 401 si no está autorizado, pero retorna el mensaje de error en el cuerpo
        return response.body();
    }

    /**
     * Lista todas las cuentas asociadas a una sucursal específica.
     */
    public List<CuentaClienteDTO> listarCuentasPorSucursal(int sucursalId) throws IOException, InterruptedException {
        if (!conectado) {
            conectar();
        }
        String url = BASE_URL + "/listarCuentasPorSucursal?sucursalId=" + sucursalId;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            Type listType = new TypeToken<List<CuentaClienteDTO>>() {}.getType();
            return gson.fromJson(response.body(), listType);
        } else {
            throw new IOException("Error al listar cuentas: Código de estado " + response.statusCode());
        }
    }

    /**
     * Realiza un depósito en una cuenta.
     */
    public String depositar(String numCuenta, double monto, int empleadoId) throws IOException, InterruptedException {
        if (!conectado) {
            conectar();
        }
        String url = BASE_URL + "/depositar";
        TransaccionRequest req = new TransaccionRequest(numCuenta, monto, empleadoId);
        String jsonPayload = gson.toJson(req);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Accept", "text/plain")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    /**
     * Realiza un retiro de una cuenta.
     */
    public String retirar(String numCuenta, double monto, int empleadoId) throws IOException, InterruptedException {
        if (!conectado) {
            conectar();
        }
        String url = BASE_URL + "/retirar";
        TransaccionRequest req = new TransaccionRequest(numCuenta, monto, empleadoId);
        String jsonPayload = gson.toJson(req);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Accept", "text/plain")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    /**
     * Consulta el historial de movimientos de una cuenta.
     */
    public List<Movimiento> consultarExtracto(String numCuenta) throws IOException, InterruptedException {
        if (!conectado) {
            conectar();
        }
        String url = BASE_URL + "/consultarExtracto?cuentaId=" + numCuenta;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            Type listType = new TypeToken<List<Movimiento>>() {}.getType();
            return gson.fromJson(response.body(), listType);
        } else {
            throw new IOException("Error al consultar extracto: Código de estado " + response.statusCode());
        }
    }

    /**
     * Busca una cuenta iterando por las distintas sucursales de la red (IDs 1-5).
     * Retorna el DTO de la cuenta o null si no se encuentra en ninguna sucursal.
     */
    public CuentaClienteDTO buscarCuentaEnRed(String numCuenta) {
        if (!conectado) {
            try {
                conectar();
            } catch (Exception e) {
                return null;
            }
        }

        // Buscamos en sucursales con IDs del 1 al 5
        for (int i = 1; i <= 5; i++) {
            try {
                List<CuentaClienteDTO> cuentas = listarCuentasPorSucursal(i);
                if (cuentas != null) {
                    CuentaClienteDTO c = cuentas.stream()
                            .filter(x -> x.getNumeroCuenta().equalsIgnoreCase(numCuenta))
                            .findFirst()
                            .orElse(null);
                    if (c != null) {
                        return c;
                    }
                }
            } catch (Exception e) {
                // Silenciamos excepciones durante el escaneo para continuar buscando en otras sucursales
            }
        }
        return null;
    }

    // ==========================================
    // DTOs de Petición (Request Bodies) internos
    // ==========================================

    private static class LoginRequest {
        private final String usuario;
        private final String clave;

        public LoginRequest(String usuario, String clave) {
            this.usuario = usuario;
            this.clave = clave;
        }
    }

    private static class TransaccionRequest {
        private final String cuentaId;
        private final double monto;
        private final int empleadoId;

        public TransaccionRequest(String cuentaId, double monto, int empleadoId) {
            this.cuentaId = cuentaId;
            this.monto = monto;
            this.empleadoId = empleadoId;
        }
    }
}
