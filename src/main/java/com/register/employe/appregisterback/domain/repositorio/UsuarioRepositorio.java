package com.register.employe.appregisterback.domain.repositorio;

import com.register.employe.appregisterback.domain.model.Usuario;

import java.util.List;

public interface UsuarioRepositorio {

    Usuario buscarUsuarioPorNombreUsuario(String usuario);
    Usuario buscarUsuarioPorCorreoElectronico(String correoElectronico);

    List<Usuario> consultarusuarios();

    void actualizarUsuario(String activo, Long idUsuario);

    Usuario crearUsuario(Usuario usuario);

    void eliminarPorId(Long idUsuario);
}
