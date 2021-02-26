package com.register.employe.appregisterback.infraestructure.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "ROLES", schema = "DB_REGISTRO")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RolEntity implements Serializable {

    private static final long serialVersionUID = -6070280162042441435L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ROL")
    private Long idRol;

    private String descripcion;

    @OneToMany(mappedBy = "rolEntity", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private List<AutorizacionEntity> autorizacionEntity;

    @ManyToMany(mappedBy = "rolesEntity", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private List<UsuarioEntity> usuariosEntity;
}
