package com.register.employe.appregisterback.infraestructure.transformer;

import com.register.employe.appregisterback.aplication.modelDTO.RolDTO;
import com.register.employe.appregisterback.domain.model.Rol;
import com.register.employe.appregisterback.infraestructure.model.RolEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class RolTransformer {

    public static Rol entityToModel(RolEntity rolEntity) {
        return Rol.builder()
                .idRol(rolEntity.getIdRol())
                .descripcion(rolEntity.getDescripcion())
                .autorizaciones(AutorizacionTransformer.lstEntityToModelo(rolEntity.getAutorizacionEntity()))
                .build();
    }

    public static RolEntity modelToEntity(Rol rol) {
        return RolEntity.builder()
                .idRol(rol.getIdRol())
                .descripcion(rol.getDescripcion())
                .autorizacionEntity(AutorizacionTransformer.lstModelToEntity(rol.getAutorizaciones()))
                .build();
    }

    public static Rol DTOToModel(RolDTO rol) {
        return Rol.builder()
                .idRol(rol.getIdRol())
                .descripcion(rol.getDescripcion())
                .autorizaciones(new ArrayList<>())
                .build();
    }

    public static List<Rol> lstEntityToModel(List<RolEntity> rolesEntity) {
        return rolesEntity.stream().map(RolTransformer::entityToModel).collect(Collectors.toList());
    }

    public static List<RolEntity> lstModelToEntity(List<Rol> roles) {
        return roles.stream().map(RolTransformer::modelToEntity).collect(Collectors.toList());
    }

    public static List<Rol> lstDTOToModel(List<RolDTO> roles) {
        return roles.stream().map(RolTransformer::DTOToModel).collect(Collectors.toList());
    }
}
