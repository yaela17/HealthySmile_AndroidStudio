package com.example.healthysmile.gui.educacionDental;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.healthysmile.controller.ApiNodeMySqlRespuesta;
import com.example.healthysmile.service.ApiNodeMySqlService;
import com.example.healthysmile.model.entities.Usuario;
import com.example.healthysmile.R;
import com.example.healthysmile.repository.NodeApiRetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_visualizacion_modelos_3d_gingivitis extends Fragment {

    private EditText edtNombre, edtCorreo, edtContrasena;
    private Button btnEnviarPaciente;
    private TextView txtResultado;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_visualizacion_modelos_3d_gingivitis, container, false);

        edtNombre = view.findViewById(R.id.edtNombre);
        edtCorreo = view.findViewById(R.id.edtCorreo);
        edtContrasena = view.findViewById(R.id.edtContrasena);
        btnEnviarPaciente = view.findViewById(R.id.btnEnviarPaciente);
        txtResultado = view.findViewById(R.id.txtResultado);

        btnEnviarPaciente.setOnClickListener(v -> crearPaciente());

        return view;
    }

    private void crearPaciente() {
        String nombre = edtNombre.getText().toString();
        String correo = edtCorreo.getText().toString();
        String contrasena = edtContrasena.getText().toString();

        if (nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
            Toast.makeText(getContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        Usuario paciente = new Usuario(contrasena, correo, null, null, 1, nombre,"Paciente");

        ApiNodeMySqlService apiService = NodeApiRetrofitClient.getApiService();
        apiService.crearPaciente(paciente).enqueue(new Callback<ApiNodeMySqlRespuesta>() {
            @Override
            public void onResponse(Call<ApiNodeMySqlRespuesta> call, Response<ApiNodeMySqlRespuesta> response) {
                if (response.isSuccessful() && response.body() != null) {
                    txtResultado.setText("Respuesta: " + response.body().getMessage());
                } else {
                    txtResultado.setText("Error en la respuesta del servidor");
                }
            }

            @Override
            public void onFailure(Call<ApiNodeMySqlRespuesta> call, Throwable t) {
                Toast.makeText(getContext(), "Error al conectar con la API", Toast.LENGTH_SHORT).show();
                txtResultado.setText("Error: " + t.getMessage());
            }
        });
    }
}
