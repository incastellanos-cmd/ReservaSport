package com.reservasport.horarios.service;

import com.reservasport.horarios.dto.HorarioRequest;
import com.reservasport.horarios.dto.HorarioResponse;

import java.util.List;
public interface HorarioService {

    HorarioResponse crearHorario(HorarioRequest request);

    List<HorarioResponse> obtenerHorarios();

    HorarioResponse obtenerHorarioPorId(Long id);

    HorarioResponse actualizarHorario(Long id, HorarioRequest request);

    HorarioResponse actualizarDisponibilidad(Long id, Boolean disponible);

    void eliminarHorario(Long id);
}
