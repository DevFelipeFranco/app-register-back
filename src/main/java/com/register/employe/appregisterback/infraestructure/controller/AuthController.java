package com.register.employe.appregisterback.infraestructure.controller;

import com.register.employe.appregisterback.aplication.modelDTO.UsuarioDTO;
import com.register.employe.appregisterback.domain.exception.*;
import com.register.employe.appregisterback.domain.model.Usuario;
import com.register.employe.appregisterback.domain.service.AuthService;
import com.register.employe.appregisterback.infraestructure.model.UserPrincipal;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping(path = {"/", "/api/auth"})
@AllArgsConstructor
public class AuthController extends ExceptionHandling {

    private final AuthService authService;

    @GetMapping(value = "")
    public String inicio() throws EmailExistException {
        throw new EmailExistException("El email ya esta registrado!");
    }

    @GetMapping(value = "/consultarUsuarios")
    public ResponseEntity<List<Usuario>> usuario() {
        return ResponseEntity.ok(authService.consultarUsuario());
    }

    @PostMapping(value = "/login")
    public ResponseEntity<Usuario> login(@RequestBody UsuarioDTO usuarioDTO) {
        Usuario login = authService.login(usuarioDTO);
        HttpHeaders jwtHeader = authService.getJwtHeader((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return new ResponseEntity<>(login, jwtHeader, HttpStatus.OK);
    }

    @PostMapping(value = "")
    public ResponseEntity<Usuario> registrarUsuario(@RequestBody UsuarioDTO usuarioDTO) throws UserNotFoundException, EmailExistException, CorreoActivacionException, UsernameExisteException {
        return ResponseEntity.status(CREATED)
                .body(authService.registrarUsuario(usuarioDTO.getUsuario(), usuarioDTO.getApellidos(), usuarioDTO.getUsuario(), usuarioDTO.getClave(), usuarioDTO.getCorreoElectronico()));
    }

    @GetMapping(value = "/accountVerification/{token}")
    public ResponseEntity<String> verificacionCuenta(@PathVariable("token") String token) {
        authService.verificarCuenta(token);
        return new ResponseEntity<>("Cuenta activada con exito", HttpStatus.OK);
    }
}
