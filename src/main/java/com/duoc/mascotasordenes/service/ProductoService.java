package com.duoc.mascotasordenes.service; // Paquete de la capa de lógica de negocio (servicio de productos)

import com.duoc.mascotasordenes.entity.Producto; // Entidad de dominio que representa un producto para mascotas
import com.duoc.mascotasordenes.exception.RecursoNoEncontradoException; // Excepción propia para errores 404
import com.duoc.mascotasordenes.repository.ProductoRepository; // Repositorio JPA de productos
import org.slf4j.Logger; // Interfaz estándar de logging en el ecosistema Spring
import org.slf4j.LoggerFactory; // Fábrica para obtener un Logger vinculado a esta clase
import org.springframework.stereotype.Service; // Registra el bean como componente de servicio en el contexto Spring
import org.springframework.transaction.annotation.Transactional; // Permite envolver métodos en una transacción

import java.util.List; // Colección devuelta en los listados

@Service // Spring crea una instancia única y la inyecta donde se necesite como servicio de negocio
public class ProductoService { // Fachada de negocio para operaciones con productos de mascotas

    private static final Logger log = LoggerFactory.getLogger(ProductoService.class); // Logger estático para registrar eventos y errores

    private final ProductoRepository productoRepository; // Dependencia al repositorio de productos (inmutable)

    public ProductoService(ProductoRepository productoRepository) { // Inyección de dependencias por constructor
        this.productoRepository = productoRepository; // Asigna el repositorio inyectado
    }

    @Transactional(readOnly = true) // Operación de solo lectura
    public List<Producto> obtenerTodos() { // Caso de uso: listar catálogo completo de productos
        log.info("Obteniendo catálogo completo de productos"); // Log INFO de la operación
        return productoRepository.findAll(); // SELECT * FROM PRODUCTO
    }

    @Transactional(readOnly = true) // Operación de solo lectura
    public Producto obtenerPorId(Long id) { // Caso de uso: obtener un producto específico por su id
        log.debug("Buscando producto con id={}", id); // Log DEBUG con el ID buscado
        return productoRepository.findById(id) // Busca por PK
                .orElseThrow(() -> new RecursoNoEncontradoException("El producto con ID " + id + " no existe")); // 404 si no existe
    }

    @Transactional(readOnly = true) // Operación de solo lectura
    public List<Producto> obtenerPorCategoria(String categoria) { // Caso de uso: filtrar productos por categoría
        log.info("Buscando productos de la categoría '{}'", categoria); // Log INFO con el filtro
        return productoRepository.findByCategoriaIgnoreCase(categoria); // Derivación de consulta de Spring Data
    }

    @Transactional(readOnly = true) // Operación de solo lectura
    public List<Producto> buscarPorNombre(String nombre) { // Caso de uso: búsqueda parcial por nombre
        log.info("Buscando productos que contengan '{}' en su nombre", nombre); // Log INFO con el texto buscado
        return productoRepository.findByNombreContainingIgnoreCase(nombre); // Derivación de consulta con LIKE %nombre%
    }

    @Transactional // Operación transaccional de escritura
    public Producto crear(Producto producto) { // Caso de uso: registrar un nuevo producto en el catálogo
        producto.setId(null); // Asegura que la secuencia Oracle genere el ID
        Producto guardado = productoRepository.save(producto); // INSERT en PRODUCTO
        log.info("Producto creado id={}, nombre='{}'", guardado.getId(), guardado.getNombre()); // Log INFO del producto creado
        return guardado; // Retorna la entidad persistida con ID
    }

    @Transactional // Operación transaccional de escritura (actualización completa)
    public Producto actualizar(Long id, Producto datosActualizados) { // Caso de uso: modificar un producto existente
        Producto existente = productoRepository.findById(id) // Busca el producto actual
                .orElseThrow(() -> new RecursoNoEncontradoException("El producto con ID " + id + " no existe")); // 404 si no existe
        existente.setNombre(datosActualizados.getNombre()); // Actualiza el nombre
        existente.setDescripcion(datosActualizados.getDescripcion()); // Actualiza la descripción
        existente.setPrecio(datosActualizados.getPrecio()); // Actualiza el precio
        existente.setCategoria(datosActualizados.getCategoria()); // Actualiza la categoría
        existente.setStock(datosActualizados.getStock()); // Actualiza el stock
        existente.setMarca(datosActualizados.getMarca()); // Actualiza la marca
        existente.setImagen(datosActualizados.getImagen()); // Actualiza la URL de imagen
        Producto actualizado = productoRepository.save(existente); // UPDATE en PRODUCTO
        log.info("Producto actualizado id={}", actualizado.getId()); // Log INFO
        return actualizado; // Retorna la entidad ya actualizada
    }

    @Transactional // Operación transaccional de escritura (eliminación)
    public void eliminar(Long id) { // Caso de uso: retirar un producto del catálogo
        if (!productoRepository.existsById(id)) { // Verifica existencia antes de eliminar
            throw new RecursoNoEncontradoException("El producto con ID " + id + " no existe"); // 404 si no existe
        }
        productoRepository.deleteById(id); // DELETE FROM PRODUCTO WHERE ID = ?
        log.warn("Producto eliminado id={}", id); // Log WARN: las eliminaciones merecen visibilidad
    }
}
