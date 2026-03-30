package com.duoc.mascotasordenes.service; // Paquete de la capa de lógica de negocio (servicio de productos)

import com.duoc.mascotasordenes.entity.Producto; // Modelo de dominio que representa un producto para mascotas
import com.duoc.mascotasordenes.repository.ProductoRepository; // Abstracción del origen de datos de productos
import org.springframework.stereotype.Service; // Registra el bean como componente de servicio en el contexto Spring

import java.util.List; // Interfaz de colección ordenada para devolver listas de productos
import java.util.Optional; // Contenedor que puede tener o no un valor (evita null)

@Service // Spring crea una instancia única y la inyecta donde se necesite como servicio de negocio
public class ProductoService { // Fachada de negocio para operaciones con productos de mascotas

    private final ProductoRepository productoRepository; // Dependencia al repositorio de productos (inmutable)

    public ProductoService(ProductoRepository productoRepository) { // Inyección de dependencias por constructor (patrón recomendado por Spring)
        this.productoRepository = productoRepository; // Asigna el repositorio inyectado por Spring al campo de instancia
    }

    public List<Producto> obtenerTodos() { // Caso de uso: listar catálogo completo de productos
        return productoRepository.findAll(); // Delegación directa al repositorio
    }

    public Optional<Producto> obtenerPorId(Long id) { // Caso de uso: obtener un producto específico por su id
        return productoRepository.findById(id); // Delegación al repositorio; retorna Optional vacío si no existe
    }

    public List<Producto> obtenerPorCategoria(String categoria) { // Caso de uso: filtrar productos por categoría (ALIMENTO, JUGUETE, etc.)
        return productoRepository.findByCategoria(categoria); // Delegación al repositorio con filtro por categoría
    }

    public List<Producto> buscarPorNombre(String nombre) { // Caso de uso: buscar productos cuyo nombre contenga el texto ingresado
        return productoRepository.findByNombreContaining(nombre); // Delegación al repositorio con búsqueda parcial por nombre
    }
}
