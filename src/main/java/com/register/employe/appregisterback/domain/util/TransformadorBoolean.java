package com.register.employe.appregisterback.domain.util;

public class TransformadorBoolean {

    public static Boolean stringToBoolean(String valor) {
        return valor.equals("S");
    }

    public static String booleanToString(Boolean valor) {
        return valor ? "S" : "N";
    }
}
