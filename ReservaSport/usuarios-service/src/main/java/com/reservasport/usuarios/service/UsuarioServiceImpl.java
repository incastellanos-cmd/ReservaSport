package com.reservasport.usuarios.service;

import com.reservasport.usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.reservasport.usuarios.dto.UsuarioRequest;
import com.reservasport.usuarios.dto.UsuarioResponse;
import com.reservasport.usuarios.model.Usuario;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import com.reservasport.usuarios.exception.EmailDuplicadoException;
import com.reservasport.usuarios.exception.UsuarioNoEncontradoException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService{

    private final UsuarioRepository usuarioRepository;

    @Override
    public UsuarioResponse crearUsuario(UsuarioRequest request) {

        log.info("Creando usuario con correo: {}", request.getEmail());

        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new EmailDuplicadoException("Ya existe un usuario con ese correo");
        }

        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .email(request.getEmail())
                .telefono(request.getTelefono())
                .activo(true)
                .build();

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        log.info("Usuario creado con ID: {}", usuarioGuardado.getId());

        return convertirAResponse(usuarioGuardado);}

    @Override
    public List<UsuarioResponse> obtenerUsuarios() {

        log.info("Consultando lista de usuarios");

        return usuarioRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Override
    public UsuarioResponse obtenerUsuarioPorId(Long id) {

        log.info("Consultando usuario con ID: {}", id);


        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

        return convertirAResponse(usuario);
    }

    @Override
    public UsuarioResponse actualizarUsuario(Long id, UsuarioRequest request) {

        log.info("Actualizando usuario con ID: {}", id);

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

        if (!usuario.getEmail().equals(request.getEmail())
                && usuarioRepository.existsByEmail(request.getEmail())) {
            throw new EmailDuplicadoException("Ya existe un usuario con ese correo");        }

        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setEmail(request.getEmail());
        usuario.setTelefono(request.getTelefono());

        Usuario usuarioActualizado = usuarioRepository.save(usuario);

        return convertirAResponse(usuarioActualizado);
    }

    @Override
    public void eliminarUsuario(Long id) {

        log.info("Eliminando usuario con ID: {}", id);

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

        usuarioRepository.delete(usuario);
    }

        private UsuarioResponse convertirAResponse(Usuario usuario) {
            return UsuarioResponse.builder()
                    .id(usuario.getId())
                    .nombre(usuario.getNombre())
                    .apellido(usuario.getApellido())
                    .email(usuario.getEmail())
                    .telefono(usuario.getTelefono())
                    .activo(usuario.getActivo())
                    .fechaCreacion(usuario.getFechaCreacion())
                    .build();
        }
}
