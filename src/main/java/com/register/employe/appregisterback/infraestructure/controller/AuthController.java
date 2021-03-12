package com.register.employe.appregisterback.infraestructure.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.register.employe.appregisterback.aplication.modelDTO.UsuarioDTO;
import com.register.employe.appregisterback.domain.constants.SecurityConstant;
import com.register.employe.appregisterback.domain.exception.*;
import com.register.employe.appregisterback.domain.model.Usuario;
import com.register.employe.appregisterback.domain.service.AuthService;
import com.register.employe.appregisterback.domain.service.UploadImageService;
import com.register.employe.appregisterback.infraestructure.model.UserPrincipal;
import com.register.employe.appregisterback.infraestructure.model.UsuarioEntity;
import com.register.employe.appregisterback.infraestructure.security.JwtProvider;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.register.employe.appregisterback.domain.constants.FileConstant.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

@RestController
@RequestMapping(path = {"/", "/api/auth"})
@AllArgsConstructor
public class AuthController extends ExceptionHandling {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UploadImageService uploadImageService;

    @GetMapping(value = "")
    public String inicio() throws EmailExistException {
        throw new EmailExistException("El email ya esta registrado!");
    }

    @GetMapping(value = "/consultarUsuarios")
    public ResponseEntity<List<Usuario>> usuario() {
        return ResponseEntity.ok(authService.consultarUsuario());
    }

    @GetMapping(value = "/consultarUsuarios/{username}")
    public ResponseEntity<Usuario> consultarUsuarioPorUsuario(@PathVariable("username") String usuario) {
        return ResponseEntity.ok(authService.buscarUsuarioPorNombreUsuario(usuario));
    }

    @GetMapping(value = "/resetPassword/{email}")
    public ResponseEntity<String> resetPassword(@PathVariable("email") String email) throws MessagingException, EmailNotFoundException {
        authService.resetPassword(email);
        return ResponseEntity.ok("Se envio al email: " + email + " la nueva clave.");
    }

    @GetMapping(value = "/accountVerification/{token}")
    public ResponseEntity<String> verificacionCuenta(@PathVariable("token") String token) {
        authService.verificarCuenta(token);
        return new ResponseEntity<>("Cuenta activada con exito", HttpStatus.OK);
    }

    @GetMapping(path = "/image/user/{usuario}/{fileName}", produces = IMAGE_JPEG_VALUE)
    public byte[] getProfileImage(@PathVariable("usuario") String usuario, @PathVariable("fileName") String fileName) throws IOException {
        return Files.readAllBytes(Paths.get(uploadImageService.getPath(usuario) + FORWARD_SLASH + fileName).normalize());
    }

    @GetMapping(path = "/image/profile/{username}", produces = IMAGE_JPEG_VALUE)
    public byte[] getTempProfileImage(@PathVariable("username") String username) throws IOException {
        URL url = new URL(TEMP_PROFILE_IMAGE_BASE_URL + username);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (InputStream inputStream = url.openStream()) {
            int bytesRead;
            byte[] chunk = new byte[1024];
            while ((bytesRead = inputStream.read(chunk)) > 0) {
                byteArrayOutputStream.write(chunk, 0, bytesRead);
            }
        }
        return byteArrayOutputStream.toByteArray();
    }

    @PostMapping(value = "/login")
    public ResponseEntity<UsuarioEntity> login(@RequestBody UsuarioDTO usuarioDTO) {
        authenticate(usuarioDTO.getUsuario(), usuarioDTO.getClave());
        UsuarioEntity usuarioLogin = authService.consultarUsuarioEntidad(usuarioDTO.getUsuario());
        UserPrincipal userPrincipal = new UserPrincipal(usuarioLogin);
        HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
        return new ResponseEntity<>(usuarioLogin, jwtHeader, HttpStatus.OK);
    }

    @PostMapping(value = "")
    public ResponseEntity<Usuario> registrarUsuario(@RequestBody UsuarioDTO usuarioDTO) throws UserNotFoundException, EmailExistException, CorreoActivacionException, UsernameExisteException, MessagingException {
        return ResponseEntity.status(CREATED)
                .body(authService.registrarUsuario(usuarioDTO.getUsuario(), usuarioDTO.getApellidos(), usuarioDTO.getUsuario(), usuarioDTO.getClave(), usuarioDTO.getCorreoElectronico()));
    }

    @PostMapping(value = "/crearUsuario", consumes = {MediaType.APPLICATION_JSON_VALUE,
                                                        MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Usuario> crearUsuario(@RequestParam("usuarioDTO") String usuarioDTO, @RequestParam(value = "imagenPerfil", required = false) MultipartFile imagenPerfil) throws UserNotFoundException, EmailExistException, UsernameExisteException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        UsuarioDTO usuario = mapper.readValue(usuarioDTO, UsuarioDTO.class);
        usuario.setImagenPerfil(imagenPerfil);

        Usuario usuarioNuevo = authService.crearNuevoUsuario(usuario.getNombres(), usuario.getApellidos(), usuario.getUsuario(), usuario.getCorreoElectronico(), usuario.getRoles(), usuario.getSnBloqueado(), usuario.getSnActivo(), usuario.getImagenPerfil());
        return ResponseEntity.status(CREATED).body(usuarioNuevo);
    }

    @PostMapping(value = "/actualizarUsuario/{oldUsuario}")
    public ResponseEntity<Usuario> actualizarUsuario(@RequestParam("usuario") UsuarioDTO usuarioDTO, @PathVariable("oldUsuario") String usuarioAnterior) throws UserNotFoundException, EmailExistException, UsernameExisteException, IOException {
        Usuario usuario = authService.updateUsuario(usuarioDTO.getUsuario(), usuarioDTO.getNombres(), usuarioDTO.getApellidos(), usuarioAnterior, usuarioDTO.getCorreoElectronico(), usuarioDTO.getRoles(), usuarioDTO.getSnBloqueado(), usuarioDTO.getSnActivo(), usuarioDTO.getImagenPerfil());
        return ResponseEntity.status(CREATED).body(usuario);
    }

    @PostMapping(value = "/updateProfileImage")
    public ResponseEntity<Usuario> actualizarImagenPerfil(@RequestBody UsuarioDTO usuarioDTO) throws UserNotFoundException, EmailExistException, UsernameExisteException, IOException {
        Usuario usuario = authService.updateProfileImage(usuarioDTO.getUsuario(), usuarioDTO.getImagenPerfil());
        return ResponseEntity.ok(usuario);
    }

    @DeleteMapping(value = "/delete/{idUsaurio}")
    @PreAuthorize("hasAnyAuthority('user:delete')")
    public ResponseEntity<String> deleteUser(@PathVariable("idUsaurio") Long idUsaurio) {
        authService.deleteUser(idUsaurio);
        return ResponseEntity.status(NO_CONTENT).body("Usuario eliminado con exito");
    }

    private void authenticate(String usuario, String clave) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(usuario, clave));
    }

    private HttpHeaders getJwtHeader(UserPrincipal userPrincipal) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(SecurityConstant.JWT_TOKEN_HEADER, jwtProvider.generateJwtToken(userPrincipal));
        return headers;
    }
}
