package com.register.employe.appregisterback.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Token {

    private Long idToken;
    private String token;
    private LocalDateTime fechaExpiracion;
    private Usuario usuario;
}
