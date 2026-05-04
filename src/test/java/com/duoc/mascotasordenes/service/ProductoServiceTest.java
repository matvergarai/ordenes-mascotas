package com.duoc.mascotasordenes.service;

import com.duoc.mascotasordenes.entity.Producto;
import com.duoc.mascotasordenes.exception.RecursoNoEncontradoException;
import com.duoc.mascotasordenes.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // Activa el procesamiento de @Mock e @InjectMocks sin levantar contexto Spring.
@DisplayName("ProductoService - pruebas unitarias con Mockito")
class ProductoServiceTest {

    @Mock // Repositorio simulado: aísla el servicio de la base de datos real.
    private ProductoRepository productoRepository;

    @InjectMocks // Instancia del SUT con el mock inyectado por constructor.
    private ProductoService productoService;

    private Producto productoDePrueba;

    @BeforeEach // Reconstruye el dato de prueba antes de cada test para garantizar aislamiento.
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
    @DisplayName("obtenerPorId devuelve el producto cuando el id existe")
    void obtenerPorId_devuelveElProducto_cuandoElIdExiste() {
        when(productoRepository.findById(7L)).thenReturn(Optional.of(productoDePrueba)); // arrange: stub del repo

        Producto resultado = productoService.obtenerPorId(7L); // act: invoca el método bajo prueba

        assertThat(resultado).isNotNull();                                  // assert: contrato del método (no null)
        assertThat(resultado.getId()).isEqualTo(7L);                        // assert: identidad
        assertThat(resultado.getNombre()).isEqualTo("Croquetas Premium 3kg"); // assert: campo de negocio
        verify(productoRepository, times(1)).findById(7L);                  // assert: interacción esperada con el mock
    }

    @Test
    @DisplayName("obtenerPorId lanza RecursoNoEncontradoException cuando el id no existe")
    void obtenerPorId_lanzaExcepcion_cuandoElIdNoExiste() {
        when(productoRepository.findById(999L)).thenReturn(Optional.empty()); // arrange: simula "no existe"

        assertThatThrownBy(() -> productoService.obtenerPorId(999L))
                .isInstanceOf(RecursoNoEncontradoException.class) // assert: tipo exacto de excepción
                .hasMessageContaining("999");                     // assert: el mensaje incluye el id consultado

        verify(productoRepository, times(1)).findById(999L);
    }
}
