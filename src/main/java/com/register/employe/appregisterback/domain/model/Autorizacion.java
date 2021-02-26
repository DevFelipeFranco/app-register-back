package com.register.employe.appregisterback.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Autorizacion {

    private Long idAutorizacion;
    private String autorizacion;
}
