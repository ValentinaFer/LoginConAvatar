package com.softulp.loginconavatar.login;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.softulp.loginconavatar.register.RegisterActivity;
import com.softulp.loginconavatar.request.ApiClient;
import com.softulp.loginconavatar.utils.EmailValidator;


public class MainActivityViewModel extends AndroidViewModel {

    private Context context;
    private MutableLiveData<String> mEmailError;
    private MutableLiveData<String> mPasswordError;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public LiveData<String> getMEmailError(){
        if (mEmailError == null){
            mEmailError = new MutableLiveData<>();
        }
        return mEmailError;
    }

    public LiveData<String> getMPasswordError(){
        if (mPasswordError == null){
            mPasswordError = new MutableLiveData<>();
        }
        return mPasswordError;
    }

    public void ingresar(String email, String password){

        if (inputsValidos(email, password)){
            if (ApiClient.login(context, email, password) != null){
                Intent intent = new Intent(context, RegisterActivity.class);
                intent.putExtra("existingUser", true);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "¡Oops! email o contraseña incorrecta.", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private boolean inputsValidos(String email, String password){

        boolean valido = true;
        if (email.isBlank()){
            mEmailError.setValue("¡Debe ingresar su email!");
            valido = false;
        } else if (!EmailValidator.isValidEmail(email)){
            mEmailError.setValue("El email debe tener un formato valido(ej: 'suEmail@hotmail.com')");
            valido = false;
        }

        if (password.isBlank()){
            mPasswordError.setValue("¡Debe ingresar su contraseña!");
            valido = false;
        }

        return valido;
    }

}
