package com.register.employe.appregisterback.infraestructure.model;

import com.register.employe.appregisterback.domain.util.TransformadorBoolean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPrincipal implements UserDetails {

    private UsuarioEntity usuarioEntity;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<AutorizacionEntity> autorities =
                this.usuarioEntity.getRolesEntity().stream()
                        .map(RolEntity::getAutorizacionEntity)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList());
        return autorities.stream()
                .map(autority ->
                        new SimpleGrantedAuthority(autority.getAutorizacion()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return usuarioEntity.getClave();
    }

    @Override
    public String getUsername() {
        return usuarioEntity.getUsuario();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return TransformadorBoolean.stringToBoolean(usuarioEntity.getSnNoBloqueado());
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return TransformadorBoolean.stringToBoolean(this.usuarioEntity.getSnActivo());
    }
}
