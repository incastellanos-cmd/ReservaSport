package com.reservasport.canchas.service;

import com.reservasport.canchas.dto.CanchaRequest;
import com.reservasport.canchas.dto.CanchaResponse;
import com.reservasport.canchas.exception.CanchaNoEncontradaException;
import com.reservasport.canchas.exception.NombreCanchaDuplicadoException;
import com.reservasport.canchas.model.Cancha;
import com.reservasport.canchas.repository.CanchaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CanchaServiceImplTest {

    @Mock
    private CanchaRepository canchaRepository;

    @InjectMocks
    private CanchaServiceImpl canchaService;

    private CanchaRequest request;
    private Cancha cancha;

    @BeforeEach
    void setUp() {

        request = new CanchaRequest();
        request.setNombre("Cancha Central");
        request.setTipo("Fútbol");
        request.setPrecioHora(new BigDecimal("25000.00"));

        cancha = Cancha.builder()
                .id(1L)
                .nombre("Cancha Central")
                .tipo("Fútbol")
                .precioHora(new BigDecimal("25000.00"))
                .disponible(true)
                .fechaCreacion(LocalDateTime.now())
                .build();
    }

    @Test
    void crearCancha_deberiaCrearCanchaCorrectamente() {

        // Given
        when(canchaRepository.existsByNombreIgnoreCase(request.getNombre()))
                .thenReturn(false);

        when(canchaRepository.save(any(Cancha.class)))
                .thenReturn(cancha);

        // When
        CanchaResponse response = canchaService.crearCancha(request);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Cancha Central", response.getNombre());
        assertEquals("Fútbol", response.getTipo());
        assertEquals(new BigDecimal("25000.00"), response.getPrecioHora());
        assertTrue(response.getDisponible());

        verify(canchaRepository)
                .existsByNombreIgnoreCase(request.getNombre());

        verify(canchaRepository)
                .save(any(Cancha.class));
    }

    @Test
    void crearCancha_deberiaLanzarExcepcionCuandoNombreExiste() {

        // Given
        when(canchaRepository.existsByNombreIgnoreCase(request.getNombre()))
                .thenReturn(true);

        // When & Then
        NombreCanchaDuplicadoException exception = assertThrows(
                NombreCanchaDuplicadoException.class,
                () -> canchaService.crearCancha(request)
        );

        assertEquals(
                "Ya existe una cancha con ese nombre",
                exception.getMessage()
        );

        verify(canchaRepository)
                .existsByNombreIgnoreCase(request.getNombre());

        verify(canchaRepository, never())
                .save(any(Cancha.class));
    }

    @Test
    void obtenerCanchas_deberiaRetornarListaDeCanchas() {

        // Given
        when(canchaRepository.findAll())
                .thenReturn(List.of(cancha));

        // When
        List<CanchaResponse> respuestas =
                canchaService.obtenerCanchas();

        // Then
        assertNotNull(respuestas);
        assertEquals(1, respuestas.size());
        assertEquals(1L, respuestas.get(0).getId());
        assertEquals("Cancha Central", respuestas.get(0).getNombre());
        assertEquals("Fútbol", respuestas.get(0).getTipo());

        verify(canchaRepository).findAll();
    }

    @Test
    void obtenerCanchaPorId_deberiaRetornarCancha() {

        // Given
        when(canchaRepository.findById(1L))
                .thenReturn(Optional.of(cancha));

        // When
        CanchaResponse response =
                canchaService.obtenerCanchaPorId(1L);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Cancha Central", response.getNombre());
        assertEquals("Fútbol", response.getTipo());
        assertTrue(response.getDisponible());

        verify(canchaRepository).findById(1L);
    }

    @Test
    void obtenerCanchaPorId_deberiaLanzarExcepcionCuandoNoExiste() {

        // Given
        when(canchaRepository.findById(99L))
                .thenReturn(Optional.empty());

        // When & Then
        CanchaNoEncontradaException exception = assertThrows(
                CanchaNoEncontradaException.class,
                () -> canchaService.obtenerCanchaPorId(99L)
        );

        assertEquals(
                "Cancha no encontrada",
                exception.getMessage()
        );

        verify(canchaRepository).findById(99L);
    }

    @Test
    void actualizarCancha_deberiaActualizarCanchaCorrectamente() {

        // Given
        request.setNombre("Cancha Renovada");
        request.setTipo("Tenis");
        request.setPrecioHora(new BigDecimal("30000.00"));

        when(canchaRepository.findById(1L))
                .thenReturn(Optional.of(cancha));

        when(canchaRepository.existsByNombreIgnoreCase("Cancha Renovada"))
                .thenReturn(false);

        when(canchaRepository.save(any(Cancha.class)))
                .thenAnswer(invocacion -> invocacion.getArgument(0));

        // When
        CanchaResponse response =
                canchaService.actualizarCancha(1L, request);

        // Then
        assertNotNull(response);
        assertEquals("Cancha Renovada", response.getNombre());
        assertEquals("Tenis", response.getTipo());
        assertEquals(
                new BigDecimal("30000.00"),
                response.getPrecioHora()
        );

        verify(canchaRepository).findById(1L);

        verify(canchaRepository)
                .existsByNombreIgnoreCase("Cancha Renovada");

        verify(canchaRepository)
                .save(any(Cancha.class));
    }

    @Test
    void actualizarCancha_deberiaLanzarExcepcionCuandoNombreExiste() {

        // Given
        request.setNombre("Cancha Norte");

        when(canchaRepository.findById(1L))
                .thenReturn(Optional.of(cancha));

        when(canchaRepository.existsByNombreIgnoreCase("Cancha Norte"))
                .thenReturn(true);

        // When & Then
        NombreCanchaDuplicadoException exception = assertThrows(
                NombreCanchaDuplicadoException.class,
                () -> canchaService.actualizarCancha(1L, request)
        );

        assertEquals(
                "Ya existe una cancha con ese nombre",
                exception.getMessage()
        );

        verify(canchaRepository).findById(1L);

        verify(canchaRepository)
                .existsByNombreIgnoreCase("Cancha Norte");

        verify(canchaRepository, never())
                .save(any(Cancha.class));
    }

    @Test
    void actualizarCancha_deberiaLanzarExcepcionCuandoNoExiste() {

        // Given
        when(canchaRepository.findById(99L))
                .thenReturn(Optional.empty());

        // When & Then
        CanchaNoEncontradaException exception = assertThrows(
                CanchaNoEncontradaException.class,
                () -> canchaService.actualizarCancha(99L, request)
        );

        assertEquals(
                "Cancha no encontrada",
                exception.getMessage()
        );

        verify(canchaRepository).findById(99L);

        verify(canchaRepository, never())
                .save(any(Cancha.class));
    }

    @Test
    void eliminarCancha_deberiaEliminarCanchaCorrectamente() {

        // Given
        when(canchaRepository.findById(1L))
                .thenReturn(Optional.of(cancha));

        // When
        canchaService.eliminarCancha(1L);

        // Then
        verify(canchaRepository).findById(1L);
        verify(canchaRepository).delete(cancha);
    }

    @Test
    void eliminarCancha_deberiaLanzarExcepcionCuandoNoExiste() {

        // Given
        when(canchaRepository.findById(99L))
                .thenReturn(Optional.empty());

        // When & Then
        CanchaNoEncontradaException exception = assertThrows(
                CanchaNoEncontradaException.class,
                () -> canchaService.eliminarCancha(99L)
        );

        assertEquals(
                "Cancha no encontrada",
                exception.getMessage()
        );

        verify(canchaRepository).findById(99L);

        verify(canchaRepository, never())
                .delete(any(Cancha.class));
    }
}
