package com.duoc.mascotasordenes.service; // Paquete de la capa de lógica de negocio (servicio de órdenes de compra)

import com.duoc.mascotasordenes.entity.DetalleOrden; // Entidad hija que conforma cada línea de la orden
import com.duoc.mascotasordenes.entity.OrdenCompra; // Entidad principal: orden de compra
import com.duoc.mascotasordenes.entity.Producto; // Entidad: producto del catálogo
import com.duoc.mascotasordenes.exception.RecursoNoEncontradoException; // Excepción propia para errores 404
import com.duoc.mascotasordenes.exception.ReglaNegocioException; // Excepción propia para errores 400
import com.duoc.mascotasordenes.repository.OrdenCompraRepository; // Repositorio JPA de órdenes
import com.duoc.mascotasordenes.repository.ProductoRepository; // Repositorio JPA de productos (para validaciones cruzadas)
import org.slf4j.Logger; // Interfaz estándar de logging en el ecosistema Spring
import org.slf4j.LoggerFactory; // Fábrica para obtener un Logger vinculado a esta clase
import org.springframework.stereotype.Service; // Registra el bean como componente de servicio en el contexto Spring
import org.springframework.transaction.annotation.Transactional; // Permite envolver métodos en una transacción

import java.util.List; // Colección devuelta en los listados

@Service // Spring crea una instancia única y la inyecta donde se necesite como servicio de negocio
public class OrdenCompraService { // Fachada de negocio para operaciones con órdenes de compra

    private static final Logger log = LoggerFactory.getLogger(OrdenCompraService.class); // Logger estático

    private final OrdenCompraRepository ordenCompraRepository; // Repositorio JPA de órdenes
    private final ProductoRepository productoRepository; // Repositorio JPA de productos (validación de existencia y stock)

    public OrdenCompraService(OrdenCompraRepository ordenCompraRepository, // Inyección por constructor
                              ProductoRepository productoRepository) {
        this.ordenCompraRepository = ordenCompraRepository; // Asigna el repositorio de órdenes
        this.productoRepository = productoRepository; // Asigna el repositorio de productos
    }

    @Transactional(readOnly = true) // Operación de solo lectura
    public List<OrdenCompra> obtenerTodas() { // Caso de uso: listar todas las órdenes
        log.info("Obteniendo listado completo de órdenes de compra"); // Log INFO
        return ordenCompraRepository.findAll(); // SELECT * FROM ORDEN_COMPRA
    }

    @Transactional(readOnly = true) // Operación de solo lectura
    public OrdenCompra obtenerPorId(Long id) { // Caso de uso: detalle de una orden (incluye sus DetalleOrden por EAGER fetch)
        log.debug("Buscando orden con id={}", id); // Log DEBUG con el ID buscado
        return ordenCompraRepository.findById(id) // Busca por PK
                .orElseThrow(() -> new RecursoNoEncontradoException("La orden con ID " + id + " no existe")); // 404 si no existe
    }

    @Transactional(readOnly = true) // Operación de solo lectura
    public List<OrdenCompra> obtenerPorEstado(String estado) { // Caso de uso: filtrar órdenes por estado
        log.info("Buscando órdenes con estado='{}'", estado); // Log INFO con el estado filtrado
        return ordenCompraRepository.findByEstadoIgnoreCase(estado); // Derivación de consulta sin distinguir mayúsculas
    }

    @Transactional(readOnly = true) // Operación de solo lectura
    public List<OrdenCompra> obtenerPorCliente(String clienteNombre) { // Caso de uso: búsqueda parcial por nombre de cliente
        log.info("Buscando órdenes del cliente '{}'", clienteNombre); // Log INFO con el texto buscado
        return ordenCompraRepository.findByClienteNombreContainingIgnoreCase(clienteNombre); // Derivación con LIKE %nombre%
    }

    @Transactional // Operación transaccional de escritura
    public OrdenCompra crearOrden(OrdenCompra orden) { // Caso de uso: registrar una nueva orden con sus detalles
        orden.setId(null); // Asegura que la secuencia Oracle genere el ID de la orden
        double totalCalculado = 0.0; // Acumulador para recalcular el total basado en los detalles
        for (DetalleOrden detalle : orden.getDetalles()) { // Recorre cada línea de la orden
            Producto producto = productoRepository.findById(detalle.getProductoId()) // Verifica existencia del producto
                    .orElseThrow(() -> new ReglaNegocioException("El producto con ID " // 400 si no existe
                            + detalle.getProductoId() + " no existe")); // Mensaje con el ID
            if (producto.getStock() < detalle.getCantidad()) { // Regla de negocio: stock suficiente
                throw new ReglaNegocioException("Stock insuficiente para el producto '" // 400
                        + producto.getNombre() + "'. Disponible: " + producto.getStock() // Stock actual
                        + ", solicitado: " + detalle.getCantidad()); // Cantidad pedida
            }
            detalle.setId(null); // Asegura que la secuencia Oracle genere el ID del detalle
            detalle.setProductoNombre(producto.getNombre()); // Desnormaliza el nombre actual del producto
            detalle.setPrecioUnitario(producto.getPrecio()); // Toma el precio vigente al momento de crear la orden
            detalle.setSubtotal(producto.getPrecio() * detalle.getCantidad()); // Calcula el subtotal (precio × cantidad)
            detalle.setOrden(orden); // Asocia el detalle con la orden padre (relación JPA bidireccional)
            producto.setStock(producto.getStock() - detalle.getCantidad()); // Descuenta el stock del producto
            productoRepository.save(producto); // Persiste la actualización del stock
            totalCalculado += detalle.getSubtotal(); // Acumula el subtotal al total de la orden
        }
        orden.setTotal(totalCalculado); // Reemplaza el total enviado por el total calculado por el servidor
        OrdenCompra guardada = ordenCompraRepository.save(orden); // INSERT en ORDEN_COMPRA y en DETALLE_ORDEN (por cascade ALL)
        log.info("Orden creada id={}, cliente='{}', total={}", // Log INFO con resumen de la orden
                guardada.getId(), guardada.getClienteNombre(), guardada.getTotal());
        return guardada; // Retorna la orden persistida con IDs asignados
    }

    @Transactional // Operación transaccional de escritura (actualización de cabecera de orden)
    public OrdenCompra actualizar(Long id, OrdenCompra datosActualizados) { // Caso de uso: modificar datos generales de una orden
        OrdenCompra existente = ordenCompraRepository.findById(id) // Busca la orden actual
                .orElseThrow(() -> new RecursoNoEncontradoException("La orden con ID " + id + " no existe")); // 404 si no existe
        existente.setClienteNombre(datosActualizados.getClienteNombre()); // Actualiza el nombre del cliente
        existente.setClienteEmail(datosActualizados.getClienteEmail()); // Actualiza el email del cliente
        existente.setFecha(datosActualizados.getFecha()); // Actualiza la fecha
        existente.setEstado(datosActualizados.getEstado()); // Actualiza el estado (PENDIENTE, PROCESANDO, ENVIADA, etc.)
        existente.setDireccionEnvio(datosActualizados.getDireccionEnvio()); // Actualiza la dirección
        existente.setMetodoPago(datosActualizados.getMetodoPago()); // Actualiza el método de pago
        OrdenCompra actualizada = ordenCompraRepository.save(existente); // UPDATE en ORDEN_COMPRA
        log.info("Orden actualizada id={}", actualizada.getId()); // Log INFO
        return actualizada; // Retorna la orden actualizada
    }

    @Transactional // Operación transaccional de escritura (cambio de estado)
    public OrdenCompra cambiarEstado(Long id, String nuevoEstado) { // Caso de uso: cambiar solo el estado de una orden
        OrdenCompra orden = ordenCompraRepository.findById(id) // Busca la orden actual
                .orElseThrow(() -> new RecursoNoEncontradoException("La orden con ID " + id + " no existe")); // 404 si no existe
        if (!nuevoEstado.matches("PENDIENTE|CONFIRMADA|ENVIADA|ENTREGADA|CANCELADA")) { // Valida el estado
            throw new ReglaNegocioException("Estado inválido. Valores permitidos: PENDIENTE, CONFIRMADA, ENVIADA, ENTREGADA, CANCELADA"); // 400
        }
        orden.setEstado(nuevoEstado); // Aplica el nuevo estado
        OrdenCompra guardada = ordenCompraRepository.save(orden); // UPDATE en ORDEN_COMPRA
        log.info("Estado de orden id={} cambiado a '{}'", id, nuevoEstado); // Log INFO
        return guardada; // Retorna la orden con estado actualizado
    }

    @Transactional // Operación transaccional de escritura (eliminación con cascada a detalles)
    public void eliminar(Long id) { // Caso de uso: eliminar una orden y sus detalles (por CascadeType.ALL + orphanRemoval)
        if (!ordenCompraRepository.existsById(id)) { // Verifica existencia antes de eliminar
            throw new RecursoNoEncontradoException("La orden con ID " + id + " no existe"); // 404 si no existe
        }
        ordenCompraRepository.deleteById(id); // DELETE FROM ORDEN_COMPRA (y en cascada DETALLE_ORDEN)
        log.warn("Orden eliminada id={}", id); // Log WARN
    }
}
