package com.softulp.loginconavatar.request;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import com.softulp.loginconavatar.model.Usuario;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;


public class ApiClient {

    private static File archivoUsuario;

    private static File conectar(Context context){
        if (archivoUsuario == null){
            archivoUsuario = new File(context.getFilesDir(), "Usuario.obj");
        }
        return archivoUsuario;
    }

    public static boolean guardar(Context context, Usuario usuario){

        boolean guardado = false;
        archivoUsuario = conectar(context);

        //try-with-resources: FileOutputStream y ObjectOutputStream implementan
        //las interfaces AutoCloseable, por lo que utilizo un try-with-resources.
        //aunque suceda una exception, el 'try-catch' se encargará de cerrarlos y catchear las
        //exceptions, si estas sucedieran. Sin embargo, no provee con un buffer, hay que
        //utilizarlo explicitamente.
        try (FileOutputStream fos = new FileOutputStream(archivoUsuario, false);
             BufferedOutputStream buff = new BufferedOutputStream(fos);
             ObjectOutputStream oos = new ObjectOutputStream(buff)) {
            oos.writeObject(usuario);
            oos.flush();
            guardado = true;
        } catch (FileNotFoundException ex){
            Toast.makeText(context, "¡Error de archivo!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(context, "¡Error de entrada/salida!", Toast.LENGTH_SHORT).show();
        }
        return guardado;
    }

    public static Usuario leer(Context context){

        Usuario usuario = null;
        archivoUsuario = conectar(context);

        try (FileInputStream fis = new FileInputStream(archivoUsuario);
             BufferedInputStream buff = new BufferedInputStream(fis);
             ObjectInputStream ois = new ObjectInputStream(buff)){

            usuario = (Usuario) ois.readObject();

        } catch (FileNotFoundException e) {
            Toast.makeText(context, "¡Error de archivo!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(context, "¡Error de entrada/salida!", Toast.LENGTH_SHORT).show();
        } catch (ClassNotFoundException e) {
            Toast.makeText(context, "Error inesperado!", Toast.LENGTH_SHORT).show();
        }
        return usuario;
    }

    public static Usuario login(Context context, String email, String password){
        Usuario usuario = null;
        usuario = leer(context); //recupero usuario y luego comparo con email y password.
        if (!email.equals(usuario.getEmail()) || !password.equals(usuario.getPassword())){
            usuario = null; //null si no coinciden los datos o directamente no es el mismo user
        }
        return usuario; //else return el usuario encontrado
    }

}
