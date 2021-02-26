package com.register.employe.appregisterback.infraestructure.repository;

import com.register.employe.appregisterback.domain.model.Usuario;
import com.register.employe.appregisterback.domain.repositorio.UsuarioRepositorio;
import com.register.employe.appregisterback.infraestructure.model.UsuarioEntity;
import com.register.employe.appregisterback.infraestructure.transformer.UsuarioTransformer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long>, UsuarioRepositorio {

    @Override
    default Usuario buscarUsuarioPorNombreUsuario(String usuario) {
        return UsuarioTransformer.entityToModel(findByUsuario(usuario));
    }

    @Override
    default Usuario buscarUsuarioPorCorreoElectronico(String correoElectronico) {
        return UsuarioTransformer.entityToModel(findByCorreoElectronico(correoElectronico));
    }

    @Override
    default List<Usuario> consultarusuarios() {
        return UsuarioTransformer.lstEntityToModel(findAll());
    }

    @Override
    default void actualizarUsuario(String activo, Long idUsuario) {
        actualizarEstadoUsuario(activo, idUsuario);
    }

    UsuarioEntity findByUsuario(String usuario);
    UsuarioEntity findByCorreoElectronico(String correoElectronico);

    @Modifying
    @Query("UPDATE UsuarioEntity u SET u.snActivo = ?1 WHERE u.idUsuario = ?2")
    void actualizarEstadoUsuario(String activo, Long idUsuario);
}
