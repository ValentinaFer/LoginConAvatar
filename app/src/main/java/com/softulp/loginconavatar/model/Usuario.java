package com.softulp.loginconavatar.model;

import android.net.Uri;
import android.util.Log;

import java.io.Serializable;

public class Usuario implements Serializable {

    private String nombre;
    private String apellido;
    private String email;
    private long dni;
    private String password;
    private String uriString;

    public Usuario(String nombre, String password, long dni, String email, String apellido, String uriString) {
        this.nombre = nombre;
        this.password = password;
        this.dni = dni;
        this.email = email;
        this.apellido = apellido;
        this.uriString = uriString;
    }
    public String getUriString() {
        return uriString;
    }

    public void setUriString(String uriString) {
        this.uriString = uriString;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getDni() {
        return dni;
    }

    public void setDni(long dni) {
        this.dni = dni;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
}
