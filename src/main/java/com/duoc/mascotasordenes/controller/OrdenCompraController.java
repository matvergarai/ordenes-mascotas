package com.duoc.mascotasordenes.controller;

import com.duoc.mascotasordenes.entity.OrdenCompra;
import com.duoc.mascotasordenes.service.OrdenCompraService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/ordenes")
public class OrdenCompraController {

    private static final Logger log = LoggerFactory.getLogger(OrdenCompraController.class);

    private final OrdenCompraService ordenCompraService;

    public OrdenCompraController(OrdenCompraService ordenCompraService) {
        this.ordenCompraService = ordenCompraService;
    }

    // Envuelve la entidad en un EntityModel y le asocia los enlaces HATEOAS del recurso.
    private EntityModel<OrdenCompra> toModel(OrdenCompra orden) {
        return EntityModel.of(orden,
                linkTo(methodOn(OrdenCompraController.class).obtenerPorId(orden.getId())).withSelfRel(),                                  // self
                linkTo(methodOn(OrdenCompraController.class).obtenerTodas()).withRel("ordenes"),                                          // colección
                linkTo(methodOn(OrdenCompraController.class).obtenerPorCliente(orden.getClienteNombre())).withRel("cliente"),             // otras órdenes del cliente
                linkTo(methodOn(OrdenCompraController.class).obtenerPorEstado(orden.getEstado())).withRel("estado"),                      // otras órdenes en el mismo estado
                // methodOn solo usa la firma para resolver la URL: el body Map.of() no se evalúa.
                linkTo(methodOn(OrdenCompraController.class).cambiarEstado(orden.getId(), Map.of())).withRel("cambiar-estado"),           // PATCH parcial
                linkTo(methodOn(OrdenCompraController.class).actualizar(orden.getId(), orden)).withRel("actualizar"),                     // PUT
                linkTo(methodOn(OrdenCompraController.class).eliminar(orden.getId())).withRel("eliminar"));                               // DELETE
    }

    @PostMapping // Crea la orden con sus detalles; el servicio valida stock y calcula el total.
    public ResponseEntity<EntityModel<OrdenCompra>> crearOrden(@Valid @RequestBody OrdenCompra orden) {
        log.info("POST /api/ordenes - cliente='{}', items={}", orden.getClienteNombre(), orden.getDetalles().size());
        OrdenCompra nuevaOrden = ordenCompraService.crearOrden(orden);
        return ResponseEntity.status(HttpStatus.CREATED).body(toModel(nuevaOrden));
    }

    @GetMapping // Listado completo de órdenes envuelto en CollectionModel.
    public CollectionModel<EntityModel<OrdenCompra>> obtenerTodas() {
        log.debug("GET /api/ordenes");
        List<EntityModel<OrdenCompra>> ordenes = ordenCompraService.obtenerTodas().stream()
                .map(this::toModel)
                .toList();
        return CollectionModel.of(ordenes,
                linkTo(methodOn(OrdenCompraController.class).obtenerTodas()).withSelfRel());
    }

    @GetMapping("/{id}") // Detalle de la orden por id; 404 si no existe.
    public EntityModel<OrdenCompra> obtenerPorId(@PathVariable Long id) {
        log.debug("GET /api/ordenes/{}", id);
        return toModel(ordenCompraService.obtenerPorId(id));
    }

    @GetMapping("/estado/{estado}") // Filtra las órdenes por estado (PENDIENTE, PAGADA, etc.).
    public CollectionModel<EntityModel<OrdenCompra>> obtenerPorEstado(@PathVariable String estado) {
        log.debug("GET /api/ordenes/estado/{}", estado);
        List<EntityModel<OrdenCompra>> ordenes = ordenCompraService.obtenerPorEstado(estado).stream()
                .map(this::toModel)
                .toList();
        return CollectionModel.of(ordenes,
                linkTo(methodOn(OrdenCompraController.class).obtenerPorEstado(estado)).withSelfRel(),
                linkTo(methodOn(OrdenCompraController.class).obtenerTodas()).withRel("ordenes"));
    }

    @GetMapping("/cliente/{clienteNombre}") // Filtra las órdenes por cliente.
    public CollectionModel<EntityModel<OrdenCompra>> obtenerPorCliente(@PathVariable String clienteNombre) {
        log.debug("GET /api/ordenes/cliente/{}", clienteNombre);
        List<EntityModel<OrdenCompra>> ordenes = ordenCompraService.obtenerPorCliente(clienteNombre).stream()
                .map(this::toModel)
                .toList();
        return CollectionModel.of(ordenes,
                linkTo(methodOn(OrdenCompraController.class).obtenerPorCliente(clienteNombre)).withSelfRel(),
                linkTo(methodOn(OrdenCompraController.class).obtenerTodas()).withRel("ordenes"));
    }

    @PutMapping("/{id}") // Actualización completa de la cabecera de la orden.
    public EntityModel<OrdenCompra> actualizar(@PathVariable Long id, @Valid @RequestBody OrdenCompra orden) {
        log.info("PUT /api/ordenes/{}", id);
        return toModel(ordenCompraService.actualizar(id, orden));
    }

    @PatchMapping("/{id}/estado") // Cambio parcial: solo actualiza el campo "estado".
    public EntityModel<OrdenCompra> cambiarEstado(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String nuevoEstado = body.get("estado");
        log.info("PATCH /api/ordenes/{}/estado - nuevo estado='{}'", id, nuevoEstado);
        return toModel(ordenCompraService.cambiarEstado(id, nuevoEstado));
    }

    @DeleteMapping("/{id}") // Baja de la orden con cascada a sus detalles; responde 204.
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.warn("DELETE /api/ordenes/{}", id);
        ordenCompraService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
