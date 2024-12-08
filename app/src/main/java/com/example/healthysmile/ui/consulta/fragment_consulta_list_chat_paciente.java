package com.example.healthysmile.ui.consulta;

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

import com.example.healthysmile.Adaptadores.AdaptadorListaPacientes;
import com.example.healthysmile.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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

        // Obtener el ID del especialista desde SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        long idEspecialista = sharedPreferences.getLong("idUsuario", -1);

        if (idEspecialista == -1) {
            Toast.makeText(getContext(), "Error: No se encontró el ID del especialista.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Consultar la colección de chats para obtener los IDs de los pacientes asociados al especialista
        db.collection("chats")
                .whereEqualTo("idEspecialista", idEspecialista)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            try {
                                Long idPaciente = document.getLong("idUsuario");
                                if (idPaciente != null) {
                                    idsPacientes.add(idPaciente);
                                }
                            } catch (Exception e) {
                                Log.e("Firestore", "Error al procesar documento de chat", e);
                            }
                        }

                        if (idsPacientes.isEmpty()) {
                            Toast.makeText(getContext(), "No se encontraron pacientes asociados." + idEspecialista, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Consultar la colección de usuarios para obtener los detalles de los pacientes
                        db.collection("usuarios")
                                .whereEqualTo("tipoUser", "Paciente")
                                .get()
                                .addOnSuccessListener(userSnapshots -> {
                                    if (!userSnapshots.isEmpty()) {
                                        for (DocumentSnapshot userDoc : userSnapshots) {
                                            try {
                                                Long idUsuario = userDoc.getLong("idUsuario");
                                                if (idUsuario != null && idsPacientes.contains(idUsuario)) {
                                                    String nombre = userDoc.getString("nomUser");
                                                    String correo = userDoc.getString("correoUser");

                                                    nombres.add(nombre != null ? nombre : "Desconocido");
                                                    correos.add(correo != null ? correo : "Sin correo");
                                                }
                                            } catch (Exception e) {
                                                Log.e("Firestore", "Error al procesar documento de usuario", e);
                                            }
                                        }

                                        // Pasar los datos al adaptador del ListView
                                        cargarListView();
                                    } else {
                                        Toast.makeText(getContext(), "No se encontraron pacientes registrados.", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("Firestore", "Error al cargar pacientes", e);
                                    Toast.makeText(getContext(), "Error al cargar pacientes", Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Toast.makeText(getContext(), "No se encontraron chats asociados al especialista.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error al consultar chats", e);
                    Toast.makeText(getContext(), "Error al consultar chats", Toast.LENGTH_SHORT).show();
                });
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