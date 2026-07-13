package com.reservasport.resenas.service;

import com.reservasport.resenas.dto.ResenaRequest;
import com.reservasport.resenas.dto.ResenaResponse;

import java.util.List;

public interface ResenaService {

    ResenaResponse crearResena(ResenaRequest request);

    List<ResenaResponse> listarResenas();

    ResenaResponse obtenerPorId(Long id);

    List<ResenaResponse> listarPorCancha(Long canchaId);

    List<ResenaResponse> listarPorUsuario(Long usuarioId);

    void eliminarResena(Long id);
}
