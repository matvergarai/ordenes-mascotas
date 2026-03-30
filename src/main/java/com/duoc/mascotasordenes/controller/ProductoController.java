package com.duoc.mascotasordenes.controller; // Paquete de controladores REST (capa de presentación / API HTTP)

import com.duoc.mascotasordenes.entity.Producto; // Entidad que representa un producto para mascotas
import com.duoc.mascotasordenes.service.ProductoService; // Capa de servicio con la lógica de negocio de productos
import org.springframework.http.ResponseEntity; // Permite construir respuestas HTTP con cuerpo y código de estado
import org.springframework.web.bind.annotation.GetMapping; // Marca métodos que atienden peticiones HTTP GET
import org.springframework.web.bind.annotation.PathVariable; // Inyecta un segmento de la URL como parámetro del método
import org.springframework.web.bind.annotation.RequestMapping; // Define el prefijo común de ruta para todos los endpoints
import org.springframework.web.bind.annotation.RestController; // Controlador REST: registra como bean y serializa respuestas a JSON

import java.util.List; // Interfaz de colección ordenada para devolver listas de productos

@RestController // Registra esta clase como bean de Spring y convierte automáticamente las respuestas a JSON
@RequestMapping("/api/productos") // Todas las rutas de este controlador empiezan por /api/productos
public class ProductoController { // Expone la API HTTP REST para productos de mascotas

    private final ProductoService productoService; // Dependencia inmutable al servicio de productos

    public ProductoController(ProductoService productoService) { // Inyección de dependencias por constructor
        this.productoService = productoService; // Asigna el servicio inyectado por Spring
    }

    @GetMapping // GET /api/productos → devuelve todos los productos del catálogo
    public List<Producto> obtenerTodos() { // Endpoint para listar todos los productos disponibles
        return productoService.obtenerTodos(); // Delega al servicio y retorna la lista completa como JSON
    }

    @GetMapping("/{id}") // GET /api/productos/{id} → busca un producto por su identificador
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) { // El id viene del segmento de URL
        return productoService.obtenerPorId(id) // Busca el producto por clave primaria en el servicio
                .map(ResponseEntity::ok) // Si existe: envuelve en HTTP 200 OK con el producto como cuerpo
                .orElse(ResponseEntity.notFound().build()); // Si no existe: responde HTTP 404 Not Found sin cuerpo
    }

    @GetMapping("/categoria/{categoria}") // GET /api/productos/categoria/{categoria} → filtra por categoría
    public List<Producto> obtenerPorCategoria(@PathVariable String categoria) { // La categoría viene del segmento de URL
        return productoService.obtenerPorCategoria(categoria); // Delega al servicio y retorna la lista filtrada como JSON
    }

    @GetMapping("/buscar/{nombre}") // GET /api/productos/buscar/{nombre} → busca por coincidencia parcial en nombre
    public List<Producto> buscarPorNombre(@PathVariable String nombre) { // El texto de búsqueda viene del segmento de URL
        return productoService.buscarPorNombre(nombre); // Delega al servicio y retorna los productos que coinciden
    }
}
