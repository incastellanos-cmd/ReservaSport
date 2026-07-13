package com.reservasport.reservas.client;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioClientResponse {

    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private Boolean activo;
}
