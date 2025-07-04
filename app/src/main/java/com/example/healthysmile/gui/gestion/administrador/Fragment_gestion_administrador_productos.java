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

public class Fragment_gestion_administrador_productos extends Fragment {

    private FloatingActionMenu fabMenu1;
    private FloatingActionButton fabVerProductos, fabAgregarProducto,fabEliminarProducto;
    SharedPreferencesHelper sharedPreferencesHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sharedPreferencesHelper = new SharedPreferencesHelper(getContext());
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gestion_administrador_productos, container, false);

        fabMenu1 = view.findViewById(R.id.fab_menu1);
        fabVerProductos = view.findViewById(R.id.fab_ver_productos);
        fabAgregarProducto = view.findViewById(R.id.fab_agregar_producto);
        fabEliminarProducto = view.findViewById(R.id.fab_eliminar_producto);

        // Set click listeners
        fabVerProductos.setOnClickListener(v -> {
            sharedPreferencesHelper.guardarAccionSeleccionadaLVProductos(false);
            cargarFragment(new Fragment_mostrar_productos_administrador());
            fabMenu1.close(true);
        });
        fabAgregarProducto.setOnClickListener(v -> {
            cargarFragment(new Fragment_form_agregar_producto_administrador());
            fabMenu1.close(true); // Cierra el menú después de seleccionar
        });
        fabEliminarProducto.setOnClickListener(v -> {
            sharedPreferencesHelper.guardarAccionSeleccionadaLVProductos(true);
            cargarFragment(new Fragment_mostrar_productos_administrador());
            fabMenu1.close(true); // Cierra el menú después de seleccionar
        });
        sharedPreferencesHelper.guardarAccionSeleccionadaLVProductos(false);
        cargarFragment(new Fragment_mostrar_productos_administrador());
        return view;
    }

    public void cargarFragment(Fragment fragment) {
        getChildFragmentManager().beginTransaction()
                .replace(R.id.frame_gestion_administrador_productos_crud_selected, fragment)
                .commit();
    }

}
