package com.reservasport.usuarios.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reservasport.usuarios.dto.UsuarioRequest;
import com.reservasport.usuarios.dto.UsuarioResponse;
import com.reservasport.usuarios.exception.EmailDuplicadoException;
import com.reservasport.usuarios.exception.UsuarioNoEncontradoException;
import com.reservasport.usuarios.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void crearUsuario_deberiaRetornar201() throws Exception {

        UsuarioRequest request = new UsuarioRequest();
        request.setNombre("Juan");
        request.setApellido("Pérez");
        request.setEmail("juan@correo.com");
        request.setTelefono("987654321");

        UsuarioResponse response = UsuarioResponse.builder()
                .id(1L)
                .nombre("Juan")
                .apellido("Pérez")
                .email("juan@correo.com")
                .telefono("987654321")
                .activo(true)
                .fechaCreacion(LocalDateTime.now())
                .build();

        when(usuarioService.crearUsuario(any(UsuarioRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Juan"));
    }

    @Test
    void obtenerUsuarios_deberiaRetornar200YLista() throws Exception {

        UsuarioResponse response = UsuarioResponse.builder()
                .id(1L)
                .nombre("Juan")
                .apellido("Pérez")
                .email("juan@correo.com")
                .telefono("987654321")
                .activo(true)
                .fechaCreacion(LocalDateTime.now())
                .build();

        when(usuarioService.obtenerUsuarios())
                .thenReturn(List.of(response));

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Juan"))
                .andExpect(jsonPath("$[0].email").value("juan@correo.com"));
    }

    @Test
    void obtenerUsuarioPorId_deberiaRetornar200() throws Exception {

        UsuarioResponse response = UsuarioResponse.builder()
                .id(1L)
                .nombre("Juan")
                .apellido("Pérez")
                .email("juan@correo.com")
                .telefono("987654321")
                .activo(true)
                .fechaCreacion(LocalDateTime.now())
                .build();

        when(usuarioService.obtenerUsuarioPorId(1L))
                .thenReturn(response);

        mockMvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Juan"))
                .andExpect(jsonPath("$.email").value("juan@correo.com"));
    }

    @Test
    void actualizarUsuario_deberiaRetornar200() throws Exception {

        UsuarioRequest request = new UsuarioRequest();
        request.setNombre("Carlos");
        request.setApellido("Gómez");
        request.setEmail("carlos@correo.com");
        request.setTelefono("999999999");

        UsuarioResponse response = UsuarioResponse.builder()
                .id(1L)
                .nombre("Carlos")
                .apellido("Gómez")
                .email("carlos@correo.com")
                .telefono("999999999")
                .activo(true)
                .fechaCreacion(LocalDateTime.now())
                .build();

        when(usuarioService.actualizarUsuario(eq(1L), any(UsuarioRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Carlos"))
                .andExpect(jsonPath("$.email").value("carlos@correo.com"));
    }

    @Test
    void eliminarUsuario_deberiaRetornar204() throws Exception {

        doNothing().when(usuarioService).eliminarUsuario(1L);

        mockMvc.perform(delete("/api/usuarios/1"))
                .andExpect(status().isNoContent());

        verify(usuarioService).eliminarUsuario(1L);
    }

    @Test
    void obtenerUsuarioPorId_deberiaRetornar404() throws Exception {

        when(usuarioService.obtenerUsuarioPorId(99L))
                .thenThrow(new UsuarioNoEncontradoException("Usuario no encontrado"));

        mockMvc.perform(get("/api/usuarios/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void crearUsuario_deberiaRetornar409CuandoEmailExiste() throws Exception {

        UsuarioRequest request = new UsuarioRequest();
        request.setNombre("Juan");
        request.setApellido("Pérez");
        request.setEmail("juan@test.com");
        request.setTelefono("987654321");

        when(usuarioService.crearUsuario(any(UsuarioRequest.class)))
                .thenThrow(new EmailDuplicadoException("Ya existe un usuario con ese correo"));

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void crearUsuario_deberiaRetornar400CuandoDatosInvalidos() throws Exception {

        UsuarioRequest request = new UsuarioRequest();
        request.setNombre("");
        request.setApellido("");
        request.setEmail("correo-invalido");
        request.setTelefono("");

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
