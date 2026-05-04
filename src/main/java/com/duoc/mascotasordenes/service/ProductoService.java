package com.duoc.mascotasordenes.service;

import com.duoc.mascotasordenes.entity.Producto;
import com.duoc.mascotasordenes.exception.RecursoNoEncontradoException;
import com.duoc.mascotasordenes.repository.ProductoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductoService {

    private static final Logger log = LoggerFactory.getLogger(ProductoService.class);

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {                       // Inyección por constructor
        this.productoRepository = productoRepository;
    }

    @Transactional(readOnly = true)
    public List<Producto> obtenerTodos() {
        log.info("Obteniendo catálogo completo de productos");
        return productoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Producto obtenerPorId(Long id) {
        log.debug("Buscando producto con id={}", id);
        return productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("El producto con ID " + id + " no existe"));
    }

    @Transactional(readOnly = true)
    public List<Producto> obtenerPorCategoria(String categoria) {
        log.info("Buscando productos de la categoría '{}'", categoria);
        return productoRepository.findByCategoriaIgnoreCase(categoria);
    }

    @Transactional(readOnly = true)
    public List<Producto> buscarPorNombre(String nombre) {
        log.info("Buscando productos que contengan '{}' en su nombre", nombre);
        return productoRepository.findByNombreContainingIgnoreCase(nombre);              // LIKE %nombre%
    }

    @Transactional
    public Producto crear(Producto producto) {
        producto.setId(null);                                                            // El id lo asigna la secuencia Oracle
        Producto guardado = productoRepository.save(producto);
        log.info("Producto creado id={}, nombre='{}'", guardado.getId(), guardado.getNombre());
        return guardado;
    }

    @Transactional
    public Producto actualizar(Long id, Producto datosActualizados) {
        Producto existente = productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("El producto con ID " + id + " no existe"));
        existente.setNombre(datosActualizados.getNombre());
        existente.setDescripcion(datosActualizados.getDescripcion());
        existente.setPrecio(datosActualizados.getPrecio());
        existente.setCategoria(datosActualizados.getCategoria());
        existente.setStock(datosActualizados.getStock());
        existente.setMarca(datosActualizados.getMarca());
        existente.setImagen(datosActualizados.getImagen());
        Producto actualizado = productoRepository.save(existente);
        log.info("Producto actualizado id={}", actualizado.getId());
        return actualizado;
    }

    @Transactional
    public void eliminar(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("El producto con ID " + id + " no existe");
        }
        productoRepository.deleteById(id);
        log.warn("Producto eliminado id={}", id);                                        // WARN para destacar bajas en los logs
    }
}
