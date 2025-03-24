package com.example.healthysmile.gui.ayudaYSoporte;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;

import androidx.fragment.app.Fragment;

import com.example.healthysmile.R;
import com.example.healthysmile.controller.ApiNodeMySqlRespuesta;
import com.example.healthysmile.controller.ayudaYSoporte.ObtenerPreguntasFrecuentesResponse;
import com.example.healthysmile.gui.extraAndroid.adaptadores.AdaptadorExpandibleListView;
import com.example.healthysmile.repository.FirebaseMessageRepository;
import com.example.healthysmile.repository.NodeApiRetrofitClient;
import com.example.healthysmile.service.ApiNodeMySqlService;
import com.example.healthysmile.service.ayudaYSoporte.ObtenerPreguntasFrecuentesService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

public class fragment_ayuda_y_soporte extends Fragment implements View.OnClickListener {

    Button btnEnviar;
    EditText campoNombre, campoCorreo, campoPregunta;
    ExpandableListView preguntasFrecuentesLV;

    private FirebaseMessageRepository conexionFirebaseDB;

    // Listas para almacenar las preguntas y respuestas
    private List<String> preguntasList = new ArrayList<>();
    private List<String> respuestasList = new ArrayList<>();
    private List<Long> idsPreguntasFrecuentesList = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ayuda_y_soporte, container, false);

        campoNombre = view.findViewById(R.id.formPreguntaFrecuenteNombre);
        campoCorreo = view.findViewById(R.id.formPreguntaFrecuenteCorreo);
        campoPregunta = view.findViewById(R.id.formPreguntaFrecuentePregunta);
        btnEnviar = view.findViewById(R.id.formPreguntaFrecuenteBtnEnviar);
        preguntasFrecuentesLV = view.findViewById(R.id.expandableListViewPreguntasFrecuentes);

        conexionFirebaseDB = new FirebaseMessageRepository(); // Inicializamos la conexión a Firebase
        btnEnviar.setOnClickListener(this);

        // Llamada para cargar las preguntas frecuentes de Firebase
        cargarPreguntasFrecuentes();

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.formPreguntaFrecuenteBtnEnviar) {
            crearPreguntaFrecuente();
        }
    }

    private void crearPreguntaFrecuente() {
        final String correoUsuario = campoCorreo.getText().toString().trim();
        final String pregunta = campoPregunta.getText().toString().trim();

        if (correoUsuario.isEmpty() || pregunta.isEmpty()) {
            Log.e("Error", "El correo o la pregunta no pueden estar vacíos");
            return;
        }

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("AppPrefs", getContext().MODE_PRIVATE);
        long idUsuario = sharedPreferences.getLong("idUsuario", -1);

        Log.d("VerificarCorreo", "Correo a verificar: " + correoUsuario);
        Log.d("VerificarCorreo", "ID de Usuario: " + idUsuario);

        verificarCorreo(correoUsuario, idUsuario, pregunta);
    }

    private void verificarCorreo(String correoUsuario, long idUsuario, String pregunta) {
        ApiNodeMySqlService apiService = NodeApiRetrofitClient.getApiService();
        apiService.verificarCorreo(correoUsuario).enqueue(new retrofit2.Callback<ApiNodeMySqlRespuesta>() {
            @Override
            public void onResponse(Call<ApiNodeMySqlRespuesta> call, Response<ApiNodeMySqlRespuesta> response) {
                Log.d("VerificarCorreo", "Respuesta recibida: " + response.toString());

                if (response.isSuccessful()) {
                    ApiNodeMySqlRespuesta apiResponse = response.body();
                    Log.d("VerificarCorreo", "Cuerpo de la respuesta: " + (apiResponse != null ? apiResponse.getMensaje() : "null"));

                    if (apiResponse != null && "Existe".equals(apiResponse.getMensaje())) {
                        Log.d("VerificarCorreo", "Correo verificado con éxito, procediendo a insertar pregunta frecuente.");
                        insertarPreguntaFrecuente(idUsuario, pregunta);
                    } else {
                        Log.e("VerificarCorreo", "Correo no registrado en la aplicación");
                        campoCorreo.setError("Correo no registrado en la aplicación");
                    }
                } else {
                    Log.e("Error", "No se pudo verificar el correo. Código de respuesta: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiNodeMySqlRespuesta> call, Throwable t) {
                Log.e("Error", "Error en la solicitud: " + t.getMessage());
                t.printStackTrace();
            }
        });
    }

    private void insertarPreguntaFrecuente(long idUsuario, String pregunta) {
        Map<String, Object> preguntaFrecuente = new HashMap<>();
        preguntaFrecuente.put("pregunta", pregunta);
        preguntaFrecuente.put("idUsuario", idUsuario);

        ApiNodeMySqlService apiService = NodeApiRetrofitClient.getApiService();

        apiService.crearPreguntaFrecuente(preguntaFrecuente)
                .enqueue(new retrofit2.Callback<ApiNodeMySqlRespuesta>() {
                    @Override
                    public void onResponse(Call<ApiNodeMySqlRespuesta> call, Response<ApiNodeMySqlRespuesta> response) {
                        if (response.isSuccessful()) {
                            vaciarCampos();
                            cargarPreguntasFrecuentes();
                        } else {
                            Log.e("Error", "No se pudo insertar la pregunta frecuente");
                        }
                    }
                    @Override
                    public void onFailure(Call<ApiNodeMySqlRespuesta> call, Throwable t) {
                        Log.e("Error", "Error de conexión: " + t.getMessage());
                    }
                });
    }

    private void cargarPreguntasFrecuentes() {
        Log.d("ala", "Correo a verificar: ");
        Log.d("ala", "si papu" );

        ObtenerPreguntasFrecuentesService obtenerPreguntasFrecuentesService = new ObtenerPreguntasFrecuentesService(getContext());
        obtenerPreguntasFrecuentesService.obtenerPreguntasFrecuentes(new ObtenerPreguntasFrecuentesResponse() {
            @Override
            public void onResponse(List<Long> idsPreguntasFrecuentes,List<String> preguntas, List<String> respuestas, List<Long> idsUsuarios, List<Long> idsEspecialistas) {
                if (!preguntas.isEmpty()) {
                    preguntasList.clear();
                    respuestasList.clear();
                    idsPreguntasFrecuentesList.clear();
                    for (int i = 0; i < preguntas.size(); i++) {
                        String pregunta = preguntas.get(i);
                        String respuesta = respuestas.get(i);
                        long idPreguntaFrecuente = idsPreguntasFrecuentes.get(i);
                        if (respuesta == null) {
                            respuesta = "No disponible";
                        }
                        preguntasList.add(pregunta);
                        respuestasList.add(respuesta);
                        idsPreguntasFrecuentesList.add(idPreguntaFrecuente);
                    }
                    AdaptadorExpandibleListView adaptador = new AdaptadorExpandibleListView(
                            getContext(), preguntasList, respuestasList,idsPreguntasFrecuentesList
                    );
                    preguntasFrecuentesLV.setAdapter(adaptador);
                }
            }
            @Override
            public void onError(String error) {
                Log.e("Error", error);
            }
        });
    }

    private void vaciarCampos() {
        campoNombre.setText("");
        campoCorreo.setText("");
        campoPregunta.setText("");
    }
}
