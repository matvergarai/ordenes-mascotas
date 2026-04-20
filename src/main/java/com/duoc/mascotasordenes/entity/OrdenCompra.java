package com.duoc.mascotasordenes.entity; // Paquete donde vive el modelo de dominio (entidad orden de compra)

import com.fasterxml.jackson.annotation.JsonManagedReference; // Evita bucles infinitos al serializar relaciones bidireccionales (lado padre)
import jakarta.persistence.CascadeType; // Define las operaciones que se propagan a las entidades relacionadas (persist, remove, etc.)
import jakarta.persistence.Column; // Mapea un atributo Java a una columna específica de la tabla
import jakarta.persistence.Entity; // Marca la clase como entidad JPA (se persiste en base de datos)
import jakarta.persistence.FetchType; // Estrategia de carga de la relación (LAZY = perezosa, EAGER = inmediata)
import jakarta.persistence.GeneratedValue; // Indica que el valor de la clave primaria se genera automáticamente
import jakarta.persistence.GenerationType; // Estrategia de generación (en Oracle usamos SEQUENCE)
import jakarta.persistence.Id; // Señala el atributo que actúa como clave primaria (PK)
import jakarta.persistence.JoinColumn; // Define la columna de clave foránea en la tabla hija (DETALLE_ORDEN)
import jakarta.persistence.OneToMany; // Define una relación uno-a-muchos (una orden tiene muchos detalles)
import jakarta.persistence.SequenceGenerator; // Define el nombre de la secuencia Oracle que genera los IDs
import jakarta.persistence.Table; // Permite personalizar el nombre de la tabla en la base de datos
import jakarta.validation.Valid; // Activa la validación en cascada para objetos anidados (detalles de la orden)
import jakarta.validation.constraints.Email; // Valida que el campo tenga formato de correo electrónico
import jakarta.validation.constraints.NotBlank; // Valida que el campo no sea nulo, vacío ni solo espacios
import jakarta.validation.constraints.NotEmpty; // Valida que la colección no sea nula ni vacía
import jakarta.validation.constraints.Pattern; // Valida que el campo cumpla una expresión regular
import jakarta.validation.constraints.PositiveOrZero; // Valida que el valor numérico sea cero o positivo
import jakarta.validation.constraints.Size; // Valida el largo mínimo/máximo de una cadena

import java.util.ArrayList; // Lista redimensionable para inicializar la colección de detalles
import java.util.List; // Interfaz de colección ordenada para almacenar los detalles de la orden

@Entity // Indica a Hibernate/JPA que esta clase se mapea a una tabla y se persiste en Oracle
@Table(name = "ORDEN_COMPRA") // Nombre explícito de la tabla: ORDEN_COMPRA (schema MASCOTAS_APP)
public class OrdenCompra { // Clase que representa una orden de compra de productos para mascotas (POJO mapeado a BD)

    @Id // Este atributo es la clave primaria de la tabla ORDEN_COMPRA
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orden_seq") // Oracle genera el ID desde una secuencia
    @SequenceGenerator(name = "orden_seq", sequenceName = "ORDEN_COMPRA_SEQ", allocationSize = 1) // Declara el nombre de la secuencia Oracle y su paso
    @Column(name = "ID") // Mapea a la columna ID de la tabla
    private Long id; // Identificador único de la orden de compra (clave primaria)

    @NotBlank(message = "El nombre del cliente es obligatorio") // No permite nulo, vacío ni solo espacios
    @Size(min = 2, max = 100, message = "El nombre del cliente debe tener entre 2 y 100 caracteres") // Largo permitido
    @Column(name = "CLIENTE_NOMBRE", nullable = false, length = 100) // Columna CLIENTE_NOMBRE: no nula, máximo 100
    private String clienteNombre; // Nombre completo del cliente que realiza la compra

    @NotBlank(message = "El email del cliente es obligatorio") // No permite nulo, vacío ni solo espacios
    @Email(message = "El email debe tener un formato válido") // Valida formato de correo electrónico
    @Size(max = 100, message = "El email no puede superar 100 caracteres") // Largo máximo permitido
    @Column(name = "CLIENTE_EMAIL", nullable = false, length = 100) // Columna CLIENTE_EMAIL: no nula, máximo 100
    private String clienteEmail; // Correo electrónico del cliente para contacto y notificaciones

    @NotBlank(message = "La fecha es obligatoria") // No permite nulo, vacío ni solo espacios
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "La fecha debe tener formato yyyy-MM-dd") // Valida formato ISO
    @Column(name = "FECHA", nullable = false, length = 10) // Columna FECHA: no nula, 10 caracteres (yyyy-MM-dd)
    private String fecha; // Fecha en que se realizó la orden (formato ISO: yyyy-MM-dd)

    @NotBlank(message = "El estado es obligatorio") // No permite nulo, vacío ni solo espacios
    @Pattern(regexp = "PENDIENTE|CONFIRMADA|ENVIADA|ENTREGADA|CANCELADA", message = "Estado inválido. Valores permitidos: PENDIENTE, CONFIRMADA, ENVIADA, ENTREGADA, CANCELADA") // Solo acepta estados válidos (alineado con check constraint del schema.sql)
    @Column(name = "ESTADO", nullable = false, length = 15) // Columna ESTADO: no nula, máximo 15 caracteres
    private String estado; // Estado actual de la orden (PENDIENTE, CONFIRMADA, ENVIADA, ENTREGADA, CANCELADA)

    @PositiveOrZero(message = "El total no puede ser negativo") // Solo acepta 0 o valores positivos
    @Column(name = "TOTAL", nullable = false) // Columna TOTAL: no nula (NUMBER en Oracle)
    private double total; // Monto total de la orden en pesos chilenos (CLP)

    @NotBlank(message = "La dirección de envío es obligatoria") // No permite nulo, vacío ni solo espacios
    @Size(max = 150, message = "La dirección no puede superar 150 caracteres") // Largo máximo permitido
    @Column(name = "DIRECCION_ENVIO", nullable = false, length = 150) // Columna DIRECCION_ENVIO: no nula, máximo 150
    private String direccionEnvio; // Dirección de despacho del pedido

    @NotBlank(message = "El método de pago es obligatorio") // No permite nulo, vacío ni solo espacios
    @Size(max = 40, message = "El método de pago no puede superar 40 caracteres") // Largo máximo permitido
    @Column(name = "METODO_PAGO", nullable = false, length = 40) // Columna METODO_PAGO: no nula, máximo 40
    private String metodoPago; // Método de pago utilizado (Tarjeta de Crédito, Débito, Transferencia Bancaria)

    @NotEmpty(message = "La orden debe tener al menos un detalle") // La lista no puede estar vacía ni ser nula
    @Valid // Activa validación en cascada: valida cada DetalleOrden dentro de la lista
    @OneToMany( // Relación uno-a-muchos bidireccional: una orden tiene varios detalles
            mappedBy = "orden", // El lado dueño de la FK es DetalleOrden.orden (DetalleOrden escribe la columna ORDEN_ID)
            cascade = CascadeType.ALL, // Propaga persist/remove/merge a los detalles automáticamente
            orphanRemoval = true, // Si un detalle se quita de la lista, también se elimina en la BD
            fetch = FetchType.EAGER // Carga los detalles junto con la orden en la misma consulta
    )
    @JsonManagedReference // Marca este lado como "padre" de la relación para evitar bucles infinitos al serializar a JSON
    private List<DetalleOrden> detalles = new ArrayList<>(); // Lista de productos incluidos en esta orden con sus cantidades y subtotales

    public OrdenCompra() { // Constructor sin argumentos (requerido por JPA y Jackson)
    }

    public OrdenCompra(Long id, String clienteNombre, String clienteEmail, String fecha, // Constructor con todos los campos
                       String estado, double total, String direccionEnvio, String metodoPago,
                       List<DetalleOrden> detalles) {
        this.id = id; // Asigna el id recibido al atributo de instancia
        this.clienteNombre = clienteNombre; // Asigna el nombre del cliente
        this.clienteEmail = clienteEmail; // Asigna el correo electrónico del cliente
        this.fecha = fecha; // Asigna la fecha de la orden
        this.estado = estado; // Asigna el estado actual de la orden
        this.total = total; // Asigna el monto total de la compra
        this.direccionEnvio = direccionEnvio; // Asigna la dirección de envío
        this.metodoPago = metodoPago; // Asigna el método de pago
        this.detalles = detalles != null ? detalles : new ArrayList<>(); // Asigna la lista de detalles (nunca nula)
    }

    public Long getId() { // Getter: expone el valor del id de la orden (lectura)
        return id; // Devuelve el identificador almacenado
    }

    public void setId(Long id) { // Setter: permite modificar el id desde fuera de la clase
        this.id = id; // Actualiza el campo id con el parámetro
    }

    public String getClienteNombre() { // Getter: expone el nombre del cliente
        return clienteNombre; // Devuelve el nombre del cliente almacenado
    }

    public void setClienteNombre(String clienteNombre) { // Setter: permite modificar el nombre del cliente
        this.clienteNombre = clienteNombre; // Actualiza el campo clienteNombre con el parámetro
    }

    public String getClienteEmail() { // Getter: expone el correo electrónico del cliente
        return clienteEmail; // Devuelve el email del cliente almacenado
    }

    public void setClienteEmail(String clienteEmail) { // Setter: permite modificar el email del cliente
        this.clienteEmail = clienteEmail; // Actualiza el campo clienteEmail con el parámetro
    }

    public String getFecha() { // Getter: expone la fecha de la orden
        return fecha; // Devuelve la fecha almacenada
    }

    public void setFecha(String fecha) { // Setter: permite modificar la fecha de la orden
        this.fecha = fecha; // Actualiza el campo fecha con el parámetro
    }

    public String getEstado() { // Getter: expone el estado actual de la orden
        return estado; // Devuelve el estado almacenado
    }

    public void setEstado(String estado) { // Setter: permite modificar el estado de la orden
        this.estado = estado; // Actualiza el campo estado con el parámetro
    }

    public double getTotal() { // Getter: expone el monto total de la orden
        return total; // Devuelve el total almacenado
    }

    public void setTotal(double total) { // Setter: permite modificar el total de la orden
        this.total = total; // Actualiza el campo total con el parámetro
    }

    public String getDireccionEnvio() { // Getter: expone la dirección de envío
        return direccionEnvio; // Devuelve la dirección de envío almacenada
    }

    public void setDireccionEnvio(String direccionEnvio) { // Setter: permite modificar la dirección de envío
        this.direccionEnvio = direccionEnvio; // Actualiza el campo direccionEnvio con el parámetro
    }

    public String getMetodoPago() { // Getter: expone el método de pago utilizado
        return metodoPago; // Devuelve el método de pago almacenado
    }

    public void setMetodoPago(String metodoPago) { // Setter: permite modificar el método de pago
        this.metodoPago = metodoPago; // Actualiza el campo metodoPago con el parámetro
    }

    public List<DetalleOrden> getDetalles() { // Getter: expone la lista de detalles de la orden
        return detalles; // Devuelve la lista de detalles (productos con cantidades y subtotales)
    }

    public void setDetalles(List<DetalleOrden> detalles) { // Setter: permite modificar la lista de detalles
        this.detalles = detalles != null ? detalles : new ArrayList<>(); // Asigna la lista (nunca nula)
    }
}
