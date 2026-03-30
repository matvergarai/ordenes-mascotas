package com.duoc.mascotasordenes.entity; // Paquete donde vive el modelo de dominio (entidad detalle de orden)

import jakarta.validation.constraints.Min; // Valida que el valor numérico sea igual o mayor al mínimo indicado
import jakarta.validation.constraints.NotBlank; // Valida que el campo no sea nulo, vacío ni solo espacios
import jakarta.validation.constraints.NotNull; // Valida que el campo no sea nulo
import jakarta.validation.constraints.Positive; // Valida que el valor numérico sea mayor que cero

public class DetalleOrden { // Clase que representa una línea/ítem dentro de una orden de compra (POJO / bean)

    private Long id; // Identificador único del detalle de orden (clave primaria)
    private Long ordenId; // Identificador de la orden de compra a la que pertenece este detalle (clave foránea)

    @NotNull(message = "El id del producto es obligatorio") // No permite nulo
    private Long productoId; // Identificador del producto incluido en este detalle (clave foránea)

    @NotBlank(message = "El nombre del producto es obligatorio") // No permite nulo, vacío ni solo espacios
    private String productoNombre; // Nombre del producto (desnormalizado para facilitar consultas sin JOIN)

    @Min(value = 1, message = "La cantidad debe ser al menos 1") // Mínimo una unidad por línea
    private int cantidad; // Cantidad de unidades del producto en esta línea de la orden

    @Positive(message = "El precio unitario debe ser mayor a 0") // Solo acepta valores positivos
    private double precioUnitario; // Precio por unidad del producto al momento de la compra en CLP

    @Positive(message = "El subtotal debe ser mayor a 0") // Solo acepta valores positivos
    private double subtotal; // Subtotal de la línea: cantidad × precioUnitario

    public DetalleOrden() { // Constructor sin argumentos (requerido por frameworks como Jackson para deserialización JSON)
    }

    public DetalleOrden(Long id, Long ordenId, Long productoId, String productoNombre, // Constructor con todos los campos para crear un detalle completo
                        int cantidad, double precioUnitario, double subtotal) {
        this.id = id; // Asigna el id recibido al atributo de instancia
        this.ordenId = ordenId; // Asigna el id de la orden padre
        this.productoId = productoId; // Asigna el id del producto
        this.productoNombre = productoNombre; // Asigna el nombre del producto
        this.cantidad = cantidad; // Asigna la cantidad de unidades
        this.precioUnitario = precioUnitario; // Asigna el precio unitario
        this.subtotal = subtotal; // Asigna el subtotal calculado
    }

    public Long getId() { // Getter: expone el valor del id del detalle (lectura)
        return id; // Devuelve el identificador almacenado
    }

    public void setId(Long id) { // Setter: permite modificar el id desde fuera de la clase
        this.id = id; // Actualiza el campo id con el parámetro
    }

    public Long getOrdenId() { // Getter: expone el id de la orden a la que pertenece
        return ordenId; // Devuelve el identificador de la orden padre
    }

    public void setOrdenId(Long ordenId) { // Setter: permite modificar el id de la orden
        this.ordenId = ordenId; // Actualiza el campo ordenId con el parámetro
    }

    public Long getProductoId() { // Getter: expone el id del producto asociado
        return productoId; // Devuelve el identificador del producto
    }

    public void setProductoId(Long productoId) { // Setter: permite modificar el id del producto
        this.productoId = productoId; // Actualiza el campo productoId con el parámetro
    }

    public String getProductoNombre() { // Getter: expone el nombre del producto
        return productoNombre; // Devuelve el nombre del producto almacenado
    }

    public void setProductoNombre(String productoNombre) { // Setter: permite modificar el nombre del producto
        this.productoNombre = productoNombre; // Actualiza el campo productoNombre con el parámetro
    }

    public int getCantidad() { // Getter: expone la cantidad de unidades
        return cantidad; // Devuelve la cantidad almacenada
    }

    public void setCantidad(int cantidad) { // Setter: permite modificar la cantidad de unidades
        this.cantidad = cantidad; // Actualiza el campo cantidad con el parámetro
    }

    public double getPrecioUnitario() { // Getter: expone el precio unitario del producto
        return precioUnitario; // Devuelve el precio unitario almacenado
    }

    public void setPrecioUnitario(double precioUnitario) { // Setter: permite modificar el precio unitario
        this.precioUnitario = precioUnitario; // Actualiza el campo precioUnitario con el parámetro
    }

    public double getSubtotal() { // Getter: expone el subtotal de esta línea de la orden
        return subtotal; // Devuelve el subtotal almacenado (cantidad × precioUnitario)
    }

    public void setSubtotal(double subtotal) { // Setter: permite modificar el subtotal
        this.subtotal = subtotal; // Actualiza el campo subtotal con el parámetro
    }
}
