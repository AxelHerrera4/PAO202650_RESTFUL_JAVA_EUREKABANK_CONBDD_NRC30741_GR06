package com.eurekabank.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utilidades de Seguridad para encriptación y validación de contraseñas usando BCrypt.
 * @author Antigravity
 */
public class SeguridadUtil {

    private SeguridadUtil() {
        // Constructor privado para evitar instanciación
    }

    /**
     * Genera un hash BCrypt para una contraseña en texto plano.
     * @param plainTextPassword La contraseña sin encriptar.
     * @return El hash BCrypt resultante.
     */
    public static String hashPassword(String plainTextPassword) {
        // El factor de costo por defecto de BCrypt es 10 (12 es aún más seguro si el CPU lo permite)
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    /**
     * Compara una contraseña en texto plano contra un hash BCrypt registrado.
     * @param plainTextPassword La contraseña enviada por el cliente.
     * @param hashedPassword El hash registrado en la base de datos.
     * @return true si coinciden, false en caso contrario.
     */
    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        try {
            return BCrypt.checkpw(plainTextPassword, hashedPassword);
        } catch (Exception e) {
            System.err.println("Error al validar contraseña con BCrypt: " + e.getMessage());
            return false;
        }
    }
}
