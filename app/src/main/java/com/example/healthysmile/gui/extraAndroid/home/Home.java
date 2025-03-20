package com.example.healthysmile.gui.extraAndroid.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthysmile.gui.extraAndroid.adaptadores.AdaptadorRecyclerViewEspecialistas;
import com.example.healthysmile.R;
import com.example.healthysmile.service.EspecialistaService;
import com.example.healthysmile.controller.extraAndroid.EspecialistaResponseListenerInicio;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;
import java.util.List;

public class Home extends Fragment implements View.OnClickListener {

    RecyclerView recyclerViewEspecialistasInfo;

    // ArrayLists para almacenar los datos
    ArrayList<String> listaNombres = new ArrayList<>();
    ArrayList<String> listaEspecialidades = new ArrayList<>();
    ArrayList<String> listaCedulas = new ArrayList<>();
    ArrayList<String> listaDescripciones = new ArrayList<>();
    ArrayList<String> listaFotos = new ArrayList<>();
    Button btnConsultaVirtual, btnEducacionDental, btnAyudaYSoporte, btnTiendaVirtual;

    private EspecialistaService especialistaService;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerViewEspecialistasInfo = view.findViewById(R.id.recyclerViewEspecialistasHome);

        // Configurar el FlexboxLayoutManager
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());
        layoutManager.setFlexDirection(FlexDirection.ROW); // Flujo de izquierda a derecha
        layoutManager.setJustifyContent(JustifyContent.FLEX_START); // Alineación al inicio
        layoutManager.setFlexWrap(FlexWrap.WRAP); // Permitir que los elementos salten a la siguiente línea
        recyclerViewEspecialistasInfo.setLayoutManager(layoutManager);

        especialistaService = new EspecialistaService(getContext());

        // Obtener datos y configurar adaptador
        obtenerDatosEspecialistas();

        btnConsultaVirtual = view.findViewById(R.id.btnHomeConsultaVirtual);
        btnTiendaVirtual = view.findViewById(R.id.btnHomeTiendaVirtual);
        btnAyudaYSoporte = view.findViewById(R.id.btnHomeAyudaYSoporte);
        btnEducacionDental = view.findViewById(R.id.btnHomeEducacionDental);

        btnConsultaVirtual.setOnClickListener(this);
        btnTiendaVirtual.setOnClickListener(this);
        btnEducacionDental.setOnClickListener(this);
        btnAyudaYSoporte.setOnClickListener(this);

        return view;
    }

    private void obtenerDatosEspecialistas() {
        especialistaService.obtenerEspecialistasInicio(new EspecialistaResponseListenerInicio() {
            @Override
            public void onResponseInicio(List<String> nombres, List<String> especialidades, List<String> descripciones, List<Long> idsEspecialista, List<String> cedulasProfesional,List<String>fotos) {
                // Llenar las listas con los datos obtenidos del servicio
                listaNombres.clear();
                listaEspecialidades.clear();
                listaDescripciones.clear();
                listaCedulas.clear();
                listaFotos.clear();

                listaNombres.addAll(nombres);
                listaEspecialidades.addAll(especialidades);
                listaDescripciones.addAll(descripciones);
                listaCedulas.addAll(cedulasProfesional);
                listaFotos.addAll(fotos);

                // Configurar el adaptador con los datos obtenidos
                AdaptadorRecyclerViewEspecialistas adaptador = new AdaptadorRecyclerViewEspecialistas(
                        getContext(),
                        listaNombres,
                        listaEspecialidades,
                        listaCedulas,
                        listaDescripciones,
                        listaFotos
                );
                recyclerViewEspecialistasInfo.setAdapter(adaptador);
            }

            @Override
            public void onErrorInicio(String error) {
                Log.e("Home", "Error al obtener especialistas: " + error);
            }
        });
    }

    @Override
    public void onClick(View v) {
        NavController navController = NavHostFragment.findNavController(this);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        String tipoUsuario = sharedPreferences.getString("tipoUsuario", "Paciente");
        if (v.getId() == R.id.btnHomeConsultaVirtual) {
            if (tipoUsuario.equals("Paciente")) {
                navController.navigate(R.id.fragment_consulta_list_chat);
            } else {
                navController.navigate(R.id.fragment_consulta_list_chat_paciente);
            }
        } else if (v.getId() == R.id.btnHomeEducacionDental) {
            navController.navigate(R.id.nav_EducacionDental);
        } else if (v.getId() == R.id.btnHomeAyudaYSoporte) {
            navController.navigate(R.id.nav_ayudaYSoporte);
        } else if (v.getId() == R.id.btnHomeTiendaVirtual) {
            navController.navigate(R.id.nav_TiendaVirutal);
        }
    }
}
