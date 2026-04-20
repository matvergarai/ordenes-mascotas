package com.duoc.mascotasordenes.repository; // Paquete de la capa de acceso a datos (repositorio de órdenes de compra)

import com.duoc.mascotasordenes.entity.OrdenCompra; // Entidad JPA que se persiste/consulta
import org.springframework.data.jpa.repository.JpaRepository; // Interfaz base de Spring Data JPA con CRUD y paginación
import org.springframework.stereotype.Repository; // Registra la clase como componente de persistencia en el contexto Spring

import java.util.List; // Colección devuelta por los métodos de búsqueda

@Repository // Spring crea una instancia única del repositorio y la inyecta donde se necesite
public interface OrdenCompraRepository extends JpaRepository<OrdenCompra, Long> { // JpaRepository provee findAll, findById, save, delete, etc.

    // Derivación de consulta: filtra órdenes por estado sin distinguir mayúsculas/minúsculas
    List<OrdenCompra> findByEstadoIgnoreCase(String estado); // Retorna órdenes con el estado indicado (PENDIENTE, PROCESANDO, ENVIADA, ENTREGADA, CANCELADA)

    // Derivación de consulta: búsqueda parcial por nombre de cliente sin distinguir mayúsculas/minúsculas
    List<OrdenCompra> findByClienteNombreContainingIgnoreCase(String clienteNombre); // Retorna órdenes cuyo nombre de cliente contenga el texto ingresado
}
