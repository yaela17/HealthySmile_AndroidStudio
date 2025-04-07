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
import com.example.healthysmile.controller.CedulaResultado;
import com.example.healthysmile.service.ApiNodeMySqlService;
import com.example.healthysmile.R;
import com.example.healthysmile.repository.NodeApiRetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_visualizacion_modelos_3d_gingivitis extends Fragment {

    private EditText edtNombre, edtPrimerApellido, edtSegundoApellido, edtCorreo, edtCedula;
    private Button btnVerificarCedula;
    private TextView txtResultado;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_visualizacion_modelos_3d_gingivitis, container, false);

        edtNombre = view.findViewById(R.id.edtNombre);
        edtPrimerApellido = view.findViewById(R.id.edtPrimerApellido);
        edtSegundoApellido = view.findViewById(R.id.edtSegundoApellido);
        edtCedula = view.findViewById(R.id.edtCedula);
        btnVerificarCedula = view.findViewById(R.id.btnVerificarCedula);
        txtResultado = view.findViewById(R.id.txtResultado);

        btnVerificarCedula.setOnClickListener(v -> verificarCedula());

        return view;
    }

    private void verificarCedula() {
        String nombre = edtNombre.getText().toString();
        String primerApellido = edtPrimerApellido.getText().toString();
        String segundoApellido = edtSegundoApellido.getText().toString();

        // Verificación de campos vacíos
        if (nombre.isEmpty() || primerApellido.isEmpty() || segundoApellido.isEmpty()) {
            Toast.makeText(getContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Llamar al endpoint para verificar la cédula
        ApiNodeMySqlService apiService = NodeApiRetrofitClient.getApiService();
        apiService.verificarCedula(nombre, primerApellido, segundoApellido).enqueue(new Callback<List<CedulaResultado>>() {
            @Override
            public void onResponse(Call<List<CedulaResultado>> call, Response<List<CedulaResultado>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CedulaResultado> resultados = response.body();
                    if (resultados.isEmpty()) {
                        txtResultado.setText("No se encontraron resultados.");
                    } else {
                        StringBuilder sb = new StringBuilder();
                        for (CedulaResultado resultado : resultados) {
                            sb.append("Cédula: ").append(resultado.getCédula()).append("\n")
                                    .append("Nombre: ").append(resultado.getNombre()).append("\n")
                                    .append("Primer Apellido: ").append(resultado.getApellido1()).append("\n")
                                    .append("Segundo Apellido: ").append(resultado.getApellido2()).append("\n")
                                    .append("Tipo: ").append(resultado.getTipo()).append("\n\n");
                        }
                        txtResultado.setText(sb.toString());
                    }
                } else {
                    txtResultado.setText("Error en la respuesta del servidor");
                }
            }
            @Override
            public void onFailure(Call<List<CedulaResultado>> call, Throwable t) {
                Toast.makeText(getContext(), "Error al conectar con la API", Toast.LENGTH_SHORT).show();
                txtResultado.setText("Error: " + t.getMessage());
            }
        });
    }
}
