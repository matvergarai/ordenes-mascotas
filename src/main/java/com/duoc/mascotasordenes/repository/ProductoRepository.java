package com.duoc.mascotasordenes.repository; // Paquete de la capa de acceso a datos (repositorio de productos)

import com.duoc.mascotasordenes.entity.Producto; // Entidad JPA que se persiste/consulta
import org.springframework.data.jpa.repository.JpaRepository; // Interfaz base de Spring Data JPA con CRUD y paginación
import org.springframework.stereotype.Repository; // Registra la clase como componente de persistencia en el contexto Spring

import java.util.List; // Colección devuelta por los métodos de búsqueda

@Repository // Spring crea una instancia única del repositorio y la inyecta donde se necesite
public interface ProductoRepository extends JpaRepository<Producto, Long> { // JpaRepository provee findAll, findById, save, delete, etc.

    // Derivación de consulta: filtra productos por categoría sin distinguir mayúsculas/minúsculas
    List<Producto> findByCategoriaIgnoreCase(String categoria); // Retorna los productos de la categoría indicada (ALIMENTO, JUGUETE, etc.)

    // Derivación de consulta: búsqueda parcial por nombre sin distinguir mayúsculas/minúsculas
    List<Producto> findByNombreContainingIgnoreCase(String nombre); // Retorna productos cuyo nombre contenga el texto ingresado
}
