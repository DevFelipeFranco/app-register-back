package com.register.employe.appregisterback.infraestructure.transformer;

import com.register.employe.appregisterback.domain.model.Token;
import com.register.employe.appregisterback.infraestructure.model.TokenEntity;

public final class TokenTransformer {

    public static TokenEntity modelToEntity(Token token) {
        return TokenEntity.builder()
                .idToken(token.getIdToken())
                .token(token.getToken())
                .fechaExpiracion(token.getFechaExpiracion())
                .usuarioEntity(UsuarioTransformer.modelToEntity(token.getUsuario()))
                .build();
    }

    public static Token entityToModel(TokenEntity tokenEntity) {
        return Token.builder()
                .idToken(tokenEntity.getIdToken())
                .token(tokenEntity.getToken())
                .fechaExpiracion(tokenEntity.getFechaExpiracion())
                .usuario(UsuarioTransformer.entityToModel(tokenEntity.getUsuarioEntity()))
                .build();
    }
}
