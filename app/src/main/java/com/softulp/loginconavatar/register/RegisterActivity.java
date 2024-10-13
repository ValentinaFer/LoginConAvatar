package com.softulp.loginconavatar.register;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.softulp.loginconavatar.R;
import com.softulp.loginconavatar.databinding.ActivityRegisterBinding;
import com.softulp.loginconavatar.model.Usuario;
import com.softulp.loginconavatar.register.RegisterActivity;
import com.softulp.loginconavatar.request.ApiClient;
import com.softulp.loginconavatar.utils.EmailValidator;
import com.softulp.loginconavatar.utils.InputError;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private RegisterActivityViewModel vm;
    private Intent intent;

    private ActivityResultLauncher<Intent> arl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        vm = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(RegisterActivityViewModel.class);
        setContentView(binding.getRoot());
        abrirGaleria();

        vm.getUsuarioM().observe(this, new Observer<Usuario>() {
            @Override
            public void onChanged(Usuario usuario) {
                binding.tvTitulo.setText(R.string.editando_usuario);

                binding.nombreEditText.setText(usuario.getNombre());
                binding.apellidoEditText.setText(usuario.getApellido());
                binding.dniEditText.setText(usuario.getDni()+"");
                binding.emailEditText.setText(usuario.getEmail());
                binding.passwordEditText.setText(usuario.getPassword());

            }
        });

        vm.getAvatarM().observe(this, new Observer<Uri>() {
            @Override
            public void onChanged(Uri uri) {
                binding.ivAvatar.setImageURI(uri);
            }
        });

        vm.getInputErrors().observe(this, new Observer<InputError>() {
            @Override
            public void onChanged(InputError inputError) {
                binding.textInputLayoutNombre.setError(inputError.getNombreError());
                binding.textInputLayoutApellido.setError(inputError.getApellidoError());
                binding.textInputLayoutDni.setError(inputError.getDniError());
                binding.textInputLayoutEmail.setError(inputError.getEmailError());
                binding.textInputLayoutPassword.setError(inputError.getPasswordError());
            }
        });

        binding.btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre, apellido, dni, email, password;
                nombre = binding.nombreEditText.getText().toString();
                apellido = binding.apellidoEditText.getText().toString();
                dni = binding.dniEditText.getText().toString();
                email = binding.emailEditText.getText().toString();
                password = binding.passwordEditText.getText().toString();
                vm.guardarDatos(nombre, apellido, dni, email, password);
            }
        });

        binding.btnAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arl.launch(intent);
            }
        });


        vm.getDatos(getIntent());

    }

    public void abrirGaleria(){
        intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        arl = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>(){
            @Override
            public void onActivityResult(ActivityResult o) {
                vm.recibirFoto(o);
            }
        });
    }
}