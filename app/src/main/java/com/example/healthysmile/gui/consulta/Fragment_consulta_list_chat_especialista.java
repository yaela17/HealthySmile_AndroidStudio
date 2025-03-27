package com.example.healthysmile.gui.consulta;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.healthysmile.gui.extraAndroid.adaptadores.AdaptadorListaEspecialistas;
import com.example.healthysmile.R;
import com.example.healthysmile.service.EspecialistaService;
import com.example.healthysmile.controller.consulta.EspecialistaResponseListenerChat;


import java.util.List;

public class Fragment_consulta_list_chat_especialista extends Fragment {

    private ListView listaChat;
    private long idUsuario;
    private String tipoUsuario;

    private List<String> nombres;
    private List<String> especialidades;
    private List<String> descripciones;
    private List<Long> idsEspecialista;

    private EspecialistaService especialistaService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consulta_list_chat_especialista, container, false);

        listaChat = view.findViewById(R.id.consultaListViewChatsEspecialistas);

        // Inicializar el servicio y SharedPreferences
        especialistaService = new EspecialistaService(requireContext());
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        tipoUsuario = sharedPreferences.getString("tipoUsuario", "Paciente");

        if (tipoUsuario.equals("Paciente")) {
            cargarEspecialistas();
        }

        return view;
    }

    private void cargarEspecialistas() {
        especialistaService.obtenerEspecialistasChat(new EspecialistaResponseListenerChat() {
            @Override
            public void onResponse(List<String> nombres, List<String> especialidades, List<String> descripciones, List<Long> idsEspecialista) {
                Fragment_consulta_list_chat_especialista.this.nombres = nombres;
                Fragment_consulta_list_chat_especialista.this.especialidades = especialidades;
                Fragment_consulta_list_chat_especialista.this.descripciones = descripciones;
                Fragment_consulta_list_chat_especialista.this.idsEspecialista = idsEspecialista;
                cargarListView();
            }
            @Override
            public void onError(String error) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
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
