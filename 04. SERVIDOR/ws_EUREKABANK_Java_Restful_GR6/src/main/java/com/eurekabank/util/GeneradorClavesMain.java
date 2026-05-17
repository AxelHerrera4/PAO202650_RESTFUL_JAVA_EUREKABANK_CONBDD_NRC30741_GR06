package com.eurekabank.util;

/**
 * Script Generador Temporal para calcular los hashes BCrypt de los usuarios semilla.
 * @author Antigravity
 */
public class GeneradorClavesMain {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("   GENERADOR DE HASHES BCRYPT - EUREKA BANK       ");
        System.out.println("==================================================");

        String claveAdmin = "monster9";
        String claveCajero = "cajero123";
        String claveGerente = "gerente123";

        String hashAdmin = SeguridadUtil.hashPassword(claveAdmin);
        String hashCajero = SeguridadUtil.hashPassword(claveCajero);
        String hashGerente = SeguridadUtil.hashPassword(claveGerente);

        System.out.println("\n🔑 Usuario: monster (Superadmin)");
        System.out.println("   Texto plano: " + claveAdmin);
        System.out.println("   BCrypt Hash: " + hashAdmin);

        System.out.println("\n🔑 Usuario: cajero1 (Cajero)");
        System.out.println("   Texto plano: " + claveCajero);
        System.out.println("   BCrypt Hash: " + hashCajero);

        System.out.println("\n🔑 Usuario: gerente1 (Gerente)");
        System.out.println("   Texto plano: " + claveGerente);
        System.out.println("   BCrypt Hash: " + hashGerente);
        
        System.out.println("\n==================================================");
        System.out.println("Copia estos hashes e insértalos en tu script SQL o");
        System.out.println("directamente en la tabla 'empleado' de tu MySQL.");
        System.out.println("==================================================");
    }
}
