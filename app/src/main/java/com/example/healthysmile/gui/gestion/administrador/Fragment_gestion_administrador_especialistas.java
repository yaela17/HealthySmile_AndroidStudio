package com.example.healthysmile.gui.gestion.administrador;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.healthysmile.R;

import com.example.healthysmile.utils.SharedPreferencesHelper;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

public class Fragment_gestion_administrador_especialistas extends Fragment {

    private FloatingActionMenu fabMenu2;
    private FloatingActionButton fabVerEspecialistas, fabAgregarEspecialistas,fabEliminarEspecialistas;
    SharedPreferencesHelper sharedPreferencesHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sharedPreferencesHelper = new SharedPreferencesHelper(getContext());
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gestion_administrador_especialistas, container, false);

        fabMenu2 = view.findViewById(R.id.fab_menu2);
        fabVerEspecialistas = view.findViewById(R.id.fab_ver_especialistas);
        fabAgregarEspecialistas = view.findViewById(R.id.fab_agregar_especialistas);
        fabEliminarEspecialistas = view.findViewById(R.id.fab_eliminar_especialista);

        // Set click listeners
        fabVerEspecialistas.setOnClickListener(v -> {
            sharedPreferencesHelper.guardarAccionSeleccionadaLVEspecialista(false);
            cargarFragment(new Fragment_mostrar_productos_administrador());
            fabMenu2.close(true);
        });
        fabAgregarEspecialistas.setOnClickListener(v -> {
            cargarFragment(new Fragment_form_agregar_producto_administrador());
            fabMenu2.close(true); // Cierra el menú después de seleccionar
        });
        fabEliminarEspecialistas.setOnClickListener(v -> {
            sharedPreferencesHelper.guardarAccionSeleccionadaLVProductos(true);
            cargarFragment(new Fragment_mostrar_productos_administrador());
            fabMenu2.close(true); // Cierra el menú después de seleccionar
        });
        sharedPreferencesHelper.guardarAccionSeleccionadaLVProductos(false);
        cargarFragment(new Fragment_mostrar_productos_administrador());
        return view;
    }

    public void cargarFragment(Fragment fragment) {
        getChildFragmentManager().beginTransaction()
                .replace(R.id.frame_gestion_administrador_especialistas_crud_selected, fragment)
                .commit();
    }
}