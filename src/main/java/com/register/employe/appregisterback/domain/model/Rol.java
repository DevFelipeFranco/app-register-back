package com.register.employe.appregisterback.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Rol {

    private Long idRol;
    private String descripcion;
    private List<Autorizacion> autorizaciones;
}
