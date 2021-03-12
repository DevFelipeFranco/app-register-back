package com.register.employe.appregisterback.domain.constants;

public class SecurityConstant {

    //    public static final long EXPIRATION_TIME = 3600;
    public static final String TOKEN_PREFEX = "Bearer ";
    public static final String JWT_TOKEN_HEADER = "Jwt-Token";
    public static final String TOKEN_CANNOT_BE_VERIFIED = "El token no se pudo verificar";
    public static final String REGISTER_EMPLOYE_LCC = "Registro Empleado, LLC";
    public static final String REGISTER_EMPLOYE_ADMINISTRATION = "Administrador de Registro Empleados";
    public static final String AUTHORITIES = "authorities";
    public static final String FORBIDDEN_MESSAGE = "Necesitas iniciar sesion para acceder a la pagina";
    public static final String ACCESS_DENIED_MESSAGE = "NO tienes permiso para acceder a esta pagina";
    public static final String OPTION_HTTP_METHOD = "OPTIONS";
    public static final String[] PUBLIC_URLS = {"/api/auth/accountVerification/**", "/api/auth/login", "/api/auth", "/api/auth/consultarUsuarios", "/api/auth/resetclave/**", "/api/auth/image/**"};
//    public static final String[] PUBLIC_URLS = {"**"};

}
