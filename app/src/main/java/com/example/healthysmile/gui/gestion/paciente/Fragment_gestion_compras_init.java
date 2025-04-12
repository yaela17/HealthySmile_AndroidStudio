package com.example.healthysmile.gui.gestion.paciente;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.healthysmile.R;

public class Fragment_gestion_compras_init extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gestion_compras_init, container, false);

        // Cargar fragmento por defecto
        cargarFragment(new Fragment_gestion_paciente_compras());

        return view;
    }

    public void cargarFragment(Fragment fragment) {
        getChildFragmentManager().beginTransaction()
                .replace(R.id.frame_gestion_compras, fragment)
                .commit();
    }
}
