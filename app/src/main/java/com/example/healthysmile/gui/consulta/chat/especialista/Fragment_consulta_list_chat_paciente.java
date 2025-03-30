package com.example.healthysmile.gui.consulta.chat.especialista;

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

import com.example.healthysmile.controller.consulta.ObtenerPacientesResponseListener;
import com.example.healthysmile.gui.extraAndroid.adaptadores.AdaptadorListaPacientes;
import com.example.healthysmile.R;
import com.example.healthysmile.service.consulta.ObtenerPacientesService;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class Fragment_consulta_list_chat_paciente extends Fragment {


    private ListView listaChatUsuarios;
    private long idUsuario;
    private String tipoUsuario;

    private FirebaseFirestore db;

    private List<String> nombresLV = new ArrayList<>();
    private List<String> correosLV = new ArrayList<>();
    private List<Long> idsPacientesLV = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
        nombresLV.clear();
        correosLV.clear();
        idsPacientesLV.clear();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        long idEspecialista = sharedPreferences.getLong("idEspecialista", -1);

        if (idEspecialista == -1) {
            Toast.makeText(getContext(), "Error: No se encontró el ID del especialista.", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("chats")
                .whereEqualTo("idEspecialista", idEspecialista)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            Long idPaciente = document.getLong("idUsuario");
                            if (idPaciente != null) {
                                idsPacientesLV.add(idPaciente);
                            }
                        }
                        if (idsPacientesLV.isEmpty()) {
                            Toast.makeText(getContext(), "No se encontraron pacientes asociados.", Toast.LENGTH_SHORT).show();
                            return;
                        }
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
        ObtenerPacientesService pacientesService = new ObtenerPacientesService(getContext());

        pacientesService.obtenerPacientesChat(new ObtenerPacientesResponseListener() {
            @Override
            public void onResponse(List<String> nombres, List<String> correos, List<Long> idsPacientes) {
                idsPacientesLV.clear();
                for (int i = 0; i < nombres.size(); i++) {
                    nombresLV.add(nombres.get(i));
                    correosLV.add(correos.get(i));
                    idsPacientesLV.add(idsPacientes.get(i));
                }
                cargarListView();
            }
            @Override
            public void onError(String error) {
                Log.e("Volley", error);
                Toast.makeText(getContext(), "Error al obtener datos de pacientes", Toast.LENGTH_SHORT).show();
            }
        }, idsPacientesLV);
    }

    private void cargarListView() {
        String[] nombresArray = nombresLV.toArray(new String[0]);
        String[] correosArray = correosLV.toArray(new String[0]);
        long[] idsArray = idsPacientesLV.stream().mapToLong(Long::longValue).toArray();

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