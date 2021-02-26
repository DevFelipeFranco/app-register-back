package com.register.employe.appregisterback.infraestructure.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificacionEmail {

    private String asunto;
    private String destinatario;
    private String cuerpo;
}
