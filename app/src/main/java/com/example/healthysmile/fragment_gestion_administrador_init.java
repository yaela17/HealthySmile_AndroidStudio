package com.example.healthysmile;

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

import com.example.healthysmile.Adaptadores.AdaptadorBorrarEspecialista;
import com.example.healthysmile.Adaptadores.AdaptadorListaEspecialistas;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class fragment_gestion_administrador_init extends Fragment {

    private ListView listaChat;
    private long idUsuario;
    private String tipoUsuario;

    private FirebaseFirestore db;

    private List<String> nombres = new ArrayList<>();
    private List<String> especialidades = new ArrayList<>();
    private List<String> descripciones = new ArrayList<>();
    private List<Long> idsEspecialista = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gestion_administrador_init, container, false);

        listaChat = view.findViewById(R.id.consultaListViewBorrarEspecialistas);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        tipoUsuario = sharedPreferences.getString("tipoUsuario", "Paciente");

        // Inicializar Firebase Firestore
        db = FirebaseFirestore.getInstance();

        cargarEspecialistas();


        return view;
    }

    private void cargarEspecialistas() {
        nombres.clear();
        especialidades.clear();
        descripciones.clear();
        idsEspecialista.clear();

        db.collection("usuarios")
                .whereEqualTo("tipoUser", "Especialista") // Filtrar por tipo de usuario
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            try {
                                // Obtener los datos del documento
                                String nombre = document.getString("nomUser");
                                long idUsuario = document.getLong("idUsuario");

                                // Verificar si existe el campo "Especialista" como mapa
                                if (document.contains("Especialista")) {
                                    Object especialistaMap = document.get("Especialista");
                                    if (especialistaMap instanceof Map<?,?>) {
                                        Map<String, Object> especialistaData = (Map<String, Object>) especialistaMap;
                                        String descripcion = (String) especialistaData.get("descripcion");
                                        String especialidad = (String) especialistaData.get("especialidad");

                                        // Agregar a los arreglos
                                        nombres.add(nombre);
                                        idsEspecialista.add(idUsuario);
                                        descripciones.add(descripcion);
                                        especialidades.add(especialidad);
                                    }
                                }
                            } catch (Exception e) {
                                Log.e("Firestore", "Error al procesar documento", e);
                            }
                        }

                        // Pasar los datos al adaptador del ListView
                        cargarListView();
                    } else {
                        Toast.makeText(getContext(), "No se encontraron especialistas.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error al cargar especialistas", e);
                    Toast.makeText(getContext(), "Error al cargar especialistas", Toast.LENGTH_SHORT).show();
                });
    }

    private void cargarListView() {
        // Convertir listas a arreglos para el adaptador
        String[] nombresArray = nombres.toArray(new String[0]);
        String[] especialidadesArray = especialidades.toArray(new String[0]);
        String[] descripcionesArray = descripciones.toArray(new String[0]);
        long[] idsArray = idsEspecialista.stream().mapToLong(Long::longValue).toArray();

        // Crear e inicializar el adaptador
        AdaptadorBorrarEspecialista adaptador = new AdaptadorBorrarEspecialista(
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