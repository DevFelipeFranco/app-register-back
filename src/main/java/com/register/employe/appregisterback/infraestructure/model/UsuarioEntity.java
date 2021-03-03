package com.register.employe.appregisterback.infraestructure.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "USUARIOS", schema = "DB_REGISTRO")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioEntity implements Serializable {

    private static final long serialVersionUID = -3192978798038865857L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_USUARIO")
    private Long idUsuario;

    private String nombres;
    private String apellidos;

    private String usuario;
    private String clave;

    private String correoElectronico;
    private LocalDateTime fechaRegistro;
    private String snActivo;
    private String snNoBloqueado;
    private LocalDateTime fechaUltimoIngreso;
    private LocalDateTime fechaUltimoIngresoVisualizacion;
    private String imagenPerfil;
    private String imagenPerfilUrl;

//    @OneToOne(mappedBy = "usuarioEntity", cascade = CascadeType.ALL)
//    private TokenEntity tokenEntity;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(
            name = "USUARIO_ROL",
            joinColumns = @JoinColumn(
                    name = "ID_USUARIO",
                    referencedColumnName = "ID_USUARIO"),
            inverseJoinColumns = @JoinColumn(
                    name = "ID_ROL",
                    referencedColumnName = "ID_ROL"))
    private List<RolEntity> rolesEntity;
}
