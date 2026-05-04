package com.duoc.mascotasordenes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // Habilita autoconfiguración, component scan y configuración Spring Boot.
public class MascotasOrdenesApplication {

    public static void main(String[] args) {
        SpringApplication.run(MascotasOrdenesApplication.class, args); // Levanta el contexto y el servidor embebido (puerto 8082).
    }
}
