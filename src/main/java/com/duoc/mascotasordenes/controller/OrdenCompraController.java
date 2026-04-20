package com.duoc.mascotasordenes.controller; // Paquete de controladores REST (capa de presentación / API HTTP)

import com.duoc.mascotasordenes.entity.OrdenCompra; // Entidad que representa una orden de compra de productos para mascotas
import com.duoc.mascotasordenes.service.OrdenCompraService; // Capa de servicio con la lógica de negocio de órdenes
import jakarta.validation.Valid; // Activa la validación automática del cuerpo de la petición según las anotaciones de la entidad
import org.slf4j.Logger; // Interfaz estándar de logging
import org.slf4j.LoggerFactory; // Fábrica para obtener un Logger vinculado a esta clase
import org.springframework.http.HttpStatus; // Enumeración de códigos HTTP (201 CREATED, 204 NO_CONTENT, etc.)
import org.springframework.http.ResponseEntity; // Permite construir respuestas HTTP con cuerpo y código de estado
import org.springframework.web.bind.annotation.DeleteMapping; // Marca métodos que atienden peticiones HTTP DELETE
import org.springframework.web.bind.annotation.GetMapping; // Marca métodos que atienden peticiones HTTP GET
import org.springframework.web.bind.annotation.PatchMapping; // Marca métodos que atienden peticiones HTTP PATCH (cambio parcial)
import org.springframework.web.bind.annotation.PathVariable; // Inyecta un segmento de la URL como parámetro del método
import org.springframework.web.bind.annotation.PostMapping; // Marca métodos que atienden peticiones HTTP POST
import org.springframework.web.bind.annotation.PutMapping; // Marca métodos que atienden peticiones HTTP PUT
import org.springframework.web.bind.annotation.RequestBody; // Deserializa el cuerpo JSON de la petición a un objeto Java
import org.springframework.web.bind.annotation.RequestMapping; // Define el prefijo común de ruta para todos los endpoints
import org.springframework.web.bind.annotation.RestController; // Controlador REST: registra como bean y serializa respuestas a JSON

import java.util.List; // Interfaz de colección ordenada para devolver listas de órdenes
import java.util.Map; // Tipo de diccionario usado para recibir cambios parciales (por ejemplo, solo el estado)

@RestController // Registra esta clase como bean de Spring y convierte automáticamente las respuestas a JSON
@RequestMapping("/api/ordenes") // Todas las rutas de este controlador empiezan por /api/ordenes
public class OrdenCompraController { // Expone la API HTTP REST para órdenes de compra de mascotas

    private static final Logger log = LoggerFactory.getLogger(OrdenCompraController.class); // Logger de la capa REST

    private final OrdenCompraService ordenCompraService; // Dependencia inmutable al servicio de órdenes

    public OrdenCompraController(OrdenCompraService ordenCompraService) { // Inyección de dependencias por constructor
        this.ordenCompraService = ordenCompraService; // Asigna el servicio inyectado por Spring
    }

    @PostMapping // POST /api/ordenes → crea una nueva orden de compra
    public ResponseEntity<OrdenCompra> crearOrden(@Valid @RequestBody OrdenCompra orden) { // @Valid activa las validaciones definidas en la entidad
        log.info("POST /api/ordenes - cliente='{}', items={}", orden.getClienteNombre(), orden.getDetalles().size()); // Log INFO de creación
        OrdenCompra nuevaOrden = ordenCompraService.crearOrden(orden); // Delega la creación al servicio (valida stock, calcula total)
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaOrden); // "201" Created + cuerpo con la orden creada
    }

    @GetMapping // GET /api/ordenes → devuelve todas las órdenes de compra
    public List<OrdenCompra> obtenerTodas() { // Endpoint para listar todas las órdenes registradas
        log.debug("GET /api/ordenes"); // Log DEBUG
        return ordenCompraService.obtenerTodas(); // Delega al servicio y retorna la lista completa como JSON
    }

    @GetMapping("/{id}") // GET /api/ordenes/{id} → busca una orden por su identificador
    public OrdenCompra obtenerPorId(@PathVariable Long id) { // El id viene del segmento de URL
        log.debug("GET /api/ordenes/{}", id); // Log DEBUG con el id buscado
        return ordenCompraService.obtenerPorId(id); // El servicio lanza "404" si no existe
    }

    @GetMapping("/estado/{estado}") // GET /api/ordenes/estado/{estado} → filtra órdenes por estado
    public List<OrdenCompra> obtenerPorEstado(@PathVariable String estado) { // El estado viene del segmento de URL
        log.debug("GET /api/ordenes/estado/{}", estado); // Log DEBUG con el estado filtrado
        return ordenCompraService.obtenerPorEstado(estado); // Delega al servicio y retorna la lista filtrada como JSON
    }

    @GetMapping("/cliente/{clienteNombre}") // GET /api/ordenes/cliente/{clienteNombre} → busca órdenes por nombre de cliente
    public List<OrdenCompra> obtenerPorCliente(@PathVariable String clienteNombre) { // El nombre del cliente viene del segmento de URL
        log.debug("GET /api/ordenes/cliente/{}", clienteNombre); // Log DEBUG con el texto buscado
        return ordenCompraService.obtenerPorCliente(clienteNombre); // Delega al servicio y retorna las órdenes que coinciden
    }

    @PutMapping("/{id}") // PUT /api/ordenes/{id} → actualiza los datos de cabecera de una orden
    public OrdenCompra actualizar(@PathVariable Long id, @Valid @RequestBody OrdenCompra orden) { // id por URL, datos por cuerpo JSON
        log.info("PUT /api/ordenes/{}", id); // Log INFO de actualización
        return ordenCompraService.actualizar(id, orden); // Delega al servicio
    }

    @PatchMapping("/{id}/estado") // PATCH /api/ordenes/{id}/estado → cambio parcial, solo actualiza el estado
    public OrdenCompra cambiarEstado(@PathVariable Long id, @RequestBody Map<String, String> body) { // id por URL, nuevo estado en body JSON
        String nuevoEstado = body.get("estado"); // Extrae el campo "estado" del body
        log.info("PATCH /api/ordenes/{}/estado - nuevo estado='{}'", id, nuevoEstado); // Log INFO con el cambio solicitado
        return ordenCompraService.cambiarEstado(id, nuevoEstado); // Delega al servicio (valida valores permitidos)
    }

    @DeleteMapping("/{id}") // DELETE /api/ordenes/{id} → elimina una orden por id (con cascada a sus detalles)
    public ResponseEntity<Void> eliminar(@PathVariable Long id) { // id por URL
        log.warn("DELETE /api/ordenes/{}", id); // Log WARN
        ordenCompraService.eliminar(id); // Delega al servicio; lanza "404" si no existe
        return ResponseEntity.noContent().build(); // "204" No Content
    }
}
