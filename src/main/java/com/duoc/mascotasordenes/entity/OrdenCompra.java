package com.duoc.mascotasordenes.entity; // Paquete donde vive el modelo de dominio (entidad orden de compra)

import jakarta.validation.Valid; // Activa la validación en cascada para objetos anidados (detalles de la orden)
import jakarta.validation.constraints.Email; // Valida que el campo tenga formato de correo electrónico
import jakarta.validation.constraints.NotBlank; // Valida que el campo no sea nulo, vacío ni solo espacios
import jakarta.validation.constraints.NotEmpty; // Valida que la colección no sea nula ni vacía
import jakarta.validation.constraints.NotNull; // Valida que el campo no sea nulo
import jakarta.validation.constraints.Pattern; // Valida que el campo cumpla una expresión regular
import jakarta.validation.constraints.Positive; // Valida que el valor numérico sea mayor que cero

import java.util.List; // Interfaz de colección ordenada para almacenar los detalles de la orden

public class OrdenCompra { // Clase que representa una orden de compra de productos para mascotas (POJO / bean)

    private Long id; // Identificador único de la orden de compra (clave primaria)

    @NotBlank(message = "El nombre del cliente es obligatorio") // No permite nulo, vacío ni solo espacios
    private String clienteNombre; // Nombre completo del cliente que realiza la compra

    @NotBlank(message = "El email del cliente es obligatorio") // No permite nulo, vacío ni solo espacios
    @Email(message = "El email debe tener un formato válido") // Valida formato de correo electrónico
    private String clienteEmail; // Correo electrónico del cliente para contacto y notificaciones

    @NotBlank(message = "La fecha es obligatoria") // No permite nulo, vacío ni solo espacios
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "La fecha debe tener formato yyyy-MM-dd") // Valida formato ISO de fecha
    private String fecha; // Fecha en que se realizó la orden (formato ISO: yyyy-MM-dd)

    @NotBlank(message = "El estado es obligatorio") // No permite nulo, vacío ni solo espacios
    @Pattern(regexp = "PENDIENTE|PROCESANDO|ENVIADA|ENTREGADA|CANCELADA", message = "Estado inválido. Valores permitidos: PENDIENTE, PROCESANDO, ENVIADA, ENTREGADA, CANCELADA") // Solo acepta estados válidos del ciclo de vida de la orden
    private String estado; // Estado actual de la orden (PENDIENTE, PROCESANDO, ENVIADA, ENTREGADA, CANCELADA)

    @Positive(message = "El total debe ser mayor a 0") // Solo acepta valores positivos
    private double total; // Monto total de la orden en pesos chilenos (CLP)

    @NotBlank(message = "La dirección de envío es obligatoria") // No permite nulo, vacío ni solo espacios
    private String direccionEnvio; // Dirección de despacho del pedido

    @NotBlank(message = "El método de pago es obligatorio") // No permite nulo, vacío ni solo espacios
    private String metodoPago; // Método de pago utilizado (Tarjeta de Crédito, Débito, Transferencia Bancaria)

    @NotEmpty(message = "La orden debe tener al menos un detalle") // La lista no puede estar vacía ni ser nula
    @Valid // Activa validación en cascada: valida cada DetalleOrden dentro de la lista
    private List<DetalleOrden> detalles; // Lista de productos incluidos en esta orden con sus cantidades y subtotales

    public OrdenCompra() { // Constructor sin argumentos (requerido por frameworks como Jackson para deserialización JSON)
    }

    public OrdenCompra(Long id, String clienteNombre, String clienteEmail, String fecha, // Constructor con todos los campos para crear una orden completa
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
        this.detalles = detalles; // Asigna la lista de detalles (productos) de la orden
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
        this.detalles = detalles; // Actualiza el campo detalles con el parámetro
    }
}
