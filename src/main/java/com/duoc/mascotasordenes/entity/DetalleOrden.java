package com.duoc.mascotasordenes.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "DETALLE_ORDEN") // Línea de detalle de una orden de compra.
public class DetalleOrden {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "detalle_seq")
    @SequenceGenerator(name = "detalle_seq", sequenceName = "DETALLE_ORDEN_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)                                  // Lado dueño de la FK hacia ORDEN_COMPRA
    @JoinColumn(name = "ORDEN_ID", nullable = false)
    @JsonBackReference                                                                    // Lado hijo en la serialización JSON (evita bucles)
    private OrdenCompra orden;

    @NotNull(message = "El id del producto es obligatorio")
    @Column(name = "PRODUCTO_ID", nullable = false)                                       // FK lógica hacia PRODUCTO.ID
    private Long productoId;

    // Datos desnormalizados completados por el servicio (productoNombre, precioUnitario, subtotal):
    // el cliente solo envía productoId + cantidad; el OrdenCompraService los rellena desde el catálogo.
    @Column(name = "PRODUCTO_NOMBRE", nullable = false, length = 100)
    private String productoNombre;

    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    @Column(name = "CANTIDAD", nullable = false)
    private int cantidad;

    @Column(name = "PRECIO_UNITARIO", nullable = false)                                   // Precio en CLP al momento de la compra
    private double precioUnitario;

    @Column(name = "SUBTOTAL", nullable = false)                                          // cantidad × precioUnitario
    private double subtotal;

    public DetalleOrden() {                                                               // Constructor requerido por JPA y Jackson
    }

    public DetalleOrden(Long id, Long ordenId, Long productoId, String productoNombre,    // ordenId se mantiene por compatibilidad
                        int cantidad, double precioUnitario, double subtotal) {
        this.id = id;
        this.productoId = productoId;
        this.productoNombre = productoNombre;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public OrdenCompra getOrden() { return orden; }
    public void setOrden(OrdenCompra orden) { this.orden = orden; }

    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }

    public String getProductoNombre() { return productoNombre; }
    public void setProductoNombre(String productoNombre) { this.productoNombre = productoNombre; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
}
