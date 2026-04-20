package com.duoc.mascotasordenes.entity; // Paquete donde vive el modelo de dominio (entidad detalle de orden)

import com.fasterxml.jackson.annotation.JsonBackReference; // Evita bucles infinitos al serializar relaciones bidireccionales (lado hijo)
import jakarta.persistence.Column; // Mapea un atributo Java a una columna específica de la tabla
import jakarta.persistence.Entity; // Marca la clase como entidad JPA (se persiste en base de datos)
import jakarta.persistence.FetchType; // Estrategia de carga de la relación (LAZY = perezosa)
import jakarta.persistence.GeneratedValue; // Indica que el valor de la clave primaria se genera automáticamente
import jakarta.persistence.GenerationType; // Estrategia de generación (en Oracle usamos SEQUENCE)
import jakarta.persistence.Id; // Señala el atributo que actúa como clave primaria (PK)
import jakarta.persistence.JoinColumn; // Define la columna de clave foránea en esta entidad hija
import jakarta.persistence.ManyToOne; // Define una relación muchos-a-uno (muchos detalles pertenecen a una orden)
import jakarta.persistence.SequenceGenerator; // Define el nombre de la secuencia Oracle que genera los IDs
import jakarta.persistence.Table; // Permite personalizar el nombre de la tabla en la base de datos
import jakarta.validation.constraints.Min; // Valida que el valor numérico sea igual o mayor al mínimo indicado
import jakarta.validation.constraints.NotNull; // Valida que el campo no sea nulo

@Entity // Indica a Hibernate/JPA que esta clase se mapea a una tabla y se persiste en Oracle
@Table(name = "DETALLE_ORDEN") // Nombre explícito de la tabla: DETALLE_ORDEN (schema MASCOTAS_APP)
public class DetalleOrden { // Clase que representa una línea/ítem dentro de una orden de compra (POJO mapeado a BD)

    @Id // Este atributo es la clave primaria de la tabla DETALLE_ORDEN
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "detalle_seq") // Oracle genera el ID desde una secuencia
    @SequenceGenerator(name = "detalle_seq", sequenceName = "DETALLE_ORDEN_SEQ", allocationSize = 1) // Declara el nombre de la secuencia Oracle y su paso
    @Column(name = "ID") // Mapea a la columna ID de la tabla
    private Long id; // Identificador único del detalle de orden (clave primaria)

    @ManyToOne(fetch = FetchType.LAZY, optional = false) // Relación muchos-a-uno: muchos detalles pertenecen a una única orden
    @JoinColumn(name = "ORDEN_ID", nullable = false) // Columna FK hacia ORDEN_COMPRA.ID; este lado es el dueño de la FK
    @JsonBackReference // Marca este lado como "hijo" de la relación para evitar bucles infinitos al serializar a JSON
    private OrdenCompra orden; // Orden de compra a la que pertenece este detalle (relación JPA)

    @NotNull(message = "El id del producto es obligatorio") // No permite nulo
    @Column(name = "PRODUCTO_ID", nullable = false) // Columna PRODUCTO_ID: no nula (clave foránea lógica hacia PRODUCTO.ID)
    private Long productoId; // Identificador del producto incluido en este detalle (clave foránea)

    // productoNombre, precioUnitario y subtotal NO llevan Bean Validation porque el cliente solo envía productoId + cantidad;
    // el OrdenCompraService los rellena desde el catálogo de productos antes de persistir.
    // La integridad a nivel de BD queda garantizada por @Column(nullable = false).
    @Column(name = "PRODUCTO_NOMBRE", nullable = false, length = 100) // Columna PRODUCTO_NOMBRE: no nula, máximo 100
    private String productoNombre; // Nombre del producto (desnormalizado para facilitar consultas sin JOIN); lo calcula el service

    @Min(value = 1, message = "La cantidad debe ser al menos 1") // Mínimo una unidad por línea
    @Column(name = "CANTIDAD", nullable = false) // Columna CANTIDAD: no nula (NUMBER en Oracle)
    private int cantidad; // Cantidad de unidades del producto en esta línea de la orden

    @Column(name = "PRECIO_UNITARIO", nullable = false) // Columna PRECIO_UNITARIO: no nula (NUMBER en Oracle)
    private double precioUnitario; // Precio por unidad al momento de la compra en CLP; lo calcula el service

    @Column(name = "SUBTOTAL", nullable = false) // Columna SUBTOTAL: no nula (NUMBER en Oracle)
    private double subtotal; // Subtotal de la línea: cantidad × precioUnitario; lo calcula el service

    public DetalleOrden() { // Constructor sin argumentos (requerido por JPA y Jackson)
    }

    public DetalleOrden(Long id, Long ordenId, Long productoId, String productoNombre, // Constructor compatible con el formato previo (se conserva ordenId por compatibilidad)
                        int cantidad, double precioUnitario, double subtotal) {
        this.id = id; // Asigna el id recibido al atributo de instancia
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

    public OrdenCompra getOrden() { // Getter: expone la orden padre (lado inverso de la relación)
        return orden; // Devuelve la orden a la que pertenece este detalle
    }

    public void setOrden(OrdenCompra orden) { // Setter: permite asociar el detalle a una orden
        this.orden = orden; // Actualiza el campo orden con el parámetro
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
