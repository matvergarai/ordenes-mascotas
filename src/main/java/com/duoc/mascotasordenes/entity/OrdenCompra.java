package com.duoc.mascotasordenes.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ORDEN_COMPRA") // Mapea la entidad a la tabla ORDEN_COMPRA en Oracle.
public class OrdenCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orden_seq")
    @SequenceGenerator(name = "orden_seq", sequenceName = "ORDEN_COMPRA_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @NotBlank(message = "El nombre del cliente es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre del cliente debe tener entre 2 y 100 caracteres")
    @Column(name = "CLIENTE_NOMBRE", nullable = false, length = 100)
    private String clienteNombre;

    @NotBlank(message = "El email del cliente es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    @Size(max = 100, message = "El email no puede superar 100 caracteres")
    @Column(name = "CLIENTE_EMAIL", nullable = false, length = 100)
    private String clienteEmail;

    @NotBlank(message = "La fecha es obligatoria")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "La fecha debe tener formato yyyy-MM-dd")
    @Column(name = "FECHA", nullable = false, length = 10)                                  // ISO yyyy-MM-dd
    private String fecha;

    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "PENDIENTE|CONFIRMADA|ENVIADA|ENTREGADA|CANCELADA", message = "Estado inválido. Valores permitidos: PENDIENTE, CONFIRMADA, ENVIADA, ENTREGADA, CANCELADA") // Coincide con el check constraint del schema.sql
    @Column(name = "ESTADO", nullable = false, length = 15)
    private String estado;

    @PositiveOrZero(message = "El total no puede ser negativo")
    @Column(name = "TOTAL", nullable = false)                                               // Total recalculado en el servicio
    private double total;

    @NotBlank(message = "La dirección de envío es obligatoria")
    @Size(max = 150, message = "La dirección no puede superar 150 caracteres")
    @Column(name = "DIRECCION_ENVIO", nullable = false, length = 150)
    private String direccionEnvio;

    @NotBlank(message = "El método de pago es obligatorio")
    @Size(max = 40, message = "El método de pago no puede superar 40 caracteres")
    @Column(name = "METODO_PAGO", nullable = false, length = 40)
    private String metodoPago;

    @NotEmpty(message = "La orden debe tener al menos un detalle")
    @Valid                                                                                  // Validación en cascada sobre cada DetalleOrden
    @OneToMany(
            mappedBy = "orden",                                                             // Lado dueño de la FK: DetalleOrden.orden
            cascade = CascadeType.ALL,                                                      // Persist/remove/merge se propagan a los detalles
            orphanRemoval = true,                                                           // Detalles fuera de la lista se eliminan en BD
            fetch = FetchType.EAGER
    )
    @JsonManagedReference                                                                   // Lado padre en la serialización JSON (evita bucles)
    private List<DetalleOrden> detalles = new ArrayList<>();

    public OrdenCompra() {                                                                  // Constructor requerido por JPA y Jackson
    }

    public OrdenCompra(Long id, String clienteNombre, String clienteEmail, String fecha,
                       String estado, double total, String direccionEnvio, String metodoPago,
                       List<DetalleOrden> detalles) {
        this.id = id;
        this.clienteNombre = clienteNombre;
        this.clienteEmail = clienteEmail;
        this.fecha = fecha;
        this.estado = estado;
        this.total = total;
        this.direccionEnvio = direccionEnvio;
        this.metodoPago = metodoPago;
        this.detalles = detalles != null ? detalles : new ArrayList<>();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getClienteNombre() { return clienteNombre; }
    public void setClienteNombre(String clienteNombre) { this.clienteNombre = clienteNombre; }

    public String getClienteEmail() { return clienteEmail; }
    public void setClienteEmail(String clienteEmail) { this.clienteEmail = clienteEmail; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public String getDireccionEnvio() { return direccionEnvio; }
    public void setDireccionEnvio(String direccionEnvio) { this.direccionEnvio = direccionEnvio; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public List<DetalleOrden> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleOrden> detalles) {
        this.detalles = detalles != null ? detalles : new ArrayList<>();
    }
}
