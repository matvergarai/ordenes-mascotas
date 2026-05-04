package com.duoc.mascotasordenes.repository;

import com.duoc.mascotasordenes.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByCategoriaIgnoreCase(String categoria);              // Filtro exacto por categoría (PERRO/GATO/AVE/OTROS)

    List<Producto> findByNombreContainingIgnoreCase(String nombre);          // Búsqueda parcial por nombre
}
