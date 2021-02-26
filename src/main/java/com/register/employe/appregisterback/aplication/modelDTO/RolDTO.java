package com.register.employe.appregisterback.aplication.modelDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RolDTO {

    private Long idRol;
    private String descripcion;
    private List<AutorizacionDTO> autorizacionesDTO;
}
