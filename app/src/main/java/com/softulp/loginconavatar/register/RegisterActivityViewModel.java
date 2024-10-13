package com.softulp.loginconavatar.register;

import static android.app.Activity.RESULT_OK;
import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.softulp.loginconavatar.model.Usuario;
import com.softulp.loginconavatar.register.RegisterActivity;
import com.softulp.loginconavatar.request.ApiClient;
import com.softulp.loginconavatar.utils.EmailValidator;
import com.softulp.loginconavatar.utils.InputError;

public class RegisterActivityViewModel extends AndroidViewModel {

    private Context context;
    private Uri uri;
    private MutableLiveData<Uri> avatarM;
    private MutableLiveData<InputError> inputErrors;
    private MutableLiveData<Usuario> usuarioM;

    public RegisterActivityViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public LiveData<InputError> getInputErrors(){
        if (inputErrors == null){
            inputErrors = new MutableLiveData<>();
        }
        return inputErrors;
    }

    public LiveData<Uri> getAvatarM(){
        if (avatarM == null){
            avatarM = new MutableLiveData<>();
        }
        return avatarM;
    }

    public LiveData<Usuario> getUsuarioM(){
        if (usuarioM == null){
            usuarioM = new MutableLiveData<>();
        }
        return usuarioM;
    }

    public void guardarDatos(String nombre, String apellido, String dni, String email, String password){

        if (validarInputs(nombre, apellido, dni, email, password)){
            Usuario usuario = new Usuario(nombre, password, Long.parseLong(dni), email, apellido, uri != null? uri.toString(): null);
            if (ApiClient.guardar(context, usuario)){
                Toast.makeText(context, "¡Su usuario se ha guardado con éxito!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "Error al guardar el usuario", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private boolean validarInputs(String nombre, String apellido, String dni, String email, String password){
        boolean valido = true;
        InputError errores = new InputError();

        if (nombre.isBlank()){
            errores.setNombreError("¡El nombre no puede estar vacio!");
            valido = false;
        }

        if (apellido.isBlank()){
            errores.setApellidoError("¡El apellido no puede estar vacio!");
            valido = false;
        }

        if (dni.isBlank()){
            errores.setDniError("¡El DNI no puede estar vacio!");
            valido = false;
        } else if (!dni.matches("\\d+")) {
            errores.setDniError("El DNI solo puede contener números.");
            valido = false;
        }

        if (email.isBlank()){
            errores.setEmailError("¡El email no puede estar vacio!");
            valido = false;
        } else if (!EmailValidator.isValidEmail(email)){
            errores.setEmailError("El email debe tener un formato valido(ej: 'suEmail@gmail.com').");
            valido = false;
        }

        if (password.isBlank()){
            errores.setPasswordError("¡La contraseña no puede estar vacia!");
            valido = false;
        }

        inputErrors.setValue(errores);

        return valido;
    }

    public void getDatos(Intent intent){
        if (intent.getBooleanExtra("existingUser", false)){
            Usuario usuario = ApiClient.leer(context);
            if (usuario != null) {
                usuarioM.setValue(usuario);
                String uriDeUsuario = usuario.getUriString();
                Log.d("uri", "null?: "+uriDeUsuario);
                if (uriDeUsuario != null){
                    Log.d("uri", "antes: "+uriDeUsuario);
                    uri = Uri.parse(uriDeUsuario);
                    avatarM.setValue(uri);
                    Log.d("uri", "despues: "+Uri.parse(uriDeUsuario).toString());
                }
            } else {
                Toast.makeText(context, "Oops: ¡Ocurrio un error inesperado al recuperar su usuario!", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void recibirFoto(ActivityResult o){
        if (o.getResultCode() == RESULT_OK){
            Intent data = o.getData();
            if (data != null){
                uri = data.getData();
                avatarM.setValue(uri);
            }
        }
    }



}
