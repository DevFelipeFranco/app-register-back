package com.register.employe.appregisterback.domain.repositorio;

import com.register.employe.appregisterback.domain.model.Token;

import java.util.Optional;

public interface TokenRepositorio {

    Token registrarToken(Token token);

    Token buscarToken(String token);
}
