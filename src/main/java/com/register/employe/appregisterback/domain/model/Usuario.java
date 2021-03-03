package com.register.employe.appregisterback.domain.model;

import com.register.employe.appregisterback.infraestructure.model.RolEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Usuario {

    private Long idUsuario;
    private String nombres;
    private String apellidos;
    private String usuario;
    private String clave;
    private String correoElectronico;
    private LocalDateTime fechaRegistro;
    private Boolean snActivo;
    private Boolean snNoBloqueado;
    private LocalDateTime fechaUltimoIngreso;
    private LocalDateTime fechaUltimoIngresoVisualizacion;
    private String imagenPerfil;
    private String imagenPerfilUrl;
    private List<Rol> roles;
    private String[] autorizacion;
}
