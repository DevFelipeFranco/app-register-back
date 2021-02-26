package com.register.employe.appregisterback.aplication.modelDTO;

import com.register.employe.appregisterback.domain.model.Rol;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AutorizacionDTO {

    private Long idAutorizacion;
    private String autorizacion;
    private RolDTO rolDTO;
}
