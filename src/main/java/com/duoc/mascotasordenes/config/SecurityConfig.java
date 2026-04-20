package com.duoc.mascotasordenes.config; // Paquete de configuración (seguridad, CORS, etc.)

import org.springframework.context.annotation.Bean; // Declara métodos productores de beans para el contenedor Spring
import org.springframework.context.annotation.Configuration; // Marca la clase como fuente de configuración de Spring
import org.springframework.http.HttpMethod; // Enumeración de verbos HTTP (GET, POST, PUT, DELETE, PATCH, OPTIONS)
import org.springframework.security.config.Customizer; // Utilidad para aplicar configuraciones por defecto
import org.springframework.security.config.annotation.web.builders.HttpSecurity; // Constructor fluente para la cadena de filtros
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity; // Activa la configuración web de Spring Security
import org.springframework.security.config.http.SessionCreationPolicy; // Políticas de creación de sesión (STATELESS para APIs REST)
import org.springframework.security.core.userdetails.User; // Utilidad para construir usuarios en memoria
import org.springframework.security.core.userdetails.UserDetails; // Interfaz que representa un usuario autenticado
import org.springframework.security.core.userdetails.UserDetailsService; // Servicio que provee usuarios por nombre
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Algoritmo de hashing BCrypt
import org.springframework.security.crypto.password.PasswordEncoder; // Abstracción del algoritmo de hashing
import org.springframework.security.provisioning.InMemoryUserDetailsManager; // Implementación en memoria del UserDetailsService
import org.springframework.security.web.SecurityFilterChain; // Cadena de filtros que Spring Security expone como bean
import org.springframework.web.cors.CorsConfiguration; // Objeto de configuración de CORS
import org.springframework.web.cors.CorsConfigurationSource; // Fuente que entrega la configuración CORS a Spring Security
import org.springframework.web.cors.UrlBasedCorsConfigurationSource; // Implementación que mapea la configuración por patrón de URL

import java.util.List; // Colección usada para las listas de orígenes, métodos y headers permitidos

@Configuration // Clase de configuración: Spring escaneará sus @Bean al arrancar
@EnableWebSecurity // Activa Spring Security y permite personalizar la cadena de filtros HTTP
public class SecurityConfig { // Configuración de autenticación Basic, autorización por roles y CORS (IL6)

    @Bean // Expone el codificador de contraseñas como bean reusable
    public PasswordEncoder passwordEncoder() { // Fábrica del PasswordEncoder
        return new BCryptPasswordEncoder(); // BCrypt: hashing con salt automático, estándar de la industria
    }

    @Bean // Expone el servicio de usuarios en memoria
    public UserDetailsService userDetailsService(PasswordEncoder encoder) { // Recibe el encoder para hashear las contraseñas
        UserDetails admin = User.withUsername("admin") // Usuario con rol ADMIN (puede modificar/eliminar)
                .password(encoder.encode("admin123")) // Hashea la contraseña "admin123"
                .roles("ADMIN", "USER") // Asigna ambos roles
                .build(); // Construye el UserDetails

        UserDetails user = User.withUsername("user") // Usuario con rol USER (solo lectura)
                .password(encoder.encode("user123")) // Hashea la contraseña "user123"
                .roles("USER") // Asigna únicamente el rol USER
                .build(); // Construye el UserDetails

        return new InMemoryUserDetailsManager(admin, user); // Registra los dos usuarios en el gestor en memoria
    }

    @Bean // Expone la configuración CORS como bean
    public CorsConfigurationSource corsConfigurationSource() { // Define qué orígenes, métodos y headers acepta el microservicio
        CorsConfiguration config = new CorsConfiguration(); // Objeto mutable con la configuración CORS
        config.setAllowedOriginPatterns(List.of("http://localhost:*", "http://127.0.0.1:*")); // Permite cualquier puerto de localhost (frontend en dev)
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")); // Verbos HTTP permitidos
        config.setAllowedHeaders(List.of("*")); // Acepta cualquier header (Authorization, Content-Type, etc.)
        config.setExposedHeaders(List.of("Authorization", "Content-Type")); // Headers visibles desde el navegador
        config.setAllowCredentials(true); // Permite enviar cookies/credenciales (necesario para Basic Auth desde el front)
        config.setMaxAge(3600L); // Cachea el preflight CORS durante 1 hora

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(); // Mapea la configuración por URL
        source.registerCorsConfiguration("/**", config); // Aplica la configuración a todos los endpoints
        return source; // Spring Security usará esta fuente para evaluar los preflights
    }

    @Bean // Expone la cadena de filtros de seguridad
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { // Construye la cadena fluentemente
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Activa CORS con nuestra configuración
                .csrf(csrf -> csrf.disable()) // Desactiva CSRF: innecesario en APIs REST stateless con Basic Auth
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Sin sesión HTTP
                .authorizeHttpRequests(auth -> auth // Configura qué endpoints requieren qué rol
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Preflight CORS siempre permitido
                        .requestMatchers(HttpMethod.GET, "/api/**").hasAnyRole("USER", "ADMIN") // Lecturas: cualquier usuario autenticado
                        .requestMatchers(HttpMethod.POST, "/api/**").hasRole("ADMIN") // Escrituras: solo ADMIN
                        .requestMatchers(HttpMethod.PUT, "/api/**").hasRole("ADMIN") // Actualizaciones: solo ADMIN
                        .requestMatchers(HttpMethod.PATCH, "/api/**").hasRole("ADMIN") // Cambios parciales: solo ADMIN
                        .requestMatchers(HttpMethod.DELETE, "/api/**").hasRole("ADMIN") // Eliminaciones: solo ADMIN
                        .anyRequest().authenticated() // Cualquier otra ruta requiere autenticación
                )
                .httpBasic(Customizer.withDefaults()); // Habilita autenticación HTTP Basic

        return http.build(); // Construye y devuelve la cadena de filtros
    }
}
