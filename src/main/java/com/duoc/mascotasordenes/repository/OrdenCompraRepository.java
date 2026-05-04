package com.duoc.mascotasordenes.repository;

import com.duoc.mascotasordenes.entity.OrdenCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdenCompraRepository extends JpaRepository<OrdenCompra, Long> {

    List<OrdenCompra> findByEstadoIgnoreCase(String estado);                              // Filtro por estado del flujo

    List<OrdenCompra> findByClienteNombreContainingIgnoreCase(String clienteNombre);      // Búsqueda parcial por cliente
}
