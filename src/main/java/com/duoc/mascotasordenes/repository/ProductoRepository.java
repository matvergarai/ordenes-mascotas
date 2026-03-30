package com.duoc.mascotasordenes.repository; // Paquete de la capa de acceso a datos (repositorio de productos)

import com.duoc.mascotasordenes.entity.Producto; // Modelo de dominio que persiste/consulta este repositorio
import jakarta.annotation.PostConstruct; // Marca un método a ejecutar tras construir el bean de Spring
import org.springframework.stereotype.Repository; // Registra la clase como componente de persistencia en el contexto Spring

import java.util.ArrayList; // Lista redimensionable para almacenar productos en memoria
import java.util.List; // Interfaz de colección ordenada
import java.util.Optional; // Contenedor que puede tener o no un valor (evita null)
import java.util.stream.Collectors; // Utilidad para recopilar resultados de un stream en una colección

@Repository // Spring crea una instancia única (singleton) y la inyecta donde se necesite
public class ProductoRepository { // Implementación en memoria del repositorio de productos (no usa base de datos)

    private final List<Producto> productos = new ArrayList<>(); // Lista mutable de productos; la referencia es final pero el contenido puede cambiar

    @PostConstruct // Se ejecuta una vez después del constructor y de inyectar dependencias
    public void init() { // Carga datos de ejemplo (productos para mascotas) al arrancar la aplicación
        productos.add(new Producto(1L, "Royal Canin Medium Adult", // Producto 1: alimento para perros adultos de raza mediana
                "Alimento seco para perros adultos de raza mediana, saco de 15 kg",
                45990, "ALIMENTO", 25, "Royal Canin",
                "https://ejemplo.com/img/royal-canin-medium.jpg"));
        productos.add(new Producto(2L, "Pro Plan Puppy Razas Pequeñas", // Producto 2: alimento para cachorros de razas pequeñas
                "Alimento para cachorros de razas pequeñas con pollo, 7.5 kg",
                38990, "ALIMENTO", 18, "Pro Plan",
                "https://ejemplo.com/img/proplan-puppy.jpg"));
        productos.add(new Producto(3L, "Pelota Kong Classic Roja", // Producto 3: juguete resistente para perros
                "Juguete de goma resistente para perros, tamaño mediano, ideal para rellenar con premios",
                15990, "JUGUETE", 40, "Kong",
                "https://ejemplo.com/img/kong-classic.jpg"));
        productos.add(new Producto(4L, "Ratón con Catnip Felino", // Producto 4: juguete interactivo para gatos
                "Juguete interactivo de ratón con hierba gatera para gatos",
                5990, "JUGUETE", 60, "Catit",
                "https://ejemplo.com/img/raton-catnip.jpg"));
        productos.add(new Producto(5L, "Shampoo Antipulgas para Perros", // Producto 5: shampoo medicado para perros
                "Shampoo medicado antipulgas y garrapatas, 500 ml, apto para cachorros desde 3 meses",
                8990, "HIGIENE", 35, "Pet Clean",
                "https://ejemplo.com/img/shampoo-antipulgas.jpg"));
        productos.add(new Producto(6L, "Antiparasitario Interno Canino", // Producto 6: desparasitante para perros
                "Tabletas desparasitantes para perros de 10 a 20 kg, caja de 2 comprimidos",
                12990, "SALUD", 50, "Bayer Drontal",
                "https://ejemplo.com/img/drontal-perro.jpg"));
        productos.add(new Producto(7L, "Collar Ajustable de Nylon", // Producto 7: collar para perros medianos
                "Collar resistente de nylon con hebilla ajustable para perros medianos, varios colores",
                7990, "ACCESORIOS", 45, "Rogz",
                "https://ejemplo.com/img/collar-nylon.jpg"));
        productos.add(new Producto(8L, "Cama Ortopédica para Perros", // Producto 8: cama con espuma viscoelástica
                "Cama con espuma viscoelástica para perros grandes, funda lavable, 90x70 cm",
                34990, "ACCESORIOS", 12, "PetSafe",
                "https://ejemplo.com/img/cama-ortopedica.jpg"));
        productos.add(new Producto(9L, "Arena Sanitaria para Gatos", // Producto 9: arena aglomerante para gatos
                "Arena aglomerante con control de olores, bolsa de 10 kg",
                9990, "HIGIENE", 55, "Ever Clean",
                "https://ejemplo.com/img/arena-gatos.jpg"));
        productos.add(new Producto(10L, "Pipeta Antipulgas Gatos", // Producto 10: pipeta spot-on para gatos
                "Pipeta spot-on antipulgas y garrapatas para gatos adultos, 3 unidades",
                18990, "SALUD", 30, "Frontline",
                "https://ejemplo.com/img/pipeta-gatos.jpg"));
    }

    public List<Producto> findAll() { // Devuelve todos los productos almacenados
        return List.copyOf(productos); // Retorna una copia inmutable para proteger la lista interna
    }

    public Optional<Producto> findById(Long id) { // Busca un producto por su identificador único
        return productos.stream() // Convierte la lista en un flujo (stream) para operaciones funcionales
                .filter(p -> p.getId().equals(id)) // Conserva solo el elemento cuyo id coincide con el buscado
                .findFirst(); // Obtiene el primero que cumple el filtro, o Optional vacío si no existe
    }

    public List<Producto> findByCategoria(String categoria) { // Busca productos que pertenezcan a una categoría específica
        return productos.stream() // Convierte la lista en un flujo para filtrado
                .filter(p -> p.getCategoria().equalsIgnoreCase(categoria)) // Filtra ignorando mayúsculas/minúsculas
                .collect(Collectors.toList()); // Recopila los resultados en una nueva lista
    }

    public List<Producto> findByNombreContaining(String nombre) { // Busca productos cuyo nombre contenga el texto dado
        return productos.stream() // Convierte la lista en un flujo para búsqueda parcial
                .filter(p -> p.getNombre().toLowerCase().contains(nombre.toLowerCase())) // Filtra por coincidencia parcial sin distinguir mayúsculas
                .collect(Collectors.toList()); // Recopila los resultados en una nueva lista
    }
}
