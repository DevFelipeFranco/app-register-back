package com.register.employe.appregisterback.infraestructure.controller;

import com.register.employe.appregisterback.aplication.modelDTO.UsuarioDTO;
import com.register.employe.appregisterback.domain.constants.SecurityConstant;
import com.register.employe.appregisterback.domain.exception.*;
import com.register.employe.appregisterback.domain.model.Usuario;
import com.register.employe.appregisterback.domain.service.AuthService;
import com.register.employe.appregisterback.infraestructure.model.UserPrincipal;
import com.register.employe.appregisterback.infraestructure.model.UsuarioEntity;
import com.register.employe.appregisterback.infraestructure.security.JwtProvider;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping(path = {"/", "/api/auth"})
@AllArgsConstructor
public class AuthController extends ExceptionHandling {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @GetMapping(value = "")
    public String inicio() throws EmailExistException {
        throw new EmailExistException("El email ya esta registrado!");
    }

    @GetMapping(value = "/consultarUsuarios")
    public ResponseEntity<List<Usuario>> usuario() {
        return ResponseEntity.ok(authService.consultarUsuario());
    }

    @PostMapping(value = "/login")
    public ResponseEntity<UsuarioEntity> login(@RequestBody UsuarioDTO usuarioDTO) {
        authenticate(usuarioDTO.getUsuario(), usuarioDTO.getClave());
        UsuarioEntity usuarioLogin = authService.consultarUsuarioEntidad(usuarioDTO.getUsuario());
        UserPrincipal userPrincipal = new UserPrincipal(usuarioLogin);
        HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
        return new ResponseEntity<>(usuarioLogin, jwtHeader, HttpStatus.OK);
    }

    private void authenticate(String usuario, String clave) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(usuario, clave));
    }

    private HttpHeaders getJwtHeader(UserPrincipal userPrincipal) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(SecurityConstant.JWT_TOKEN_HEADER, jwtProvider.generateJwtToken(userPrincipal));
        return headers;
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
