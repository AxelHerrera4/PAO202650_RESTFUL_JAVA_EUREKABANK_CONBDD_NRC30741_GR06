/**
 * Servicio de conexión RESTful para Eureka Bank.
 * Reemplaza soapService.js mapeando peticiones HTTP JSON y parámetros GET.
 */

/**
 * Autentica al usuario y devuelve sus datos si tiene éxito.
 * Consume: POST /eureka-api/resources/eurekabank/autenticar
 * Formato backend: "SUCCESS: Bienvenido Nombre (Rol)"
 */
export async function autenticarUsuario(usuario: string, clave: string) {
    try {
        const response = await fetch("/eureka-api/resources/eurekabank/autenticar", {
            method: "POST",
            headers: {
                "Content-Type": "application/json; charset=utf-8"
            },
            body: JSON.stringify({ usuario, clave })
        });

        const returnText = await response.text();
        console.log("DEBUG: Respuesta login recibida ->", returnText);

        if (response.ok && returnText.startsWith("SUCCESS:")) {
            // Extraemos nombre y rol del formato "SUCCESS: Bienvenido Nombre (Rol)"
            const match = returnText.match(/Bienvenido\s+(.+)\s+\((.+)\)/i);
            
            if (match) {
                const nombre = match[1].trim();
                const rol = match[2].trim();
                
                return {
                    success: true,
                    nombre: nombre,
                    rol: rol,
                    idSucursal: 1, // Valor por defecto ya que el backend no lo entrega en el login
                    nombreSucursal: "Sede Principal - Eureka Bank"
                };
            }
        }
        
        return { 
            success: false, 
            message: returnText.replace("ERROR: ", "") || "Error de autenticación" 
        };
    } catch (error) {
        console.error("REST Error:", error);
        return {
            success: false,
            message: "Error de conexión con el servidor."
        };
    }
}

/**
 * Lista las cuentas de una sucursal específica.
 * Consume: GET /eureka-api/resources/eurekabank/listarCuentasPorSucursal?sucursalId=X
 */
export async function listarCuentasPorSucursal(idSucursal: number) {
    try {
        const response = await fetch(`/eureka-api/resources/eurekabank/listarCuentasPorSucursal?sucursalId=${idSucursal}`);
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json();
        
        return data.map((item: any) => ({
            numero: item.numeroCuenta || "N/A",
            titular: `${item.nombreCliente || ""} ${item.apellidoCliente || ""}`.trim() || "Consumidor Final",
            saldo: item.saldo || 0,
            estado: (item.disponibilidad || "VERDE").toUpperCase() === "VERDE" ? "LIBRE" : "OCUPADA"
        }));
    } catch (error) {
        console.error("REST Error:", error);
        throw error;
    }
}
