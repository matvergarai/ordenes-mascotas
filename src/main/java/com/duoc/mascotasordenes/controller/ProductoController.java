package com.duoc.mascotasordenes.controller; // Paquete de controladores REST (capa de presentación / API HTTP)

import com.duoc.mascotasordenes.entity.Producto; // Entidad que representa un producto para mascotas
import com.duoc.mascotasordenes.service.ProductoService; // Capa de servicio con la lógica de negocio de productos
import jakarta.validation.Valid; // Activa la validación automática del cuerpo de la petición según las anotaciones de la entidad
import org.slf4j.Logger; // Interfaz estándar de logging
import org.slf4j.LoggerFactory; // Fábrica para obtener un Logger vinculado a esta clase
import org.springframework.http.HttpStatus; // Enumeración de códigos HTTP
import org.springframework.http.ResponseEntity; // Permite construir respuestas HTTP con cuerpo y código de estado
import org.springframework.web.bind.annotation.DeleteMapping; // Marca métodos que atienden peticiones HTTP DELETE
import org.springframework.web.bind.annotation.GetMapping; // Marca métodos que atienden peticiones HTTP GET
import org.springframework.web.bind.annotation.PathVariable; // Inyecta un segmento de la URL como parámetro del método
import org.springframework.web.bind.annotation.PostMapping; // Marca métodos que atienden peticiones HTTP POST
import org.springframework.web.bind.annotation.PutMapping; // Marca métodos que atienden peticiones HTTP PUT
import org.springframework.web.bind.annotation.RequestBody; // Deserializa el cuerpo JSON de la petición a un objeto Java
import org.springframework.web.bind.annotation.RequestMapping; // Define el prefijo común de ruta para todos los endpoints
import org.springframework.web.bind.annotation.RestController; // Controlador REST: registra como bean y serializa respuestas a JSON

import java.util.List; // Interfaz de colección ordenada para devolver listas de productos

@RestController // Registra esta clase como bean de Spring y convierte automáticamente las respuestas a JSON
@RequestMapping("/api/productos") // Todas las rutas de este controlador empiezan por /api/productos
public class ProductoController { // Expone la API HTTP REST para productos de mascotas

    private static final Logger log = LoggerFactory.getLogger(ProductoController.class); // Logger de la capa REST

    private final ProductoService productoService; // Dependencia inmutable al servicio de productos

    public ProductoController(ProductoService productoService) { // Inyección de dependencias por constructor
        this.productoService = productoService; // Asigna el servicio inyectado por Spring
    }

    @GetMapping // GET /api/productos → devuelve todos los productos del catálogo
    public List<Producto> obtenerTodos() { // Endpoint para listar todos los productos disponibles
        log.debug("GET /api/productos"); // Log DEBUG de la operación
        return productoService.obtenerTodos(); // Delega al servicio y retorna la lista completa como JSON
    }

    @GetMapping("/{id}") // GET /api/productos/{id} → busca un producto por su identificador
    public Producto obtenerPorId(@PathVariable Long id) { // El id viene del segmento de URL
        log.debug("GET /api/productos/{}", id); // Log DEBUG con el id buscado
        return productoService.obtenerPorId(id); // El servicio lanza "404" si no existe
    }

    @GetMapping("/categoria/{categoria}") // GET /api/productos/categoria/{categoria} → filtra por categoría
    public List<Producto> obtenerPorCategoria(@PathVariable String categoria) { // La categoría viene del segmento de URL
        log.debug("GET /api/productos/categoria/{}", categoria); // Log DEBUG con el filtro
        return productoService.obtenerPorCategoria(categoria); // Delega al servicio y retorna la lista filtrada como JSON
    }

    @GetMapping("/buscar/{nombre}") // GET /api/productos/buscar/{nombre} → busca por coincidencia parcial en nombre
    public List<Producto> buscarPorNombre(@PathVariable String nombre) { // El texto de búsqueda viene del segmento de URL
        log.debug("GET /api/productos/buscar/{}", nombre); // Log DEBUG con el texto buscado
        return productoService.buscarPorNombre(nombre); // Delega al servicio y retorna los productos que coinciden
    }

    @PostMapping // POST /api/productos → registra un nuevo producto
    public ResponseEntity<Producto> crear(@Valid @RequestBody Producto producto) { // @Valid activa validaciones; @RequestBody deserializa
        log.info("POST /api/productos - creando producto '{}'", producto.getNombre()); // Log INFO de creación
        Producto creado = productoService.crear(producto); // Delega al servicio
        return ResponseEntity.status(HttpStatus.CREATED).body(creado); // "201" Created + cuerpo con el producto creado
    }

    @PutMapping("/{id}") // PUT /api/productos/{id} → actualiza todos los datos de un producto existente
    public Producto actualizar(@PathVariable Long id, @Valid @RequestBody Producto producto) { // id por URL, datos por cuerpo JSON
        log.info("PUT /api/productos/{}", id); // Log INFO de actualización
        return productoService.actualizar(id, producto); // Delega al servicio
    }

    @DeleteMapping("/{id}") // DELETE /api/productos/{id} → elimina un producto por id
    public ResponseEntity<Void> eliminar(@PathVariable Long id) { // id por URL
        log.warn("DELETE /api/productos/{}", id); // Log WARN
        productoService.eliminar(id); // Delega al servicio; lanza "404" si no existe
        return ResponseEntity.noContent().build(); // "204" No Content
    }
}
