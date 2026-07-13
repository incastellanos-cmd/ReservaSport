package com.reservasport.resenas.service;

import com.reservasport.resenas.dto.ResenaRequest;
import com.reservasport.resenas.dto.ResenaResponse;
import com.reservasport.resenas.exception.ResenaNoEncontradaException;
import com.reservasport.resenas.model.Resena;
import com.reservasport.resenas.repository.ResenaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResenaServiceImpl implements ResenaService {

    private final ResenaRepository repository;

    @Override
    public ResenaResponse crearResena(ResenaRequest request) {

        Resena resena = Resena.builder()
                .usuarioId(request.getUsuarioId())
                .canchaId(request.getCanchaId())
                .calificacion(request.getCalificacion())
                .comentario(request.getComentario())
                .build();

        return convertir(repository.save(resena));

    }

    @Override
    public List<ResenaResponse> listarResenas() {

        return repository.findAll()
                .stream()
                .map(this::convertir)
                .toList();

    }

    @Override
    public ResenaResponse obtenerPorId(Long id) {

        return convertir(buscar(id));

    }

    @Override
    public List<ResenaResponse> listarPorCancha(Long canchaId) {

        return repository.findByCanchaId(canchaId)
                .stream()
                .map(this::convertir)
                .toList();

    }

    @Override
    public List<ResenaResponse> listarPorUsuario(Long usuarioId) {

        return repository.findByUsuarioId(usuarioId)
                .stream()
                .map(this::convertir)
                .toList();

    }

    @Override
    public void eliminarResena(Long id) {

        repository.delete(buscar(id));

    }

    private Resena buscar(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new ResenaNoEncontradaException(id));

    }

    private ResenaResponse convertir(Resena resena) {

        return ResenaResponse.builder()
                .id(resena.getId())
                .usuarioId(resena.getUsuarioId())
                .canchaId(resena.getCanchaId())
                .calificacion(resena.getCalificacion())
                .comentario(resena.getComentario())
                .estado(resena.getEstado())
                .fechaCreacion(resena.getFechaCreacion())
                .build();

    }

}
