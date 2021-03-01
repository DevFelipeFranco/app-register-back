package com.register.employe.appregisterback.infraestructure.transformer;

import com.register.employe.appregisterback.domain.model.Usuario;
import com.register.employe.appregisterback.domain.util.TransformadorBoolean;
import com.register.employe.appregisterback.infraestructure.model.UserPrincipal;
import com.register.employe.appregisterback.infraestructure.model.UsuarioEntity;

import java.util.List;
import java.util.stream.Collectors;

public final class UsuarioTransformer {

    public static Usuario entityToModel(UsuarioEntity usuarioEntity) {
        if (usuarioEntity != null) {
            return Usuario.builder()
                    .idUsuario(usuarioEntity.getIdUsuario())
                    .nombres(usuarioEntity.getNombres())
                    .apellidos(usuarioEntity.getApellidos())
                    .usuario(usuarioEntity.getUsuario())
                    .clave(usuarioEntity.getClave())
                    .correoElectronico(usuarioEntity.getCorreoElectronico())
                    .fechaRegistro(usuarioEntity.getFechaRegistro())
                    .snActivo(TransformadorBoolean.stringToBoolean(usuarioEntity.getSnActivo()))
                    .snBloqueado(TransformadorBoolean.stringToBoolean(usuarioEntity.getSnBloqueado()))
                    .fechaUltimoIngreso(usuarioEntity.getFechaUltimoIngreso())
                    .fechaUltimoIngresoVisualizacion(usuarioEntity.getFechaUltimoIngresoVisualizacion())
                    .imagenPerfil(usuarioEntity.getImagenPerfil())
                    .imagenPerfilUrl(usuarioEntity.getImagenPerfilUrl())
                    .roles(RolTransformer.lstEntityToModel(usuarioEntity.getRolesEntity()))
                    .build();
        }
        return null;
    }

    public static UsuarioEntity modelToEntity(Usuario usuario) {
        return UsuarioEntity.builder()
                .idUsuario(usuario.getIdUsuario())
                .nombres(usuario.getNombres())
                .apellidos(usuario.getApellidos())
                .usuario(usuario.getUsuario())
                .clave(usuario.getClave())
                .correoElectronico(usuario.getCorreoElectronico())
                .fechaRegistro(usuario.getFechaRegistro())
                .snActivo(TransformadorBoolean.booleanToString(usuario.getSnActivo()))
                .snBloqueado(TransformadorBoolean.booleanToString(usuario.getSnBloqueado()))
                .fechaUltimoIngreso(usuario.getFechaUltimoIngreso())
                .fechaUltimoIngresoVisualizacion(usuario.getFechaUltimoIngresoVisualizacion())
                .imagenPerfil(usuario.getImagenPerfil())
                .imagenPerfilUrl(usuario.getImagenPerfilUrl())
                .rolesEntity(RolTransformer.lstModelToEntity(usuario.getRoles()))
                .build();
    }

    public static List<Usuario> lstEntityToModel(List<UsuarioEntity> usuariosEntity) {
        return usuariosEntity.stream().map(UsuarioTransformer::entityToModel).collect(Collectors.toList());
    }

//    private Usuario userPrincipalToUsuario(UserPrincipal userPrincipal) {
//        return Usuario.builder()
//                .idUsuario(userPrincipal.getUsuarioEntity().getIdUsuario())
//                .nombres(userPrincipal.getUsuarioEntity().getNombres())
//                .apellidos(userPrincipal.getUsuarioEntity().getApellidos())
//                .usuario(userPrincipal.getUsername())
//                .clave(userPrincipal.getPassword())
//                .correoElectronico(userPrincipal.getUsuarioEntity().getCorreoElectronico())
//                .fechaRegistro(userPrincipal.getUsuarioEntity().getFechaRegistro())
//                .snActivo(userPrincipal.isEnabled())
//                .snBloqueado(userPrincipal.isAccountNonLocked())
//                .fechaUltimoIngreso(userPrincipal.getUsuarioEntity().getFechaUltimoIngreso())
//                .fechaUltimoIngresoVisualizacion(userPrincipal.getUsuarioEntity().getFechaUltimoIngresoVisualizacion())
//                .imagenPerfil(userPrincipal.getUsuarioEntity().getImagenPerfil())
//                .imagenPerfilUrl(userPrincipal.getUsuarioEntity().getImagenPerfilUrl())
//                .roles(RolTransformer.lstEntityToModel(userPrincipal.getUsuarioEntity().getRolesEntity()))
//                .build();
//    }
}
