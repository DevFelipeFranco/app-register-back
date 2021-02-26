package com.register.employe.appregisterback.infraestructure.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "TOKEN", schema = "DB_REGISTRO")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenEntity implements Serializable {

    private static final long serialVersionUID = -3398493589197219284L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idToken;

    private String token;

    @Column(name = "FECHA_EXPIRACION", columnDefinition = "TIMESTAMP")
    private LocalDateTime fechaExpiracion;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ID_USUARIO")
    private UsuarioEntity usuarioEntity;
}
