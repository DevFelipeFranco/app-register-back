package com.register.employe.appregisterback.infraestructure.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "AUTORIZACION", schema = "DB_REGISTRO")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AutorizacionEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAutorizacion;

    private String autorizacion;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ROL")
    private RolEntity rolEntity;
}
