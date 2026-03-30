package com.duoc.mascotasordenes; // Paquete raíz del microservicio de órdenes de productos para mascotas

import org.springframework.boot.SpringApplication; // Punto de entrada para arrancar Spring Boot
import org.springframework.boot.autoconfigure.SpringBootApplication; // Activa autoconfiguración, escaneo de componentes y configuración

@SpringBootApplication // Marca la clase como aplicación Spring Boot (equivale a @Configuration + @EnableAutoConfiguration + @ComponentScan)
public class MascotasOrdenesApplication { // Clase principal que inicia el microservicio

    public static void main(String[] args) { // Método estándar de Java: arranque del programa
        SpringApplication.run(MascotasOrdenesApplication.class, args); // Crea el contexto de Spring, registra beans y levanta el servidor Tomcat embebido
    }
}
