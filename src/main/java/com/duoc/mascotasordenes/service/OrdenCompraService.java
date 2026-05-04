package com.duoc.mascotasordenes.service;

import com.duoc.mascotasordenes.entity.DetalleOrden;
import com.duoc.mascotasordenes.entity.OrdenCompra;
import com.duoc.mascotasordenes.entity.Producto;
import com.duoc.mascotasordenes.exception.RecursoNoEncontradoException;
import com.duoc.mascotasordenes.exception.ReglaNegocioException;
import com.duoc.mascotasordenes.repository.OrdenCompraRepository;
import com.duoc.mascotasordenes.repository.ProductoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrdenCompraService {

    private static final Logger log = LoggerFactory.getLogger(OrdenCompraService.class);

    private final OrdenCompraRepository ordenCompraRepository;
    private final ProductoRepository productoRepository;                                  // Validación de existencia y stock

    public OrdenCompraService(OrdenCompraRepository ordenCompraRepository,
                              ProductoRepository productoRepository) {
        this.ordenCompraRepository = ordenCompraRepository;
        this.productoRepository = productoRepository;
    }

    @Transactional(readOnly = true)
    public List<OrdenCompra> obtenerTodas() {
        log.info("Obteniendo listado completo de órdenes de compra");
        return ordenCompraRepository.findAll();
    }

    @Transactional(readOnly = true)
    public OrdenCompra obtenerPorId(Long id) {                                            // Trae la orden con sus detalles (EAGER)
        log.debug("Buscando orden con id={}", id);
        return ordenCompraRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("La orden con ID " + id + " no existe"));
    }

    @Transactional(readOnly = true)
    public List<OrdenCompra> obtenerPorEstado(String estado) {
        log.info("Buscando órdenes con estado='{}'", estado);
        return ordenCompraRepository.findByEstadoIgnoreCase(estado);
    }

    @Transactional(readOnly = true)
    public List<OrdenCompra> obtenerPorCliente(String clienteNombre) {
        log.info("Buscando órdenes del cliente '{}'", clienteNombre);
        return ordenCompraRepository.findByClienteNombreContainingIgnoreCase(clienteNombre);
    }

    /**
     * Crea la orden validando catálogo y stock, recalcula totales/subtotales en el servidor
     * y descuenta stock. La cascada de OrdenCompra propaga el INSERT a DetalleOrden.
     */
    @Transactional
    public OrdenCompra crearOrden(OrdenCompra orden) {
        orden.setId(null);                                                                // El id lo asigna la secuencia Oracle
        double totalCalculado = 0.0;
        for (DetalleOrden detalle : orden.getDetalles()) {
            Producto producto = productoRepository.findById(detalle.getProductoId())
                    .orElseThrow(() -> new ReglaNegocioException(
                            "El producto con ID " + detalle.getProductoId() + " no existe"));
            if (producto.getStock() < detalle.getCantidad()) {
                throw new ReglaNegocioException("Stock insuficiente para el producto '"
                        + producto.getNombre() + "'. Disponible: " + producto.getStock()
                        + ", solicitado: " + detalle.getCantidad());
            }
            detalle.setId(null);
            detalle.setProductoNombre(producto.getNombre());                              // Snapshot desnormalizado
            detalle.setPrecioUnitario(producto.getPrecio());                              // Precio vigente al cierre de la orden
            detalle.setSubtotal(producto.getPrecio() * detalle.getCantidad());
            detalle.setOrden(orden);                                                      // Cierra la relación bidireccional
            producto.setStock(producto.getStock() - detalle.getCantidad());
            productoRepository.save(producto);
            totalCalculado += detalle.getSubtotal();
        }
        orden.setTotal(totalCalculado);                                                   // El total enviado por el cliente se ignora
        OrdenCompra guardada = ordenCompraRepository.save(orden);
        log.info("Orden creada id={}, cliente='{}', total={}",
                guardada.getId(), guardada.getClienteNombre(), guardada.getTotal());
        return guardada;
    }

    @Transactional
    public OrdenCompra actualizar(Long id, OrdenCompra datosActualizados) {               // Solo actualiza la cabecera
        OrdenCompra existente = ordenCompraRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("La orden con ID " + id + " no existe"));
        existente.setClienteNombre(datosActualizados.getClienteNombre());
        existente.setClienteEmail(datosActualizados.getClienteEmail());
        existente.setFecha(datosActualizados.getFecha());
        existente.setEstado(datosActualizados.getEstado());
        existente.setDireccionEnvio(datosActualizados.getDireccionEnvio());
        existente.setMetodoPago(datosActualizados.getMetodoPago());
        OrdenCompra actualizada = ordenCompraRepository.save(existente);
        log.info("Orden actualizada id={}", actualizada.getId());
        return actualizada;
    }

    @Transactional
    public OrdenCompra cambiarEstado(Long id, String nuevoEstado) {
        OrdenCompra orden = ordenCompraRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("La orden con ID " + id + " no existe"));
        if (!nuevoEstado.matches("PENDIENTE|CONFIRMADA|ENVIADA|ENTREGADA|CANCELADA")) {
            throw new ReglaNegocioException("Estado inválido. Valores permitidos: PENDIENTE, CONFIRMADA, ENVIADA, ENTREGADA, CANCELADA");
        }
        orden.setEstado(nuevoEstado);
        OrdenCompra guardada = ordenCompraRepository.save(orden);
        log.info("Estado de orden id={} cambiado a '{}'", id, nuevoEstado);
        return guardada;
    }

    @Transactional
    public void eliminar(Long id) {                                                       // Cascade ALL + orphanRemoval borra los detalles
        if (!ordenCompraRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("La orden con ID " + id + " no existe");
        }
        ordenCompraRepository.deleteById(id);
        log.warn("Orden eliminada id={}", id);
    }
}
