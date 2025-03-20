package com.example.healthysmile.gui.consulta;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.healthysmile.gui.extraAndroid.adaptadores.AdaptadorListaPacientes;
import com.example.healthysmile.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class fragment_consulta_list_chat_paciente extends Fragment {


    private ListView listaChatUsuarios;
    private long idUsuario;
    private String tipoUsuario;

    private FirebaseFirestore db;

    private List<String> nombres = new ArrayList<>();
    private List<String> correos = new ArrayList<>();
    private List<Long> idsPacientes = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_consulta_list_chat_paciente, container, false);

        listaChatUsuarios = view.findViewById(R.id.consultaListViewChatsPaciente);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        tipoUsuario = sharedPreferences.getString("tipoUsuario", "Paciente");

        // Inicializar Firebase Firestore
        db = FirebaseFirestore.getInstance();

        if (tipoUsuario.equals("Especialista")) {
            cargarPacientes();
        }

        return view;
    }

    private void cargarPacientes() {
        nombres.clear();
        correos.clear();
        idsPacientes.clear();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        long idEspecialista = sharedPreferences.getLong("idEspecialista", -1);

        if (idEspecialista == -1) {
            Toast.makeText(getContext(), "Error: No se encontró el ID del especialista.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Consultar Firebase para obtener los IDs de los pacientes con los que ha chateado el especialista
        db.collection("chats")
                .whereEqualTo("idEspecialista", idEspecialista)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            Long idPaciente = document.getLong("idUsuario");
                            if (idPaciente != null) {
                                idsPacientes.add(idPaciente);
                            }
                        }

                        if (idsPacientes.isEmpty()) {
                            Toast.makeText(getContext(), "No se encontraron pacientes asociados.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Enviar la lista de IDs al backend MySQL
                        obtenerDetallesPacientes();
                    } else {
                        Toast.makeText(getContext(), "No se encontraron chats asociados al especialista.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error al consultar chats", e);
                    Toast.makeText(getContext(), "Error al consultar chats", Toast.LENGTH_SHORT).show();
                });
    }

    private void obtenerDetallesPacientes() {
        // Convertir la lista de IDs en un string separado por comas
        String idsString = android.text.TextUtils.join(",", idsPacientes);

        String url = "http://10.0.2.2:3000/api/obtenerPacientesChatAndroid?idsUsuarios=" + idsString;

        // Realizar la petición HTTP para obtener los datos de los pacientes desde MySQL
        RequestQueue queue = Volley.newRequestQueue(getContext());

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject paciente = response.getJSONObject(i);
                            long id = paciente.getLong("idUsuario");
                            String nombre = paciente.getString("nombre");
                            String correo = paciente.getString("correo");

                            nombres.add(nombre);
                            correos.add(correo);
                            idsPacientes.add(id);
                        }

                        // Pasar los datos al adaptador del ListView
                        cargarListView();
                    } catch (Exception e) {
                        Log.e("Volley", "Error al procesar JSON", e);
                        Toast.makeText(getContext(), "Error al procesar datos", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("Volley", "Error en la petición HTTP", error);
                    Toast.makeText(getContext(), "Error al obtener datos de pacientes", Toast.LENGTH_SHORT).show();
                });

        queue.add(request);
    }

    private void cargarListView() {
        // Convertir listas a arreglos para el adaptador
        String[] nombresArray = nombres.toArray(new String[0]);
        String[] correosArray = correos.toArray(new String[0]);
        long[] idsArray = idsPacientes.stream().mapToLong(Long::longValue).toArray();

        // Crear e inicializar el adaptador
        AdaptadorListaPacientes adaptador = new AdaptadorListaPacientes(
                getContext(),
                correosArray,
                idsArray,
                this,
                nombresArray
        );

        listaChatUsuarios.setAdapter(adaptador);
    }
}