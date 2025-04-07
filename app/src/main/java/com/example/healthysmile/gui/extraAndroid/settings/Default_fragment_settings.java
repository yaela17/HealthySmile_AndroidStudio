package com.example.healthysmile.gui.extraAndroid.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.healthysmile.gui.extraAndroid.adaptadores.AdaptadorOpcionesPerfilListView;
import com.example.healthysmile.R;

public class Default_fragment_settings extends Fragment implements AdapterView.OnItemClickListener {

    ListView listaDefaultSettings;
    AdaptadorOpcionesPerfilListView adaptador;
    Drawable[] listLeftIcon;
    String[] listTitleInputFile;
    String[] listDescriptionInputFile;
    Drawable[] lisRightIcon;
    String nombre, correo, foto, tipoUsuario;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflamos la vista del fragmento
        View view = inflater.inflate(R.layout.fragment_default_settings, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        long idUsuario = sharedPreferences.getLong("idUsuario", 0);
        nombre = sharedPreferences.getString("nombreUsuario", null);
        correo = sharedPreferences.getString("correoUsuario", null);
        foto = sharedPreferences.getString("fotoUsuario", null);
        tipoUsuario = sharedPreferences.getString("tipoUsuario", null);

        if ("Paciente".equals(tipoUsuario)) {
        } else {
            // Si es Especialista, obtenemos los datos específicos
            long idEspecialista = sharedPreferences.getLong("idEspecialista", 0);
            String cedulaProfesional = sharedPreferences.getString("cedulaProfesionalEsp", null);
            String descripcion = sharedPreferences.getString("descripcionEsp", null);
            String especialidad = sharedPreferences.getString("especialidadEsp", null);
        }

        // Inicializamos los arrays de iconos
        listLeftIcon = new Drawable[]{
                getResources().getDrawable(R.drawable.dentistaaa),
                getResources().getDrawable(R.drawable.icon_person)
        };

        lisRightIcon = new Drawable[]{
                null,null
        };

        // Configurar el ListView y el adaptador
        listTitleInputFile = new String[]{
                nombre, "sigma"
        };

        listDescriptionInputFile = new String[]{
                correo, "sigma"
        };

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
            transaction.replace(R.id.settingsFrameListViewContainer, new Settings_perfil());
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

}

