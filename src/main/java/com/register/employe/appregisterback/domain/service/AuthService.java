package com.register.employe.appregisterback.domain.service;

import com.register.employe.appregisterback.domain.exception.CorreoActivacionException;
import com.register.employe.appregisterback.domain.exception.EmailExistException;
import com.register.employe.appregisterback.domain.exception.UserNotFoundException;
import com.register.employe.appregisterback.domain.exception.UsernameExisteException;
import com.register.employe.appregisterback.domain.model.Autorizacion;
import com.register.employe.appregisterback.domain.model.Rol;
import com.register.employe.appregisterback.domain.model.Token;
import com.register.employe.appregisterback.domain.model.Usuario;
import com.register.employe.appregisterback.domain.util.TransformadorBoolean;
import com.register.employe.appregisterback.infraestructure.model.NotificacionEmail;
import com.register.employe.appregisterback.infraestructure.model.UsuarioEntity;
import com.register.employe.appregisterback.infraestructure.repository.TokenRepository;
import com.register.employe.appregisterback.infraestructure.repository.UsuarioRepository;
import com.register.employe.appregisterback.infraestructure.service.MailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.*;

import static com.register.employe.appregisterback.domain.constants.UsuarioConstant.*;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    public Usuario registrarUsuario(String nombres, String apellidos, String usuario, String clave, String email) throws UserNotFoundException, UsernameExisteException, EmailExistException, CorreoActivacionException {
        validateNuevoUsuarioYEmail(StringUtils.EMPTY, usuario, email);

        List<Autorizacion> autorizacions = Collections.singletonList(new Autorizacion(1L, "user:read"));
        List<Rol> rols = Collections.singletonList(new Rol(1L, "ROLE_USER", autorizacions));

        Usuario registroUsuario = Usuario.builder()
                .nombres(nombres)
                .apellidos(apellidos)
                .usuario(usuario)
                .clave(passwordEncoder.encode(clave))
                .correoElectronico(email)
                .fechaRegistro(LocalDateTime.now())
                .snActivo(false)
                .snBloqueado(true)
                .roles(Collections.singletonList(new Rol(1L, "ROLE_USER", new ArrayList<>())))
                .imagenPerfilUrl(getImagenPerfilTemporar())
                .build();

        Token token = generarVerificacionToken(registroUsuario);
        log.info("Se registro el usuario con exito y se envio el token para la activacion de la cuenta");
        enviarEmail(token.getToken(), email);
        return token.getUsuario();
    }

    private String getImagenPerfilTemporar() {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(RUTA_IMAGEN_PERFIL_POR_DEFECTO).toUriString();
    }

    private Usuario validateNuevoUsuarioYEmail(String actualNombreUsuario, String nuevoUsuario, String nuevoEmail) throws UserNotFoundException, UsernameExisteException, EmailExistException {
        Usuario usuarioPorNuevoNombreUsuario = buscarUsuarioPorNombreUsuario(nuevoUsuario);
        Usuario usuarioPorNuevoEmail = buscarUsuarioPorEmail(nuevoEmail);

        if (StringUtils.isNoneBlank(actualNombreUsuario)) {
            Usuario actualUsuario = buscarUsuarioPorNombreUsuario(actualNombreUsuario);
            if (actualUsuario == null) {
                throw new UserNotFoundException(NO_SE_ENCONTRO_EL_USUARIO_POR + actualNombreUsuario);
            }

            if (usuarioPorNuevoNombreUsuario != null && !actualUsuario.getIdUsuario().equals(usuarioPorNuevoNombreUsuario.getIdUsuario())) {
                throw new UsernameExisteException(EL_USUARIO_YA_SE_ENCUENTRA_REGISTRADO);
            }

            if (usuarioPorNuevoEmail != null && !actualUsuario.getIdUsuario().equals(usuarioPorNuevoEmail.getIdUsuario())) {
                throw new EmailExistException(EL_EMAIL_YA_SE_ENCUENTRA_REGISTRADO);
            }
            return actualUsuario;
        } else {
            if (usuarioPorNuevoNombreUsuario != null) {
                throw new UsernameExisteException(EL_USUARIO_YA_SE_ENCUENTRA_REGISTRADO);
            }

            if (usuarioPorNuevoEmail != null) {
                throw new EmailExistException(EL_EMAIL_YA_SE_ENCUENTRA_REGISTRADO);
            }

            return null;
        }
    }

    public Usuario buscarUsuarioPorNombreUsuario(String usuario) {
        return usuarioRepository.buscarUsuarioPorNombreUsuario(usuario);
    }

    public Usuario buscarUsuarioPorEmail(String email) {
        return usuarioRepository.buscarUsuarioPorCorreoElectronico(email);
    }

    private Token generarVerificacionToken(Usuario usuario) {
        String token = UUID.randomUUID().toString();
        Token verificacionToken = Token.builder()
                .token(token)
                .usuario(usuario)
                .fechaExpiracion(LocalDateTime.now())
                .build();

        return tokenRepository.registrarToken(verificacionToken);
    }

    public List<Usuario> consultarUsuario() {
        return usuarioRepository.consultarusuarios();
    }

    private void enviarEmail(String token, String email) throws CorreoActivacionException {
        mailService.sendMail(new NotificacionEmail("Por favor activa tu cuenta",
                email, "Gracias por registrarte, " +
                "por favor da click en la URL a continuacion para activar tu cuenta : " +
                "http://localhost:9003/api/auth/accountVerification/" + token));
    }

    public void verificarCuenta(String token) {
        Token tokenRegistrado = tokenRepository.buscarToken(token);
        buscarUsaurioYHabilitar(tokenRegistrado);
    }

    private void buscarUsaurioYHabilitar(Token token) {
//        String usuario = token.getUsuario().getUsuario();
//        Usuario usuarioEncontrado = buscarUsuarioPorNombreUsuario(usuario);
        token.getUsuario().setSnActivo(true);
        usuarioRepository.actualizarUsuario(TransformadorBoolean.booleanToString(token.getUsuario().getSnActivo()), token.getUsuario().getIdUsuario());
    }
}
