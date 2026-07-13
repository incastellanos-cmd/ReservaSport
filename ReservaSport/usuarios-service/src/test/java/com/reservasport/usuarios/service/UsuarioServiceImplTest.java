package com.reservasport.usuarios.service;

import com.reservasport.usuarios.dto.UsuarioRequest;
import com.reservasport.usuarios.dto.UsuarioResponse;
import com.reservasport.usuarios.exception.EmailDuplicadoException;
import com.reservasport.usuarios.exception.UsuarioNoEncontradoException;
import com.reservasport.usuarios.model.Usuario;
import com.reservasport.usuarios.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private UsuarioRequest request;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        request = new UsuarioRequest();
        request.setNombre("Juan");
        request.setApellido("Pérez");
        request.setEmail("juan@correo.com");
        request.setTelefono("987654321");

        usuario = Usuario.builder()
                .id(1L)
                .nombre("Juan")
                .apellido("Pérez")
                .email("juan@correo.com")
                .telefono("987654321")
                .activo(true)
                .fechaCreacion(LocalDateTime.now())
                .build();
    }

    @Test
    void crearUsuario_deberiaCrearUsuarioCorrectamente() {

        // Given
        when(usuarioRepository.existsByEmail(request.getEmail()))
                .thenReturn(false);

        when(usuarioRepository.save(any(Usuario.class)))
                .thenReturn(usuario);

        // When
        UsuarioResponse response = usuarioService.crearUsuario(request);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Juan", response.getNombre());
        assertEquals("juan@correo.com", response.getEmail());
        assertTrue(response.getActivo());

        verify(usuarioRepository).existsByEmail(request.getEmail());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void crearUsuario_deberiaLanzarExcepcionCuandoEmailYaExiste() {

        // Given
        when(usuarioRepository.existsByEmail(request.getEmail()))
                .thenReturn(true);

        // When & Then
        EmailDuplicadoException exception = assertThrows(
                EmailDuplicadoException.class,
                () -> usuarioService.crearUsuario(request)
        );

        assertEquals(
                "Ya existe un usuario con ese correo",
                exception.getMessage()
        );

        verify(usuarioRepository).existsByEmail(request.getEmail());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void obtenerUsuarioPorId_deberiaRetornarUsuario() {

        // Given
        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuario));

        // When
        UsuarioResponse response = usuarioService.obtenerUsuarioPorId(1L);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Juan", response.getNombre());
        assertEquals("juan@correo.com", response.getEmail());

        verify(usuarioRepository).findById(1L);
    }

    @Test
    void obtenerUsuarioPorId_deberiaLanzarExcepcionCuandoNoExiste() {

        // Given
        when(usuarioRepository.findById(99L))
                .thenReturn(Optional.empty());

        // When & Then
        UsuarioNoEncontradoException exception = assertThrows(
                UsuarioNoEncontradoException.class,
                () -> usuarioService.obtenerUsuarioPorId(99L)
        );

        assertEquals("Usuario no encontrado", exception.getMessage());

        verify(usuarioRepository).findById(99L);
    }

    @Test
    void eliminarUsuario_deberiaEliminarUsuario() {

        // Given
        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuario));

        // When
        usuarioService.eliminarUsuario(1L);

        // Then
        verify(usuarioRepository).findById(1L);
        verify(usuarioRepository).delete(usuario);
    }

    @Test
    void obtenerUsuarios_deberiaRetornarListaDeUsuarios() {

        // Given
        when(usuarioRepository.findAll())
                .thenReturn(List.of(usuario));

        // When
        List<UsuarioResponse> responses = usuarioService.obtenerUsuarios();

        // Then
        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(1L, responses.get(0).getId());
        assertEquals("Juan", responses.get(0).getNombre());
        assertEquals("juan@correo.com", responses.get(0).getEmail());

        verify(usuarioRepository).findAll();
    }

    @Test
    void actualizarUsuario_deberiaActualizarUsuarioCorrectamente() {

        // Given
        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuario));

        when(usuarioRepository.save(any(Usuario.class)))
                .thenReturn(usuario);

        // When
        UsuarioResponse response = usuarioService.actualizarUsuario(1L, request);

        // Then
        assertNotNull(response);
        assertEquals("Juan", response.getNombre());
        assertEquals("Pérez", response.getApellido());
        assertEquals("juan@correo.com", response.getEmail());

        verify(usuarioRepository).findById(1L);
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void actualizarUsuario_deberiaLanzarExcepcionCuandoNuevoEmailYaExiste() {

        // Given
        request.setEmail("otro@correo.com");

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuario));

        when(usuarioRepository.existsByEmail("otro@correo.com"))
                .thenReturn(true);

        // When & Then
        EmailDuplicadoException exception = assertThrows(
                EmailDuplicadoException.class,
                () -> usuarioService.actualizarUsuario(1L, request)
        );

        assertEquals(
                "Ya existe un usuario con ese correo",
                exception.getMessage()
        );

        verify(usuarioRepository).findById(1L);
        verify(usuarioRepository).existsByEmail("otro@correo.com");
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void eliminarUsuario_deberiaLanzarExcepcionCuandoNoExiste() {

        // Given
        when(usuarioRepository.findById(99L))
                .thenReturn(Optional.empty());

        // When & Then
        UsuarioNoEncontradoException exception = assertThrows(
                UsuarioNoEncontradoException.class,
                () -> usuarioService.eliminarUsuario(99L)
        );

        assertEquals("Usuario no encontrado", exception.getMessage());

        verify(usuarioRepository).findById(99L);
        verify(usuarioRepository, never()).delete(any(Usuario.class));
    }
}
