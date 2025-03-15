package com.example.healthysmile.gui.consulta;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.healthysmile.gui.Adaptadores.AdaptadorListaEspecialistas;
import com.example.healthysmile.R;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class fragment_consulta_list_chat_especialista extends Fragment {

    private ListView listaChat;
    private long idUsuario;
    private String tipoUsuario;

    private FirebaseFirestore db;

    private List<String> nombres = new ArrayList<>();
    private List<String> especialidades = new ArrayList<>();
    private List<String> descripciones = new ArrayList<>();
    private List<Long> idsEspecialista = new ArrayList<>();

    private static final String URL_OBTENER_ESPECIALISTAS = "http://10.0.2.2:3000/api/obtenerEspecialistasChatsitoAndroid";
    private RequestQueue requestQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consulta_list_chat_especialista, container, false);

        listaChat = view.findViewById(R.id.consultaListViewChatsEspecialistas);
        requestQueue = Volley.newRequestQueue(requireContext());

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        tipoUsuario = sharedPreferences.getString("tipoUsuario", "Paciente");

        // Inicializar Firebase Firestore
        db = FirebaseFirestore.getInstance();

        if (tipoUsuario.equals("Paciente")) {
            cargarEspecialistas();
        }

        return view;
    }

    private void cargarEspecialistas() {
        nombres.clear();
        especialidades.clear();
        descripciones.clear();
        idsEspecialista.clear();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL_OBTENER_ESPECIALISTAS,
                null,
                response -> procesarEspecialistas(response),
                error -> {
                    Log.e("Volley", "Error al obtener especialistas", error);
                    Toast.makeText(getContext(), "Error al cargar especialistas", Toast.LENGTH_SHORT).show();
                }
        );

        requestQueue.add(jsonArrayRequest);
    }

    private void procesarEspecialistas(JSONArray response) {
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject especialista = response.getJSONObject(i);
                Log.d("JSON", "Especialista JSON: " + especialista.toString());
                long idUsuario = especialista.getLong("idUsuario");
                String nombre = especialista.getString("nombre");
                String especialidad = especialista.optString("especialidad", "No especificado");
                String descripcion = especialista.optString("descripcion", "No disponible");

                // Agregar a los arreglos
                nombres.add(nombre);
                idsEspecialista.add(idUsuario);
                especialidades.add(especialidad);
                descripciones.add(descripcion);
            }

            // Pasar los datos al adaptador del ListView
            cargarListView();

        } catch (JSONException e) {
            Log.e("Volley", "Error al procesar JSON", e);
            Toast.makeText(getContext(), "Error al procesar datos de especialistas.", Toast.LENGTH_SHORT).show();
        }
    }


    private void cargarListView() {
        // Convertir listas a arreglos para el adaptador
        String[] nombresArray = nombres.toArray(new String[0]);
        String[] especialidadesArray = especialidades.toArray(new String[0]);
        String[] descripcionesArray = descripciones.toArray(new String[0]);
        long[] idsArray = idsEspecialista.stream().mapToLong(Long::longValue).toArray();

        // Crear e inicializar el adaptador
        AdaptadorListaEspecialistas adaptador = new AdaptadorListaEspecialistas(
                getContext(),
                this,
                nombresArray,
                especialidadesArray,
                descripcionesArray,
                idsArray
        );

        // Asignar el adaptador al ListView
        listaChat.setAdapter(adaptador);
    }
}
