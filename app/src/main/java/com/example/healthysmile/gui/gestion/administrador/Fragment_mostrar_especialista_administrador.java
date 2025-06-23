package com.example.healthysmile.gui.gestion.administrador;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.healthysmile.R;
import com.example.healthysmile.service.EspecialistaService;
import com.example.healthysmile.utils.SharedPreferencesHelper;

public class Fragment_mostrar_especialista_administrador extends Fragment implements AdapterView.OnItemClickListener {

    ListView especialistasLV;
    EspecialistaService especialistaService;
    SharedPreferencesHelper sharedPreferencesHelper;
    boolean eliminar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mostrar_especialista_administrador, container, false);

        especialistasLV = view.findViewById(R.id.mostrar_especialistas_disponibles_administrador_list_view);
        especialistaService = new EspecialistaService(requireContext());
        sharedPreferencesHelper = new SharedPreferencesHelper(getContext());

        eliminar = sharedPreferencesHelper.obtenerAccionSeleccionada();

        especialistasLV.setOnItemClickListener(this);


        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}