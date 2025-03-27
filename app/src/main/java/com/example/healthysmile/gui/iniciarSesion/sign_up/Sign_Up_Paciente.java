package com.example.healthysmile.gui.iniciarSesion.sign_up;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.healthysmile.controller.ApiNodeMySqlRespuesta;
import com.example.healthysmile.model.TemplateParams;
import com.example.healthysmile.service.ApiNodeMySqlService;
import com.example.healthysmile.service.EmailServiceJS;
import com.example.healthysmile.utils.SharedPreferencesHelper;
import com.example.healthysmile.model.entities.Usuario;
import com.example.healthysmile.R;
import com.example.healthysmile.repository.NodeApiRetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Sign_Up_Paciente extends Fragment {

    private EditText fragSignUpInputNombreUsuario, fragSignUpInputCorreo, fragSignUpInputContrasena;
    private Button fragSignUpBtnRegistrarse;
    private SharedPreferencesHelper manejadorShadPreferences;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign__up__paciente, container, false);

        manejadorShadPreferences = new SharedPreferencesHelper(getContext());

        fragSignUpInputNombreUsuario = view.findViewById(R.id.fragSignUpPacienteInputNombreUsuario);
        fragSignUpInputCorreo = view.findViewById(R.id.fragSignUpPacienteInputCorreo);
        fragSignUpInputContrasena = view.findViewById(R.id.fragSignUpPacienteInputContrasena);
        fragSignUpBtnRegistrarse = view.findViewById(R.id.fragSignUpPacienteBtnRegistrarse);

        fragSignUpBtnRegistrarse.setOnClickListener(v -> registrarUsuario());

        return view;
    }

    private void registrarUsuario() {
        String nombre = fragSignUpInputNombreUsuario.getText().toString().trim();
        String correo = fragSignUpInputCorreo.getText().toString().trim();
        String contrasena = fragSignUpInputContrasena.getText().toString().trim();
        int nivelPermisos = 1;
        String tipoUser = "Paciente";

        if (nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
            Toast.makeText(getActivity(), "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        Usuario paciente = new Usuario(contrasena, correo, null, null, nivelPermisos, nombre,"Paciente");
        ApiNodeMySqlService apiService = NodeApiRetrofitClient.getApiService();

        apiService.crearPaciente(paciente).enqueue(new Callback<ApiNodeMySqlRespuesta>() {
            @Override
            public void onResponse(Call<ApiNodeMySqlRespuesta> call, Response<ApiNodeMySqlRespuesta> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getActivity(), "Usuario registrado con éxito", Toast.LENGTH_SHORT).show();
                    long idUsuario = response.body().getIdUsuario();
                    Log.d("RegistroUsuario", "ID Usuario: " + idUsuario);
                    manejadorShadPreferences.guardarPaciente(nombre, correo, tipoUser, nivelPermisos);
                    manejadorShadPreferences.guardarIdUsuario(idUsuario);

                    EmailServiceJS emailService = new EmailServiceJS();
                    String verification_code = generadorCodigoDeVerificacion();
                    manejadorShadPreferences.guardarCodigoVerificacion(verification_code);
                    TemplateParams templateParams = new TemplateParams(contrasena,correo,nombre,verification_code);
                    emailService.enviarCorreo(templateParams);

                    limpiarCampos();
                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.signUpFragmentContainer, new Fragment_sign__up__verificacion_correo()) // Reemplaza con el nuevo fragmento
                            .commit();
                } else {
                    Toast.makeText(getActivity(), "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiNodeMySqlRespuesta> call, Throwable t) {
                Toast.makeText(getActivity(), "Error al conectar con la API", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void limpiarCampos() {
        fragSignUpInputNombreUsuario.setText("");
        fragSignUpInputCorreo.setText("");
        fragSignUpInputContrasena.setText("");
    }

    public String generadorCodigoDeVerificacion() {
        int code = (int) (100000 + Math.random() * 900000);
        return String.valueOf(code);
    }

}
