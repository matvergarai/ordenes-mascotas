package com.duoc.mascotasordenes.controller;

import com.duoc.mascotasordenes.entity.Producto;
import com.duoc.mascotasordenes.service.ProductoService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductoController.class)         // Carga solo el slice MVC del controller (sin JPA ni servicios reales).
@WithMockUser(roles = "USER")                 // Inyecta principal autenticado con rol USER (evita 401 de Spring Security).
@DisplayName("ProductoController - pruebas de la capa REST y verificación de enlaces HATEOAS")
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;                  // Cliente HTTP simulado para invocar los endpoints.

    @MockBean
    private ProductoService productoService;  // Reemplaza el bean real por un mock dentro del contexto de test.

    private Producto productoDePrueba;

    @BeforeEach
    void inicializarDatos() {
        productoDePrueba = new Producto(
                7L,
                "Croquetas Premium 3kg",
                "Alimento balanceado para perros adultos",
                15990.0,
                "Alimento",
                50,
                "PetFood",
                "https://example.com/img/croquetas.png");
    }

    @Test
    @DisplayName("GET /api/productos/{id} retorna 200 y los datos del producto")
    void obtenerPorId_retorna200ConDatosDelProducto() throws Exception {
        when(productoService.obtenerPorId(7L)).thenReturn(productoDePrueba); // stub del servicio

        mockMvc.perform(get("/api/productos/{id}", 7L))
                .andExpect(status().isOk())                                        // contrato HTTP
                .andExpect(jsonPath("$.id").value(7))                              // contrato del payload
                .andExpect(jsonPath("$.nombre").value("Croquetas Premium 3kg"))
                .andExpect(jsonPath("$.categoria").value("Alimento"));
    }

    @Test
    @DisplayName("GET /api/productos/{id} incluye los enlaces HATEOAS (_links) requeridos")
    void obtenerPorId_incluyeEnlacesHateoas() throws Exception {
        when(productoService.obtenerPorId(7L)).thenReturn(productoDePrueba);

        mockMvc.perform(get("/api/productos/{id}", 7L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self.href").exists())                                                                // self existe
                .andExpect(jsonPath("$._links.self.href", Matchers.containsString("/api/productos/7")))                            // self apunta al recurso
                .andExpect(jsonPath("$._links.productos.href").exists())                                                           // colección
                .andExpect(jsonPath("$._links.categoria.href", Matchers.containsString("/api/productos/categoria/Alimento")))      // filtrado relacionado
                .andExpect(jsonPath("$._links.actualizar.href").exists())                                                          // PUT
                .andExpect(jsonPath("$._links.eliminar.href").exists());                                                           // DELETE
    }
}
