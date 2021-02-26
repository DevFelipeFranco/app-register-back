package com.register.employe.appregisterback.infraestructure.repository;

import com.register.employe.appregisterback.domain.exception.TokenNotFoundException;
import com.register.employe.appregisterback.domain.model.Token;
import com.register.employe.appregisterback.domain.repositorio.TokenRepositorio;
import com.register.employe.appregisterback.infraestructure.model.TokenEntity;
import com.register.employe.appregisterback.infraestructure.transformer.TokenTransformer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<TokenEntity, Long>, TokenRepositorio {

    @Override
    default Token registrarToken(Token token) {
        TokenEntity tokenEntity = TokenTransformer.modelToEntity(token);
        return TokenTransformer.entityToModel(save(tokenEntity));
    }

    @Override
    default Token buscarToken(String token) {
        return TokenTransformer.entityToModel(findByToken(token).orElseThrow(() -> new TokenNotFoundException("Token invalido")));
    }

    Optional<TokenEntity> findByToken(String token);
}
