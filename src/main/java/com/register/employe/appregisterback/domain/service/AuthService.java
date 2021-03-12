package com.register.employe.appregisterback.domain.service;

import com.register.employe.appregisterback.aplication.modelDTO.RolDTO;
import com.register.employe.appregisterback.domain.enumeration.Role;
import com.register.employe.appregisterback.domain.exception.*;
import com.register.employe.appregisterback.domain.model.Rol;
import com.register.employe.appregisterback.domain.model.Token;
import com.register.employe.appregisterback.domain.model.Usuario;
import com.register.employe.appregisterback.domain.util.TransformadorBoolean;
import com.register.employe.appregisterback.infraestructure.model.NotificacionEmail;
import com.register.employe.appregisterback.infraestructure.model.UsuarioEntity;
import com.register.employe.appregisterback.infraestructure.repository.TokenRepository;
import com.register.employe.appregisterback.infraestructure.repository.UsuarioRepository;
import com.register.employe.appregisterback.infraestructure.service.MailService;
import com.register.employe.appregisterback.infraestructure.transformer.RolTransformer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.register.employe.appregisterback.domain.constants.FileConstant.RUTA_IMAGEN_PERFIL_POR_DEFECTO;
import static com.register.employe.appregisterback.domain.constants.UsuarioConstant.*;
import static org.apache.logging.log4j.util.Strings.EMPTY;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final UploadImageService uploadImageService;

    public Usuario registrarUsuario(String nombres, String apellidos, String usuario, String clave, String email) throws UserNotFoundException, UsernameExisteException, EmailExistException, CorreoActivacionException {
        validateNuevoUsuarioYEmail(StringUtils.EMPTY, usuario, email);

        Usuario registroUsuario = Usuario.builder()
                .nombres(nombres)
                .apellidos(apellidos)
                .usuario(usuario)
                .clave(passwordEncoder.encode(clave))
                .correoElectronico(email)
                .fechaRegistro(LocalDateTime.now())
                .snActivo(false)
                .snNoBloqueado(true)
                .roles(Collections.singletonList(new Rol(1L, Role.ROLE_USER.name(), new ArrayList<>())))
                .imagenPerfilUrl(getImagenPerfilTemporarUrl(usuario))
                .build();

        Token token = generarVerificacionToken(registroUsuario);
        log.info("Se registro el usuario con exito y se envio el token para la activacion de la cuenta");
        enviarEmail(token.getToken(), email);
//        mailService.sendNewPasswordEmail(nombres, clave, email);
        return token.getUsuario();
    }

    @Transactional
    public Usuario crearNuevoUsuario(String firstName, String lastName, String username, String email, List<RolDTO> role, boolean isNotLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException, UsernameExisteException, EmailExistException, IOException {
        validateNuevoUsuarioYEmail(EMPTY, username, email);
        List<Rol> roles = RolTransformer.lstDTOToModel(role);
        Usuario usuarioNuevo = Usuario.builder()
                .nombres(firstName)
                .apellidos(lastName)
                .usuario(username)
                .clave(passwordEncoder.encode(generatePassword()))
                .correoElectronico(email)
                .fechaRegistro(LocalDateTime.now())
                .snActivo(isActive)
                .snNoBloqueado(isNotLocked)
                .roles(roles)
                .imagenPerfilUrl(getImagenPerfilTemporarUrl(username))
                .build();

//        usuarioRepository.crearUsuario(usuarioNuevo);
        uploadImageService.saveProfileImage(usuarioNuevo, profileImage);
        return usuarioNuevo;
    }

    public Usuario updateUsuario(String currentUsername, String newFirstName, String newLastName, String newUsername, String newEmail, List<RolDTO> role, boolean isNotLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException, UsernameExisteException, EmailExistException, IOException {
        Usuario currentUser = validateNuevoUsuarioYEmail(currentUsername, newUsername, newEmail);
        List<Rol> roles = RolTransformer.lstDTOToModel(role);
        Usuario.builder()
                .nombres(newFirstName)
                .apellidos(newLastName)
                .usuario(newUsername)
                .correoElectronico(newEmail)
                .fechaRegistro(LocalDateTime.now())
                .snActivo(isActive)
                .snNoBloqueado(isNotLocked)
                .roles(roles)
                .build();

        usuarioRepository.crearUsuario(currentUser);
        uploadImageService.saveProfileImage(currentUser, profileImage);
        return currentUser;
    }

    private String getImagenPerfilTemporarUrl(String username) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(RUTA_IMAGEN_PERFIL_POR_DEFECTO + username).toUriString();
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

    private String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(10);
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
        token.getUsuario().setSnActivo(true);
        usuarioRepository.actualizarUsuario(TransformadorBoolean.booleanToString(token.getUsuario().getSnActivo()), token.getUsuario().getIdUsuario());
    }

    public UsuarioEntity consultarUsuarioEntidad(String usuario) {
        return usuarioRepository.findByUsuario(usuario);
    }

    public void deleteUser(Long idUsuario) {
        usuarioRepository.eliminarPorId(idUsuario);
    }

    public void resetPassword(String email) throws MessagingException, EmailNotFoundException {
        Usuario usuario = usuarioRepository.buscarUsuarioPorCorreoElectronico(email);
        if (usuario == null) {
            throw new EmailNotFoundException(NO_USER_FOUND_BY_EMAIL + email);
        }

        String clave = generatePassword();
        usuario.setClave(passwordEncoder.encode(clave));
        usuarioRepository.crearUsuario(usuario);
        mailService.sendNewPasswordEmail(usuario.getNombres(), clave, usuario.getCorreoElectronico());
    }

    public Usuario updateProfileImage(String username, MultipartFile profileImage) throws UserNotFoundException, UsernameExisteException, EmailExistException, IOException {
        Usuario usuario = validateNuevoUsuarioYEmail(username, null, null);
        uploadImageService.saveProfileImage(usuario, profileImage);
        return usuario;
    }
}
