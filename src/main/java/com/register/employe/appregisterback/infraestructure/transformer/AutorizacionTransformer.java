package com.register.employe.appregisterback.infraestructure.transformer;

import com.register.employe.appregisterback.domain.model.Autorizacion;
import com.register.employe.appregisterback.infraestructure.model.AutorizacionEntity;

import java.util.List;
import java.util.stream.Collectors;

public final class AutorizacionTransformer {

    public static Autorizacion entityToModelo(AutorizacionEntity autorizacionEntity) {
        return Autorizacion.builder()
                .idAutorizacion(autorizacionEntity.getIdAutorizacion())
                .autorizacion(autorizacionEntity.getAutorizacion())
                .build();
    }

    public static AutorizacionEntity modelToEntity(Autorizacion autorizacion) {
        return AutorizacionEntity.builder()
                .idAutorizacion(autorizacion.getIdAutorizacion())
                .autorizacion(autorizacion.getAutorizacion())
                .build();
    }

    public static List<Autorizacion> lstEntityToModelo(List<AutorizacionEntity> autorizacionEntities) {
        return autorizacionEntities.stream().map(AutorizacionTransformer::entityToModelo).collect(Collectors.toList());
    }

    public static List<AutorizacionEntity> lstModelToEntity(List<Autorizacion> autorizacion) {
        return autorizacion.stream().map(AutorizacionTransformer::modelToEntity).collect(Collectors.toList());
    }
}
