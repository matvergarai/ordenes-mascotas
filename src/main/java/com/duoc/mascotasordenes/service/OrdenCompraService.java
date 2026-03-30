package com.duoc.mascotasordenes.service; // Paquete de la capa de lógica de negocio (servicio de órdenes de compra)

import com.duoc.mascotasordenes.entity.OrdenCompra; // Modelo de dominio que representa una orden de compra
import com.duoc.mascotasordenes.repository.OrdenCompraRepository; // Abstracción del origen de datos de órdenes
import org.springframework.stereotype.Service; // Registra el bean como componente de servicio en el contexto Spring

import java.util.List; // Interfaz de colección ordenada para devolver listas de órdenes
import java.util.Optional; // Contenedor que puede tener o no un valor (evita null)

@Service // Spring crea una instancia única y la inyecta donde se necesite como servicio de negocio
public class OrdenCompraService { // Fachada de negocio para operaciones con órdenes de compra de productos para mascotas

    private final OrdenCompraRepository ordenCompraRepository; // Dependencia al repositorio de órdenes (inmutable)

    public OrdenCompraService(OrdenCompraRepository ordenCompraRepository) { // Inyección de dependencias por constructor (patrón recomendado por Spring)
        this.ordenCompraRepository = ordenCompraRepository; // Asigna el repositorio inyectado por Spring al campo de instancia
    }

    public List<OrdenCompra> obtenerTodas() { // Caso de uso: listar todas las órdenes de compra
        return ordenCompraRepository.findAll(); // Delegación directa al repositorio
    }

    public Optional<OrdenCompra> obtenerPorId(Long id) { // Caso de uso: obtener una orden específica por su id
        return ordenCompraRepository.findById(id); // Delegación al repositorio; retorna Optional vacío si no existe
    }

    public List<OrdenCompra> obtenerPorEstado(String estado) { // Caso de uso: filtrar órdenes por estado (PENDIENTE, PROCESANDO, ENVIADA, etc.)
        return ordenCompraRepository.findByEstado(estado); // Delegación al repositorio con filtro por estado
    }

    public List<OrdenCompra> obtenerPorCliente(String clienteNombre) { // Caso de uso: buscar órdenes cuyo cliente contenga el nombre ingresado
        return ordenCompraRepository.findByClienteNombreContaining(clienteNombre); // Delegación al repositorio con búsqueda parcial por nombre de cliente
    }

    public OrdenCompra crearOrden(OrdenCompra orden) { // Caso de uso: registrar una nueva orden de compra
        return ordenCompraRepository.save(orden); // Delega la persistencia al repositorio y retorna la orden con id asignado
    }
}
