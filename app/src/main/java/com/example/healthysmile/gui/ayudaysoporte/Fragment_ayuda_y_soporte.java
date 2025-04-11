package com.example.healthysmile.gui.ayudaysoporte;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.healthysmile.R;
import com.example.healthysmile.controller.ApiNodeMySqlRespuesta;
import com.example.healthysmile.controller.ayudaYSoporte.BuscarPreguntaResponse;
import com.example.healthysmile.controller.ayudaYSoporte.ObtenerPreguntasFrecuentesResponse;
import com.example.healthysmile.gui.extraAndroid.adaptadores.AdaptadorExpandibleListView;
import com.example.healthysmile.gui.extraAndroid.adaptadores.FormularioAgregarPreguntaFrecuente;
import com.example.healthysmile.gui.tiendavirtual.Fragment_tienda_virtual_buscar_producto_nombre;
import com.example.healthysmile.gui.tiendavirtual.Fragment_tienda_virtual_default;
import com.example.healthysmile.model.requests.CrearPreguntaFrecuenteRequest;
import com.example.healthysmile.repository.FirebaseMessageRepository;
import com.example.healthysmile.repository.NodeApiRetrofitClient;
import com.example.healthysmile.service.ApiNodeMySqlService;
import com.example.healthysmile.service.ayudaYSoporte.BuscarPreguntaFrecuenteService;
import com.example.healthysmile.service.ayudaYSoporte.ObtenerPreguntasFrecuentesService;
import com.example.healthysmile.service.tiendaVirtual.ObtenerProductoPorNombreService;
import com.example.healthysmile.utils.SharedPreferencesHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

public class Fragment_ayuda_y_soporte extends Fragment {

    ExpandableListView preguntasFrecuentesLV;
    FloatingActionButton btnAgregarPregunta;
    SharedPreferencesHelper sharedPreferencesHelper;
    EditText inputBuscarPreguntaFrecuente;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable buscarRunnable;


    // Listas para almacenar las preguntas y respuestas
    private List<String> preguntasList = new ArrayList<>();
    private List<String> respuestasList = new ArrayList<>();
    private List<Long> idsPreguntasFrecuentesList = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ayuda_y_soporte, container, false);
        sharedPreferencesHelper = new SharedPreferencesHelper(getContext());
        btnAgregarPregunta = view.findViewById(R.id.ayuda_y_soporte_action_button_pregunta_frecuente);
        preguntasFrecuentesLV = view.findViewById(R.id.expandableListViewPreguntasFrecuentes);
        inputBuscarPreguntaFrecuente = view.findViewById(R.id.ayuda_y_soporte_input_search);


        btnAgregarPregunta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FormularioAgregarPreguntaFrecuente formulario = new FormularioAgregarPreguntaFrecuente(getContext());
                formulario.show(getParentFragmentManager(), "formulario_pregunta");
            }
        });

        inputBuscarPreguntaFrecuente.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable editable) {
                // Cancelar cualquier búsqueda previa pendiente
                handler.removeCallbacks(buscarRunnable);

                // Solo ejecutar la búsqueda si el texto no está vacío
                if (editable.length() > 0) {
                    // Crear un nuevo Runnable para ejecutar la búsqueda después de 1 segundo
                    buscarRunnable = new Runnable() {
                        @Override
                        public void run() {
                            buscarPreguntaFrecuente();
                        }
                    };
                    // Esperar 1 segundo (1000 ms) antes de ejecutar la búsqueda
                    handler.postDelayed(buscarRunnable, 500);
                }
            }
        });

        cargarPreguntasFrecuentes();
        return view;
    }

    private void buscarPreguntaFrecuente(){
        String query = inputBuscarPreguntaFrecuente.getText().toString().trim();
        if (!query.isEmpty()) {
            BuscarPreguntaFrecuenteService service = new BuscarPreguntaFrecuenteService(getContext());

            service.buscarPreguntaFrecuente(query, new BuscarPreguntaResponse() {
                @Override
                public void onResponse(List<Long> idPreguntasFrecuentes, List<String> preguntas, List<String> respuestas, List<Long> idsUsuarios, List<Long> idsEspecialistas) {
                    AdaptadorExpandibleListView adaptadorExpandibleListView = new AdaptadorExpandibleListView(getContext(),preguntas,respuestas,idPreguntasFrecuentes);
                    preguntasFrecuentesLV.setAdapter(adaptadorExpandibleListView);
                }
                @Override
                public void onPreguntasNoEncontradas(String mensaje) {
                    if (getActivity() != null && isAdded()) {
                        Toast.makeText(getActivity(), "Mensaje: " + mensaje, Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onError(String error) {
                    if (getActivity() != null && isAdded()) {
                        Toast.makeText(getActivity(), "Error: " + error, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(requireContext(), "Por favor ingrese un texto para buscar", Toast.LENGTH_SHORT).show();
        }
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
                        //campoCorreo.setError("Correo no registrado en la aplicación");
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

    public void insertarPreguntaFrecuente(long idUsuario, String pregunta) {

        CrearPreguntaFrecuenteRequest crearPreguntaFrecuenteRequest = new CrearPreguntaFrecuenteRequest((int) idUsuario,pregunta);
        ApiNodeMySqlService apiService = NodeApiRetrofitClient.getApiService();

        apiService.crearPreguntaFrecuente(crearPreguntaFrecuenteRequest)
                .enqueue(new retrofit2.Callback<ApiNodeMySqlRespuesta>() {
                    @Override
                    public void onResponse(Call<ApiNodeMySqlRespuesta> call, Response<ApiNodeMySqlRespuesta> response) {
                        if (response.isSuccessful()) {

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

    public void cargarPreguntasFrecuentes() {
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
}
