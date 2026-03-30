package com.duoc.mascotasordenes.repository; // Paquete de la capa de acceso a datos (repositorio de órdenes de compra)

import com.duoc.mascotasordenes.entity.DetalleOrden; // Modelo que representa cada línea/ítem de una orden
import com.duoc.mascotasordenes.entity.OrdenCompra; // Modelo principal de orden de compra
import jakarta.annotation.PostConstruct; // Marca un método a ejecutar tras construir el bean de Spring
import org.springframework.stereotype.Repository; // Registra la clase como componente de persistencia en el contexto Spring

import java.util.ArrayList; // Lista redimensionable para almacenar órdenes en memoria
import java.util.Arrays; // Utilidad para crear listas a partir de arreglos (Arrays.asList)
import java.util.List; // Interfaz de colección ordenada
import java.util.Optional; // Contenedor que puede tener o no un valor (evita null)
import java.util.stream.Collectors; // Utilidad para recopilar resultados de un stream en una colección

@Repository // Spring crea una instancia única (singleton) y la inyecta donde se necesite
public class OrdenCompraRepository { // Implementación en memoria del repositorio de órdenes (no usa base de datos)

    private final List<OrdenCompra> ordenes = new ArrayList<>(); // Lista mutable de órdenes; la referencia es final pero el contenido puede cambiar

    @PostConstruct // Se ejecuta una vez después del constructor y de inyectar dependencias
    public void init() { // Carga datos de ejemplo (órdenes de compra) al arrancar la aplicación
        ordenes.add(new OrdenCompra(1L, "María González", "maria.gonzalez@correo.cl", // Orden 1: entregada, cliente de Providencia
                "2026-03-15", "ENTREGADA", 84980, "Av. Providencia 1234, Santiago", "Tarjeta de Crédito",
                Arrays.asList( // Lista de detalles (productos) de la orden 1
                        new DetalleOrden(1L, 1L, 1L, "Royal Canin Medium Adult", 1, 45990, 45990), // Detalle 1: alimento para perros
                        new DetalleOrden(2L, 1L, 5L, "Shampoo Antipulgas para Perros", 1, 8990, 8990), // Detalle 2: shampoo medicado
                        new DetalleOrden(3L, 1L, 7L, "Collar Ajustable de Nylon", 2, 7990, 15980), // Detalle 3: dos collares
                        new DetalleOrden(4L, 1L, 4L, "Ratón con Catnip Felino", 1, 5990, 5990)))); // Detalle 4: juguete para gatos

        ordenes.add(new OrdenCompra(2L, "Carlos Muñoz", "carlos.munoz@correo.cl", // Orden 2: en procesamiento, cliente de Providencia
                "2026-03-18", "PROCESANDO", 54980, "Calle Los Leones 567, Providencia", "Débito",
                Arrays.asList( // Lista de detalles de la orden 2
                        new DetalleOrden(5L, 2L, 2L, "Pro Plan Puppy Razas Pequeñas", 1, 38990, 38990), // Detalle 5: alimento para cachorros
                        new DetalleOrden(6L, 2L, 3L, "Pelota Kong Classic Roja", 1, 15990, 15990)))); // Detalle 6: juguete Kong

        ordenes.add(new OrdenCompra(3L, "Ana Pérez", "ana.perez@correo.cl", // Orden 3: pendiente, cliente de Ñuñoa
                "2026-03-20", "PENDIENTE", 63970, "Pasaje El Roble 89, Ñuñoa", "Transferencia Bancaria",
                Arrays.asList( // Lista de detalles de la orden 3
                        new DetalleOrden(7L, 3L, 8L, "Cama Ortopédica para Perros", 1, 34990, 34990), // Detalle 7: cama ortopédica
                        new DetalleOrden(8L, 3L, 9L, "Arena Sanitaria para Gatos", 2, 9990, 19980), // Detalle 8: dos bolsas de arena
                        new DetalleOrden(9L, 3L, 5L, "Shampoo Antipulgas para Perros", 1, 8990, 8990)))); // Detalle 9: shampoo

        ordenes.add(new OrdenCompra(4L, "Roberto Silva", "roberto.silva@correo.cl", // Orden 4: enviada, cliente de Ñuñoa
                "2026-03-10", "ENVIADA", 37980, "Av. Irarrázaval 2345, Ñuñoa", "Tarjeta de Crédito",
                Arrays.asList( // Lista de detalles de la orden 4
                        new DetalleOrden(10L, 4L, 10L, "Pipeta Antipulgas Gatos", 1, 18990, 18990), // Detalle 10: pipeta para gatos
                        new DetalleOrden(11L, 4L, 6L, "Antiparasitario Interno Canino", 1, 12990, 12990), // Detalle 11: desparasitante
                        new DetalleOrden(12L, 4L, 4L, "Ratón con Catnip Felino", 1, 5990, 5990)))); // Detalle 12: juguete para gatos

        ordenes.add(new OrdenCompra(5L, "Claudia Rojas", "claudia.rojas@correo.cl", // Orden 5: cancelada, cliente de Macul
                "2026-03-22", "CANCELADA", 45990, "Av. Macul 678, Macul", "Débito",
                Arrays.asList( // Lista de detalles de la orden 5
                        new DetalleOrden(13L, 5L, 1L, "Royal Canin Medium Adult", 1, 45990, 45990)))); // Detalle 13: alimento para perros

        ordenes.add(new OrdenCompra(6L, "José Herrera", "jose.herrera@correo.cl", // Orden 6: pendiente, cliente de Santiago Centro
                "2026-03-25", "PENDIENTE", 74960, "Calle Moneda 910, Santiago Centro", "Tarjeta de Crédito",
                Arrays.asList( // Lista de detalles de la orden 6
                        new DetalleOrden(14L, 6L, 2L, "Pro Plan Puppy Razas Pequeñas", 1, 38990, 38990), // Detalle 14: alimento para cachorros
                        new DetalleOrden(15L, 6L, 3L, "Pelota Kong Classic Roja", 1, 15990, 15990), // Detalle 15: juguete Kong
                        new DetalleOrden(16L, 6L, 9L, "Arena Sanitaria para Gatos", 2, 9990, 19980)))); // Detalle 16: dos bolsas de arena

        ordenes.add(new OrdenCompra(7L, "Valentina Díaz", "valentina.diaz@correo.cl", // Orden 7: en procesamiento, cliente de Las Condes
                "2026-03-27", "PROCESANDO", 61970, "Av. Apoquindo 4567, Las Condes", "Transferencia Bancaria",
                Arrays.asList( // Lista de detalles de la orden 7
                        new DetalleOrden(17L, 7L, 8L, "Cama Ortopédica para Perros", 1, 34990, 34990), // Detalle 17: cama ortopédica
                        new DetalleOrden(18L, 7L, 6L, "Antiparasitario Interno Canino", 1, 12990, 12990), // Detalle 18: desparasitante
                        new DetalleOrden(19L, 7L, 4L, "Ratón con Catnip Felino", 1, 5990, 5990), // Detalle 19: juguete para gatos
                        new DetalleOrden(20L, 7L, 7L, "Collar Ajustable de Nylon", 1, 7990, 7990)))); // Detalle 20: collar para perros

        ordenes.add(new OrdenCompra(8L, "María González", "maria.gonzalez@correo.cl", // Orden 8: pendiente, segunda compra de María
                "2026-03-28", "PENDIENTE", 47980, "Av. Providencia 1234, Santiago", "Débito",
                Arrays.asList( // Lista de detalles de la orden 8
                        new DetalleOrden(21L, 8L, 10L, "Pipeta Antipulgas Gatos", 1, 18990, 18990), // Detalle 21: pipeta para gatos
                        new DetalleOrden(22L, 8L, 9L, "Arena Sanitaria para Gatos", 2, 9990, 19980), // Detalle 22: dos bolsas de arena
                        new DetalleOrden(23L, 8L, 5L, "Shampoo Antipulgas para Perros", 1, 8990, 8990)))); // Detalle 23: shampoo

        ordenes.add(new OrdenCompra(9L, "Felipe Torres", "felipe.torres@correo.cl", // Orden 9: enviada, cliente de Santiago Centro
                "2026-03-29", "ENVIADA", 91970, "Calle Huérfanos 345, Santiago Centro", "Tarjeta de Crédito",
                Arrays.asList( // Lista de detalles de la orden 9
                        new DetalleOrden(24L, 9L, 1L, "Royal Canin Medium Adult", 1, 45990, 45990), // Detalle 24: alimento para perros
                        new DetalleOrden(25L, 9L, 3L, "Pelota Kong Classic Roja", 1, 15990, 15990), // Detalle 25: juguete Kong
                        new DetalleOrden(26L, 9L, 9L, "Arena Sanitaria para Gatos", 3, 9990, 29970)))); // Detalle 26: tres bolsas de arena
    }

    public OrdenCompra save(OrdenCompra orden) { // Persiste una nueva orden de compra en la lista en memoria
        Long nuevoId = ordenes.stream() // Genera un nuevo id único basado en el máximo existente
                .mapToLong(OrdenCompra::getId) // Extrae los ids de todas las órdenes
                .max() // Obtiene el id más alto
                .orElse(0L) + 1; // Si no hay órdenes, empieza en 1
        orden.setId(nuevoId); // Asigna el nuevo id a la orden
        ordenes.add(orden); // Agrega la orden a la lista en memoria
        return orden; // Retorna la orden con el id asignado
    }

    public List<OrdenCompra> findAll() { // Devuelve todas las órdenes de compra almacenadas
        return List.copyOf(ordenes); // Retorna una copia inmutable para proteger la lista interna
    }

    public Optional<OrdenCompra> findById(Long id) { // Busca una orden de compra por su identificador único
        return ordenes.stream() // Convierte la lista en un flujo (stream) para operaciones funcionales
                .filter(o -> o.getId().equals(id)) // Conserva solo la orden cuyo id coincide con el buscado
                .findFirst(); // Obtiene la primera que cumple el filtro, o Optional vacío si no existe
    }

    public List<OrdenCompra> findByEstado(String estado) { // Busca órdenes que tengan un estado específico
        return ordenes.stream() // Convierte la lista en un flujo para filtrado
                .filter(o -> o.getEstado().equalsIgnoreCase(estado)) // Filtra ignorando mayúsculas/minúsculas
                .collect(Collectors.toList()); // Recopila los resultados en una nueva lista
    }

    public List<OrdenCompra> findByClienteNombreContaining(String clienteNombre) { // Busca órdenes cuyo nombre de cliente contenga el texto dado
        return ordenes.stream() // Convierte la lista en un flujo para búsqueda parcial
                .filter(o -> o.getClienteNombre().toLowerCase().contains(clienteNombre.toLowerCase())) // Filtra por coincidencia parcial sin distinguir mayúsculas
                .collect(Collectors.toList()); // Recopila los resultados en una nueva lista
    }
}
