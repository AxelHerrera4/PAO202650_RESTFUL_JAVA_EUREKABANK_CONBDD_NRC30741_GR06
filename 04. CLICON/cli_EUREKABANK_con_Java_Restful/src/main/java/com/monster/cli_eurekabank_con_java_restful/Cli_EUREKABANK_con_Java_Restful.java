package com.monster.cli_eurekabank_con_java_restful;

import com.monster.cli_eurekabank_con_java_restful.controlador.ConsoleController;
import com.monster.cli_eurekabank_con_java_restful.modelo.ClienteRestfulModel;
import com.monster.cli_eurekabank_con_java_restful.vista.ConsoleView;

/**
 * Punto de entrada principal (Bootstrap) para el Cliente de Consola RESTful de Eureka Bank.
 * Instancia e inicia la arquitectura MVC de la aplicación de soporte.
 */
public class Cli_EUREKABANK_con_Java_Restful {

    public static void main(String[] args) {
        ClienteRestfulModel model = new ClienteRestfulModel();
        ConsoleView view = new ConsoleView();
        ConsoleController controller = new ConsoleController(model, view);

        controller.iniciar();
    }
}
