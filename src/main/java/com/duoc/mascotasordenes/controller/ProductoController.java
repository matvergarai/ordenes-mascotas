package com.duoc.mascotasordenes.controller;

import com.duoc.mascotasordenes.entity.Producto;
import com.duoc.mascotasordenes.service.ProductoService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private static final Logger log = LoggerFactory.getLogger(ProductoController.class);

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    // Envuelve la entidad en un EntityModel y le asocia los enlaces HATEOAS del recurso.
    private EntityModel<Producto> toModel(Producto producto) {
        return EntityModel.of(producto,
                linkTo(methodOn(ProductoController.class).obtenerPorId(producto.getId())).withSelfRel(),                            // self
                linkTo(methodOn(ProductoController.class).obtenerTodos()).withRel("productos"),                                     // colección
                linkTo(methodOn(ProductoController.class).obtenerPorCategoria(producto.getCategoria())).withRel("categoria"),       // productos de la misma categoría
                linkTo(methodOn(ProductoController.class).actualizar(producto.getId(), producto)).withRel("actualizar"),            // PUT
                linkTo(methodOn(ProductoController.class).eliminar(producto.getId())).withRel("eliminar"));                         // DELETE
    }

    @GetMapping // Catálogo completo envuelto en CollectionModel.
    public CollectionModel<EntityModel<Producto>> obtenerTodos() {
        log.debug("GET /api/productos");
        List<EntityModel<Producto>> productos = productoService.obtenerTodos().stream()
                .map(this::toModel)
                .toList();
        return CollectionModel.of(productos,
                linkTo(methodOn(ProductoController.class).obtenerTodos()).withSelfRel());
    }

    @GetMapping("/{id}") // Detalle por id; 404 si no existe.
    public EntityModel<Producto> obtenerPorId(@PathVariable Long id) {
        log.debug("GET /api/productos/{}", id);
        return toModel(productoService.obtenerPorId(id));
    }

    @GetMapping("/categoria/{categoria}") // Filtra el catálogo por categoría.
    public CollectionModel<EntityModel<Producto>> obtenerPorCategoria(@PathVariable String categoria) {
        log.debug("GET /api/productos/categoria/{}", categoria);
        List<EntityModel<Producto>> productos = productoService.obtenerPorCategoria(categoria).stream()
                .map(this::toModel)
                .toList();
        return CollectionModel.of(productos,
                linkTo(methodOn(ProductoController.class).obtenerPorCategoria(categoria)).withSelfRel(),
                linkTo(methodOn(ProductoController.class).obtenerTodos()).withRel("productos"));
    }

    @GetMapping("/buscar/{nombre}") // Búsqueda parcial por nombre (LIKE %nombre%).
    public CollectionModel<EntityModel<Producto>> buscarPorNombre(@PathVariable String nombre) {
        log.debug("GET /api/productos/buscar/{}", nombre);
        List<EntityModel<Producto>> productos = productoService.buscarPorNombre(nombre).stream()
                .map(this::toModel)
                .toList();
        return CollectionModel.of(productos,
                linkTo(methodOn(ProductoController.class).buscarPorNombre(nombre)).withSelfRel(),
                linkTo(methodOn(ProductoController.class).obtenerTodos()).withRel("productos"));
    }

    @PostMapping // Alta del producto; @Valid dispara las validaciones de la entidad.
    public ResponseEntity<EntityModel<Producto>> crear(@Valid @RequestBody Producto producto) {
        log.info("POST /api/productos - creando producto '{}'", producto.getNombre());
        Producto creado = productoService.crear(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(toModel(creado));
    }

    @PutMapping("/{id}") // Actualización completa del producto.
    public EntityModel<Producto> actualizar(@PathVariable Long id, @Valid @RequestBody Producto producto) {
        log.info("PUT /api/productos/{}", id);
        return toModel(productoService.actualizar(id, producto));
    }

    @DeleteMapping("/{id}") // Baja del producto; responde 204.
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.warn("DELETE /api/productos/{}", id);
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
