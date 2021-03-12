package com.register.employe.appregisterback.domain.service;

import com.register.employe.appregisterback.domain.model.Usuario;
import com.register.employe.appregisterback.infraestructure.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.register.employe.appregisterback.domain.constants.FileConstant.*;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
@AllArgsConstructor
@Slf4j
public class UploadImageService {

    private final UsuarioRepository usuarioRepository;

    public void saveProfileImage(Usuario usuario, MultipartFile profileImage) throws IOException {
        if (profileImage != null) {
            Path userFolder = getPath(usuario.getUsuario());
            log.info("Path save images: " + userFolder);

            if (!Files.exists(userFolder)) {
                Files.createDirectories(userFolder);
                log.info(DIRECTORY_CREATE + userFolder);
            }

            Files.deleteIfExists(Paths.get(userFolder + usuario.getUsuario() + DOT + JPG_EXTENCION));
            Files.copy(profileImage.getInputStream(), userFolder.resolve(usuario.getUsuario() + DOT + JPG_EXTENCION), REPLACE_EXISTING);
            usuario.setImagenPerfilUrl(setProfileImageUrl(usuario.getUsuario()));
            usuarioRepository.crearUsuario(usuario);
            log.info(FILE_SAVED_IN_FILE_SYSTEM + profileImage.getOriginalFilename());
        }
    }

    private String setProfileImageUrl(String usuario) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(USER_IMAGE_PATH + usuario +
                FORWARD_SLASH + usuario + DOT + JPG_EXTENCION).toUriString();

    }

    public Path getPath(String usuario) {
        return Paths.get(USER_FOLDER).resolve(usuario).toAbsolutePath().normalize();
    }
}
