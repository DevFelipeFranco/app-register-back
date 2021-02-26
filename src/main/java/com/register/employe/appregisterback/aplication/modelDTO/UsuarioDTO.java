package com.register.employe.appregisterback.aplication.modelDTO;

import com.register.employe.appregisterback.domain.model.Rol;
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
public class UsuarioDTO {

    private Long idUsuario;
    private String nombres;
    private String apellidos;
    private String usuario;
    private String clave;
    private String correoElectronico;
    private LocalDateTime fechaRegistro;
    private String snActivo;
    private String snBloqueado;
    private LocalDateTime fechaUltimoIngreso;
    private LocalDateTime fechaUltimoIngresoVisualizacion;
    private String imagenPerfil;
    private String imagenPerfilUrl;
    private List<RolDTO> rolesDTO;

}
