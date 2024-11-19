package com.example.healthysmile;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


public class settings_perfil extends Fragment implements AdapterView.OnItemClickListener {

    ListView listaDefaultSettings;
    AdaptadorPerfilListView adaptador;
    Drawable[] listLeftIcon;
    String[] listTitleInputFile = {
            "", "Nombre", "Correo", "Especialidad/Tipo usuario"
    };
    String[] listDescriptionInputFile = {
            "", "Desc2", "Desc 3", "Descripcion 4"
    };
    Drawable[] lisRightIcon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflamos la vista del fragmento
        View view = inflater.inflate(R.layout.fragment_settings_perfil, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Perfil");

        // Aquí es donde ya tienes acceso al contexto y puedes usar getResources()
        listLeftIcon = new Drawable[]{
                getResources().getDrawable(R.drawable.default_photo_perfil_especialista),
                getResources().getDrawable(R.drawable.icon_person),
                getResources().getDrawable(R.drawable.icon_email),
                getResources().getDrawable(R.drawable.icon_cedulaprofesional)
        };

        lisRightIcon = new Drawable[]{null,
                getResources().getDrawable(R.drawable.icon_pencil_change_input),
                getResources().getDrawable(R.drawable.icon_pencil_change_input),
                getResources().getDrawable(R.drawable.icon_pencil_change_input)};

        // Configurar el ListView y el adaptador
        listaDefaultSettings = view.findViewById(R.id.listViewSettingsPerfil);
        adaptador = new AdaptadorPerfilListView(getContext(), listLeftIcon, listTitleInputFile, listDescriptionInputFile, lisRightIcon);
        listaDefaultSettings.setAdapter(adaptador);
        listaDefaultSettings.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}