package com.duoc.mascotasordenes.controller; // Paquete de controladores REST (capa de presentación / API HTTP)

import com.duoc.mascotasordenes.entity.OrdenCompra; // Entidad que representa una orden de compra de productos para mascotas
import com.duoc.mascotasordenes.service.OrdenCompraService; // Capa de servicio con la lógica de negocio de órdenes
import jakarta.validation.Valid; // Activa la validación automática del cuerpo de la petición según las anotaciones de la entidad
import org.springframework.http.HttpStatus; // Enumeración de códigos de estado HTTP (201 CREATED, 400 BAD REQUEST, etc.)
import org.springframework.http.ResponseEntity; // Permite construir respuestas HTTP con cuerpo y código de estado
import org.springframework.web.bind.annotation.GetMapping; // Marca métodos que atienden peticiones HTTP GET
import org.springframework.web.bind.annotation.PathVariable; // Inyecta un segmento de la URL como parámetro del método
import org.springframework.web.bind.annotation.PostMapping; // Marca métodos que atienden peticiones HTTP POST
import org.springframework.web.bind.annotation.RequestBody; // Deserializa el cuerpo JSON de la petición a un objeto Java
import org.springframework.web.bind.annotation.RequestMapping; // Define el prefijo común de ruta para todos los endpoints
import org.springframework.web.bind.annotation.RestController; // Controlador REST: registra como bean y serializa respuestas a JSON

import java.util.List; // Interfaz de colección ordenada para devolver listas de órdenes

@RestController // Registra esta clase como bean de Spring y convierte automáticamente las respuestas a JSON
@RequestMapping("/api/ordenes") // Todas las rutas de este controlador empiezan por /api/ordenes
public class OrdenCompraController { // Expone la API HTTP REST para órdenes de compra de mascotas

    private final OrdenCompraService ordenCompraService; // Dependencia inmutable al servicio de órdenes

    public OrdenCompraController(OrdenCompraService ordenCompraService) { // Inyección de dependencias por constructor
        this.ordenCompraService = ordenCompraService; // Asigna el servicio inyectado por Spring
    }

    @PostMapping // POST /api/ordenes → crea una nueva orden de compra
    public ResponseEntity<OrdenCompra> crearOrden(@Valid @RequestBody OrdenCompra orden) { // @Valid activa las validaciones definidas en la entidad; @RequestBody deserializa el JSON del body
        OrdenCompra nuevaOrden = ordenCompraService.crearOrden(orden); // Delega la creación al servicio
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaOrden); // Responde HTTP 201 Created con la orden creada (incluye el id asignado)
    }

    @GetMapping // GET /api/ordenes → devuelve todas las órdenes de compra
    public List<OrdenCompra> obtenerTodas() { // Endpoint para listar todas las órdenes registradas
        return ordenCompraService.obtenerTodas(); // Delega al servicio y retorna la lista completa como JSON
    }

    @GetMapping("/{id}") // GET /api/ordenes/{id} → busca una orden por su identificador
    public ResponseEntity<OrdenCompra> obtenerPorId(@PathVariable Long id) { // El id viene del segmento de URL
        return ordenCompraService.obtenerPorId(id) // Busca la orden por clave primaria en el servicio
                .map(ResponseEntity::ok) // Si existe: envuelve en HTTP 200 OK con la orden como cuerpo
                .orElse(ResponseEntity.notFound().build()); // Si no existe: responde HTTP 404 Not Found sin cuerpo
    }

    @GetMapping("/estado/{estado}") // GET /api/ordenes/estado/{estado} → filtra órdenes por estado
    public List<OrdenCompra> obtenerPorEstado(@PathVariable String estado) { // El estado viene del segmento de URL (PENDIENTE, PROCESANDO, etc.)
        return ordenCompraService.obtenerPorEstado(estado); // Delega al servicio y retorna la lista filtrada como JSON
    }

    @GetMapping("/cliente/{clienteNombre}") // GET /api/ordenes/cliente/{clienteNombre} → busca órdenes por nombre de cliente
    public List<OrdenCompra> obtenerPorCliente(@PathVariable String clienteNombre) { // El nombre del cliente viene del segmento de URL
        return ordenCompraService.obtenerPorCliente(clienteNombre); // Delega al servicio y retorna las órdenes que coinciden
    }
}
