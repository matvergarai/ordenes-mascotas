package com.duoc.mascotasordenes.entity; // Paquete donde vive el modelo de dominio (entidad producto para mascotas)

public class Producto { // Clase que representa un producto de la tienda de mascotas (POJO / bean)

    private Long id; // Identificador único del producto (clave primaria en persistencia)
    private String nombre; // Nombre comercial del producto (ej: "Royal Canin Medium Adult")
    private String descripcion; // Descripción detallada del producto
    private double precio; // Precio unitario del producto en pesos chilenos (CLP)
    private String categoria; // Categoría del producto (ALIMENTO, JUGUETE, HIGIENE, SALUD, ACCESORIOS)
    private int stock; // Cantidad disponible en inventario
    private String marca; // Marca o fabricante del producto
    private String imagen; // URL de la imagen representativa del producto

    public Producto() { // Constructor sin argumentos (requerido por frameworks como Jackson para deserialización JSON)
    }

    public Producto(Long id, String nombre, String descripcion, double precio, // Constructor con todos los campos para crear un producto completo
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
