package com.duoc.mascotasordenes.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "PRODUCTO") // Mapea la entidad a la tabla PRODUCTO en Oracle.
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "producto_seq")
    @SequenceGenerator(name = "producto_seq", sequenceName = "PRODUCTO_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Column(name = "NOMBRE", nullable = false, length = 100)
    private String nombre;

    @Size(max = 300, message = "La descripción no puede superar 300 caracteres")
    @Column(name = "DESCRIPCION", length = 300)
    private String descripcion;

    @Positive(message = "El precio debe ser mayor a 0")
    @Column(name = "PRECIO", nullable = false)                                            // Precio unitario en CLP
    private double precio;

    @NotBlank(message = "La categoría es obligatoria")
    @Pattern(regexp = "PERRO|GATO|AVE|OTROS", message = "Categoría inválida. Valores permitidos: PERRO, GATO, AVE, OTROS") // Coincide con el check constraint del schema.sql
    @Column(name = "CATEGORIA", nullable = false, length = 20)
    private String categoria;

    @PositiveOrZero(message = "El stock no puede ser negativo")
    @Column(name = "STOCK", nullable = false)
    private int stock;

    @Size(max = 50, message = "La marca no puede superar 50 caracteres")
    @Column(name = "MARCA", length = 50)
    private String marca;

    @Size(max = 200, message = "La URL de la imagen no puede superar 200 caracteres")
    @Column(name = "IMAGEN", length = 200)
    private String imagen;

    public Producto() {                                                                   // Constructor requerido por JPA y Jackson
    }

    public Producto(Long id, String nombre, String descripcion, double precio,
                    String categoria, int stock, String marca, String imagen) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoria = categoria;
        this.stock = stock;
        this.marca = marca;
        this.imagen = imagen;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }
}
