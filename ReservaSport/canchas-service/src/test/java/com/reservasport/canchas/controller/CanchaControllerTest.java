package com.reservasport.canchas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reservasport.canchas.dto.CanchaRequest;
import com.reservasport.canchas.dto.CanchaResponse;
import com.reservasport.canchas.exception.CanchaNoEncontradaException;
import com.reservasport.canchas.exception.GlobalExceptionHandler;
import com.reservasport.canchas.exception.NombreCanchaDuplicadoException;
import com.reservasport.canchas.service.CanchaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CanchaController.class)
@Import(GlobalExceptionHandler.class)
public class CanchaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CanchaService canchaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void crearCancha_deberiaRetornar201() throws Exception {

        CanchaRequest request = crearRequest();

        CanchaResponse response = crearResponse();

        when(canchaService.crearCancha(any(CanchaRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/canchas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Cancha Central"))
                .andExpect(jsonPath("$.tipo").value("Fútbol"))
                .andExpect(jsonPath("$.precioHora").value(25000.00))
                .andExpect(jsonPath("$.disponible").value(true));
    }

    @Test
    void obtenerCanchas_deberiaRetornar200YLista() throws Exception {

        when(canchaService.obtenerCanchas())
                .thenReturn(List.of(crearResponse()));

        mockMvc.perform(get("/api/canchas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Cancha Central"))
                .andExpect(jsonPath("$[0].tipo").value("Fútbol"));
    }

    @Test
    void obtenerCanchaPorId_deberiaRetornar200() throws Exception {

        when(canchaService.obtenerCanchaPorId(1L))
                .thenReturn(crearResponse());

        mockMvc.perform(get("/api/canchas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Cancha Central"));
    }

    @Test
    void actualizarCancha_deberiaRetornar200() throws Exception {

        CanchaRequest request = crearRequest();

        CanchaResponse response = CanchaResponse.builder()
                .id(1L)
                .nombre("Cancha Renovada")
                .tipo("Tenis")
                .precioHora(new BigDecimal("30000.00"))
                .disponible(true)
                .fechaCreacion(LocalDateTime.now())
                .build();

        request.setNombre("Cancha Renovada");
        request.setTipo("Tenis");
        request.setPrecioHora(new BigDecimal("30000.00"));

        when(canchaService.actualizarCancha(
                eq(1L),
                any(CanchaRequest.class)
        )).thenReturn(response);

        mockMvc.perform(put("/api/canchas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Cancha Renovada"))
                .andExpect(jsonPath("$.tipo").value("Tenis"))
                .andExpect(jsonPath("$.precioHora").value(30000.00));
    }

    @Test
    void eliminarCancha_deberiaRetornar204() throws Exception {

        doNothing().when(canchaService).eliminarCancha(1L);

        mockMvc.perform(delete("/api/canchas/1"))
                .andExpect(status().isNoContent());

        verify(canchaService).eliminarCancha(1L);
    }

    @Test
    void obtenerCanchaPorId_deberiaRetornar404CuandoNoExiste()
            throws Exception {

        when(canchaService.obtenerCanchaPorId(99L))
                .thenThrow(
                        new CanchaNoEncontradaException(
                                "Cancha no encontrada"
                        )
                );

        mockMvc.perform(get("/api/canchas/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Cancha no encontrada"));
    }

    @Test
    void crearCancha_deberiaRetornar409CuandoNombreExiste()
            throws Exception {

        CanchaRequest request = crearRequest();

        when(canchaService.crearCancha(any(CanchaRequest.class)))
                .thenThrow(
                        new NombreCanchaDuplicadoException(
                                "Ya existe una cancha con ese nombre"
                        )
                );

        mockMvc.perform(post("/api/canchas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message")
                        .value("Ya existe una cancha con ese nombre"));
    }

    @Test
    void crearCancha_deberiaRetornar400CuandoDatosSonInvalidos()
            throws Exception {

        CanchaRequest request = new CanchaRequest();
        request.setNombre("");
        request.setTipo("");
        request.setPrecioHora(BigDecimal.ZERO);

        mockMvc.perform(post("/api/canchas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("Error de validación"));
    }

    private CanchaRequest crearRequest() {

        CanchaRequest request = new CanchaRequest();
        request.setNombre("Cancha Central");
        request.setTipo("Fútbol");
        request.setPrecioHora(new BigDecimal("25000.00"));

        return request;
    }

    private CanchaResponse crearResponse() {

        return CanchaResponse.builder()
                .id(1L)
                .nombre("Cancha Central")
                .tipo("Fútbol")
                .precioHora(new BigDecimal("25000.00"))
                .disponible(true)
                .fechaCreacion(LocalDateTime.now())
                .build();
    }
}
