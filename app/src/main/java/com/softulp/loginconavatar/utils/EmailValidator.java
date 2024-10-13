package com.softulp.loginconavatar.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+$";

    public static boolean isValidEmail(String email){
        Pattern pattern = Pattern.compile(EMAIL_REGEX); //compila la regex
        Matcher matcher = pattern.matcher(email); //crea un Matcher para checkear si hace match
        return matcher.matches(); //retorna si el email hizo match con el patron
    }

}
