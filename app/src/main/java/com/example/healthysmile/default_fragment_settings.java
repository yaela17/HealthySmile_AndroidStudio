package com.example.healthysmile;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

public class default_fragment_settings extends Fragment implements AdapterView.OnItemClickListener {

    ListView listaDefaultSettings;
    AdaptadorOpcionesPerfilListView adaptador;
    Drawable[] listLeftIcon;
    String[] listTitleInputFile = {
            "Nombre especialista", "Holita titulazo 2", "Hola 3", "Titulo 4", "Titulo 5", "Titulo 6",
            "Titulo 7", "Titulo 8", "Titulo 9", "Titulo 10", "Titulo 11", "Titulo 12",
            "Titulo 13", "Titulo 14", "Titulo 15", "Titulo 16", "Titulo 17", "Titulo 18",
            "Titulo 19", "Titulo 20"
    };
    String[] listDescriptionInputFile = {
            "Descripcion especialista", "Holita descripcionzasa 2", "Hola 3", "Descripcion 4", "Descripcion 5", "Descripcion 6",
            "Descripcion 7", "Descripcion 8", "Descripcion 9", "Descripcion 10", "Descripcion 11", "Descripcion 12",
            "Descripcion 13", "Descripcion 14", "Descripcion 15", "Descripcion 16", "Descripcion 17", "Descripcion 18",
            "Descripcion 19", "Descripcion 20"
    };
    Drawable[] lisRightIcon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflamos la vista del fragmento
        View view = inflater.inflate(R.layout.fragment_default_settings, container, false);

        // Aquí es donde ya tienes acceso al contexto y puedes usar getResources()
        listLeftIcon = new Drawable[]{
                getResources().getDrawable(R.drawable.default_photo_perfil_especialista),
                getResources().getDrawable(R.drawable.icon_ayuda_y_soporte),
                getResources().getDrawable(R.drawable.icon_ayuda_y_soporte),
                getResources().getDrawable(R.drawable.icon_visible_off),
                getResources().getDrawable(R.drawable.icon_ayuda_y_soporte),
                getResources().getDrawable(R.drawable.icon_ayuda_y_soporte),
                getResources().getDrawable(R.drawable.icon_visible_off),
                getResources().getDrawable(R.drawable.icon_ayuda_y_soporte),
                getResources().getDrawable(R.drawable.icon_ayuda_y_soporte),
                getResources().getDrawable(R.drawable.icon_visible_off),
                getResources().getDrawable(R.drawable.icon_ayuda_y_soporte),
                getResources().getDrawable(R.drawable.icon_ayuda_y_soporte),
                getResources().getDrawable(R.drawable.icon_visible_off),
                getResources().getDrawable(R.drawable.icon_ayuda_y_soporte),
                getResources().getDrawable(R.drawable.icon_ayuda_y_soporte),
                getResources().getDrawable(R.drawable.icon_visible_off),
                getResources().getDrawable(R.drawable.icon_ayuda_y_soporte),
                getResources().getDrawable(R.drawable.icon_ayuda_y_soporte),
                getResources().getDrawable(R.drawable.icon_visible_off),
                getResources().getDrawable(R.drawable.icon_ayuda_y_soporte)
        };

        lisRightIcon = new Drawable[]{
                null,
                null,
               null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null
        };

        // Configurar el ListView y el adaptador
        listaDefaultSettings = view.findViewById(R.id.listViewDefaultSettings);
        adaptador = new AdaptadorOpcionesPerfilListView(getContext(), listLeftIcon, listTitleInputFile, listDescriptionInputFile, lisRightIcon);
        listaDefaultSettings.setAdapter(adaptador);
        listaDefaultSettings.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.settingsFrameListViewContainer, new settings_perfil());
            transaction.addToBackStack(null);
            transaction.commit();
        }

    }
}
