package com.register.employe.appregisterback.aplication.modelDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

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
    private Boolean snActivo;
    private Boolean snBloqueado;
    private LocalDateTime fechaUltimoIngreso;
    private LocalDateTime fechaUltimoIngresoVisualizacion;
    private MultipartFile imagenPerfil;
    private String imagenPerfilUrl;
    private List<RolDTO> roles;

}
