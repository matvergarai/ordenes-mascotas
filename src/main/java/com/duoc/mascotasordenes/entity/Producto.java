package com.duoc.mascotasordenes.entity; // Paquete donde vive el modelo de dominio (entidad producto para mascotas)

import jakarta.persistence.Column; // Mapea un atributo Java a una columna específica de la tabla
import jakarta.persistence.Entity; // Marca la clase como entidad JPA (se persiste en base de datos)
import jakarta.persistence.GeneratedValue; // Indica que el valor de la clave primaria se genera automáticamente
import jakarta.persistence.GenerationType; // Estrategia de generación (en Oracle usamos SEQUENCE)
import jakarta.persistence.Id; // Señala el atributo que actúa como clave primaria (PK)
import jakarta.persistence.SequenceGenerator; // Define el nombre de la secuencia Oracle que genera los IDs
import jakarta.persistence.Table; // Permite personalizar el nombre de la tabla en la base de datos
import jakarta.validation.constraints.NotBlank; // Valida que el campo no sea nulo, vacío ni solo espacios
import jakarta.validation.constraints.Pattern; // Valida que el campo cumpla una expresión regular
import jakarta.validation.constraints.PositiveOrZero; // Valida que el valor numérico sea cero o positivo
import jakarta.validation.constraints.Positive; // Valida que el valor numérico sea mayor que cero
import jakarta.validation.constraints.Size; // Valida el largo mínimo/máximo de una cadena

@Entity // Indica a Hibernate/JPA que esta clase se mapea a una tabla y se persiste en Oracle
@Table(name = "PRODUCTO") // Nombre explícito de la tabla: PRODUCTO (schema MASCOTAS_APP)
public class Producto { // Clase que representa un producto de la tienda de mascotas (POJO mapeado a BD)

    @Id // Este atributo es la clave primaria de la tabla PRODUCTO
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "producto_seq") // Oracle genera el ID desde una secuencia
    @SequenceGenerator(name = "producto_seq", sequenceName = "PRODUCTO_SEQ", allocationSize = 1) // Declara el nombre de la secuencia Oracle y su paso
    @Column(name = "ID") // Mapea a la columna ID de la tabla
    private Long id; // Identificador único del producto (clave primaria en persistencia)

    @NotBlank(message = "El nombre del producto es obligatorio") // No permite nulo, vacío ni solo espacios
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres") // Largo permitido
    @Column(name = "NOMBRE", nullable = false, length = 100) // Columna NOMBRE: no nula, máximo 100 caracteres
    private String nombre; // Nombre comercial del producto (ej: "Royal Canin Medium Adult")

    @Size(max = 300, message = "La descripción no puede superar 300 caracteres") // Largo máximo permitido
    @Column(name = "DESCRIPCION", length = 300) // Columna DESCRIPCION: máximo 300 caracteres
    private String descripcion; // Descripción detallada del producto

    @Positive(message = "El precio debe ser mayor a 0") // Solo acepta valores positivos
    @Column(name = "PRECIO", nullable = false) // Columna PRECIO: no nula (NUMBER en Oracle)
    private double precio; // Precio unitario del producto en pesos chilenos (CLP)

    @NotBlank(message = "La categoría es obligatoria") // No permite nulo, vacío ni solo espacios
    @Pattern(regexp = "PERRO|GATO|AVE|OTROS", message = "Categoría inválida. Valores permitidos: PERRO, GATO, AVE, OTROS") // Solo acepta categorías válidas (alineado con check constraint del schema.sql)
    @Column(name = "CATEGORIA", nullable = false, length = 20) // Columna CATEGORIA: no nula, máximo 20
    private String categoria; // Categoría del producto (PERRO, GATO, AVE, OTROS)

    @PositiveOrZero(message = "El stock no puede ser negativo") // Solo acepta 0 o positivos
    @Column(name = "STOCK", nullable = false) // Columna STOCK: no nula (NUMBER en Oracle)
    private int stock; // Cantidad disponible en inventario

    @Size(max = 50, message = "La marca no puede superar 50 caracteres") // Largo máximo permitido
    @Column(name = "MARCA", length = 50) // Columna MARCA: máximo 50 caracteres
    private String marca; // Marca o fabricante del producto

    @Size(max = 200, message = "La URL de la imagen no puede superar 200 caracteres") // Largo máximo permitido
    @Column(name = "IMAGEN", length = 200) // Columna IMAGEN: máximo 200 caracteres
    private String imagen; // URL de la imagen representativa del producto

    public Producto() { // Constructor sin argumentos (requerido por JPA y Jackson)
    }

    public Producto(Long id, String nombre, String descripcion, double precio, // Constructor con todos los campos
                    String categoria, int stock, String marca, String imagen) {
        this.id = id; // Asigna el id recibido al atributo de instancia
        this.nombre = nombre; // Asigna el nombre del producto
        this.descripcion = descripcion; // Asigna la descripción del producto
        this.precio = precio; // Asigna el precio unitario
        this.categoria = categoria; // Asigna la categoría del producto
        this.stock = stock; // Asigna la cantidad en stock
        this.marca = marca; // Asigna la marca del producto
        this.imagen = imagen; // Asigna la URL de la imagen
    }

    public Long getId() { // Getter: expone el valor de id (lectura)
        return id; // Devuelve el identificador almacenado
    }

    public void setId(Long id) { // Setter: permite modificar id desde fuera de la clase
        this.id = id; // Actualiza el campo id con el parámetro
    }

    public String getNombre() { // Getter: expone el nombre del producto
        return nombre; // Devuelve el nombre almacenado
    }

    public void setNombre(String nombre) { // Setter: permite modificar el nombre del producto
        this.nombre = nombre; // Actualiza el campo nombre con el parámetro
    }

    public String getDescripcion() { // Getter: expone la descripción del producto
        return descripcion; // Devuelve la descripción almacenada
    }

    public void setDescripcion(String descripcion) { // Setter: permite modificar la descripción
        this.descripcion = descripcion; // Actualiza el campo descripcion con el parámetro
    }

    public double getPrecio() { // Getter: expone el precio unitario del producto
        return precio; // Devuelve el precio almacenado
    }

    public void setPrecio(double precio) { // Setter: permite modificar el precio del producto
        this.precio = precio; // Actualiza el campo precio con el parámetro
    }

    public String getCategoria() { // Getter: expone la categoría del producto
        return categoria; // Devuelve la categoría almacenada
    }

    public void setCategoria(String categoria) { // Setter: permite modificar la categoría
        this.categoria = categoria; // Actualiza el campo categoria con el parámetro
    }

    public int getStock() { // Getter: expone la cantidad en stock
        return stock; // Devuelve el stock almacenado
    }

    public void setStock(int stock) { // Setter: permite modificar el stock del producto
        this.stock = stock; // Actualiza el campo stock con el parámetro
    }

    public String getMarca() { // Getter: expone la marca del producto
        return marca; // Devuelve la marca almacenada
    }

    public void setMarca(String marca) { // Setter: permite modificar la marca
        this.marca = marca; // Actualiza el campo marca con el parámetro
    }

    public String getImagen() { // Getter: expone la URL de la imagen del producto
        return imagen; // Devuelve la URL de imagen almacenada
    }

    public void setImagen(String imagen) { // Setter: permite modificar la URL de la imagen
        this.imagen = imagen; // Actualiza el campo imagen con el parámetro
    }
}
