import { API_CONFIG } from '../constants/Config';

export const restMobileService = {
    /**
     * Autenticar cliente usando DNI como usuario y contraseña
     */
    async login(dni) {
        try {
            console.log("[REST] Intentando iniciar sesión con DNI:", dni);
            const response = await fetch(`${API_CONFIG.BASE_URL}/autenticar`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    usuario: dni,
                    clave: dni
                })
            });

            const text = await response.text();
            console.log("[REST] Respuesta recibida:", text);

            if (!response.ok) {
                throw new Error(text.replace("ERROR: ", "") || "DNI no registrado o error de acceso");
            }

            if (text.startsWith("SUCCESS:")) {
                const match = text.match(/Bienvenido\s+(.+)\s+\((.+)\)/i);
                if (match) {
                    const role = match[2].trim();
                    if (role.toLowerCase() !== 'cliente') {
                        throw new Error("Acceso solo permitido para clientes.");
                    }
                    return {
                        success: true,
                        nombre: match[1].trim(),
                        rol: role,
                        usuario: dni // Guardamos el DNI para consultas futuras
                    };
                }
            }

            throw new Error(text.replace("ERROR: ", "") || "DNI no registrado o rol incorrecto");
        } catch (error) {
            console.error("[REST] Error en login:", error);
            throw error;
        }
    },

    /**
     * Obtener saldo y estado de cuenta (mashup de saldo + semáforo de disponibilidad)
     */
    async getAccountStatus(usuario) {
        try {
            console.log("[REST] Consultando estado de cuenta para DNI:", usuario);
            const response = await fetch(`${API_CONFIG.BASE_URL}/consultarCuentasPorCliente?dni=${usuario}`);
            
            if (!response.ok) {
                if (response.status === 404) {
                    console.log("[REST] No se encontró cuenta para el DNI:", usuario);
                    return null;
                }
                throw new Error(`Error en servidor (${response.status})`);
            }

            const data = await response.json();
            console.log("[REST] Datos de cuenta obtenidos:", data);
            
            if (!data) return null;

            return {
                numero: data.numeroCuenta,
                saldo: data.saldo !== undefined ? data.saldo.toString() : "0.0",
                disponibilidad: data.disponibilidad || "ROJO" // VERDE o ROJO
            };
        } catch (error) {
            console.error("[REST] Error en getAccountStatus:", error);
            throw error;
        }
    },

    /**
     * Obtener el historial de movimientos de la cuenta
     */
    async getExtracto(numeroCuenta) {
        try {
            console.log("[REST] Consultando extracto para cuenta:", numeroCuenta);
            const response = await fetch(`${API_CONFIG.BASE_URL}/consultarExtracto?cuentaId=${numeroCuenta}`);
            
            if (!response.ok) {
                throw new Error(`Error en servidor (${response.status})`);
            }

            const list = await response.json();
            console.log("[REST] Movimientos recibidos:", list.length);

            if (!Array.isArray(list)) return [];

            return list.map((item, index) => ({
                id: item.idMovimiento !== undefined ? item.idMovimiento.toString() : Math.random().toString(36).substr(2, 9),
                tipo: item.tipo || "Desconocido",
                monto: item.monto !== undefined ? item.monto.toString() : "0.0",
                fecha: item.fechaHora || "Sin fecha"
            }));
        } catch (error) {
            console.error("[REST] Error en getExtracto:", error);
            return [];
        }
    }
};
