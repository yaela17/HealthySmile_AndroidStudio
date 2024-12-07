package com.example.healthysmile.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthysmile.Adaptadores.AdaptadorRecyclerViewEspecialistas;
import com.example.healthysmile.R;
import com.example.healthysmile.ui.consulta.fragment_cosulta_virtual_init;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.Map;

public class Home extends Fragment implements View.OnClickListener {

    RecyclerView recyclerViewEspecialistasInfo;

    // ArrayLists para almacenar los datos
    ArrayList<String> listaNombres = new ArrayList<>();
    ArrayList<String> listaEspecialidades = new ArrayList<>();
    ArrayList<String> listaCedulas = new ArrayList<>();
    ArrayList<String> listaDescripciones = new ArrayList<>();
    ArrayList<String> listaFotos = new ArrayList<>();
    Button btnConsultaVirtual, btnEducacionDental, btnAyudaYSoporte,btnTiendaVirtual;

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
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("usuarios") // Cambia "usuarios" por el nombre real de tu colección
                .whereEqualTo("tipoUser", "Especialista") // Filtro para especialistas
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        // Obtener datos comunes
                        String nombre = document.getString("nomUser");
                        String fotoPerfil = document.contains("fotoPerfil") ? document.getString("fotoPerfil") : null;

                        // Obtener datos del subcampo "Especialista"
                        if (document.contains("Especialista")) {
                            Object especialistaObj = document.get("Especialista");
                            if (especialistaObj instanceof Map) {
                                Map<String, Object> especialistaMap = (Map<String, Object>) especialistaObj;
                                String especialidad = (String) especialistaMap.get("especialidad");
                                String cedula = (String) especialistaMap.get("cedulaProfesional");
                                String descripcion = (String) especialistaMap.get("descripcion");

                                // Agregar a los ArrayLists
                                listaNombres.add(nombre != null ? nombre : "Sin nombre");
                                listaEspecialidades.add(especialidad != null ? especialidad : "Sin especialidad");
                                listaCedulas.add(cedula != null ? cedula : "Sin cédula");
                                listaDescripciones.add(descripcion != null ? descripcion : "Sin descripción");

                                // Verificar y agregar foto
                                listaFotos.add(fotoPerfil != null ? fotoPerfil : "default");
                            }
                        }
                    }

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
                })
                .addOnFailureListener(e -> Log.e("FirestoreError", "Error al obtener datos: " + e.getMessage()));
    }

    @Override
    public void onClick(View v) {
        NavController navController = NavHostFragment.findNavController(this);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        String tipoUsuario = sharedPreferences.getString("tipoUsuario", "Paciente");
        if(v.getId() == R.id.btnHomeConsultaVirtual){
            if(tipoUsuario.equals("Paciente")){
                navController.navigate(R.id.fragment_consulta_list_chat);
            }else
                navController.navigate(R.id.fragment_consulta_list_chat_paciente);
        }else
            if(v.getId() == R.id.btnHomeEducacionDental){
                navController.navigate(R.id.nav_EducacionDental);
            }else
                if(v.getId() == R.id.btnHomeAyudaYSoporte){
                    navController.navigate(R.id.nav_ayudaYSoporte);
                }else
                    if(v.getId() == R.id.btnHomeTiendaVirtual){
                        navController.navigate(R.id.nav_TiendaVirutal);
                    }
    }
}
