package com.reservasport.usuarios.service;

import com.reservasport.usuarios.dto.UsuarioRequest;
import com.reservasport.usuarios.dto.UsuarioResponse;

import java.util.List;

public interface UsuarioService {

    UsuarioResponse crearUsuario(UsuarioRequest request);

    List<UsuarioResponse> obtenerUsuarios();

    UsuarioResponse obtenerUsuarioPorId(Long id);

    UsuarioResponse actualizarUsuario(Long id, UsuarioRequest request);

    void eliminarUsuario(Long id);
}
