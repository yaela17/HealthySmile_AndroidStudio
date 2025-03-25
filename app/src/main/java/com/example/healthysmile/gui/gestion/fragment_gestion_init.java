package com.example.healthysmile.gui.gestion;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.healthysmile.R;
import com.example.healthysmile.gui.extraAndroid.settings.default_fragment_settings;
import com.example.healthysmile.gui.extraAndroid.settings.fragmentProfileImageSelector;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Map;

public class fragment_gestion_init extends Fragment {

    String tipoUsuario;
    BottomNavigationView bottomNavigationView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gestion_administrador_init, container, false);

        bottomNavigationView = view.findViewById(R.id.bottom_navigation_gestion);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        tipoUsuario = sharedPreferences.getString("tipoUsuario", "Paciente");

        if (tipoUsuario.equals("Paciente")) {
            bottomNavigationView.getMenu().clear();
            bottomNavigationView.inflateMenu(R.menu.menu_bottom_nav_paciente);
            loadDefaultFragment(new fragment_gestion_paciente_citas());
        } else{
            bottomNavigationView.getMenu().clear();
            bottomNavigationView.inflateMenu(R.menu.menu_bottom_nav_especialista);
            loadDefaultFragment(new fragment_gestion_especialista_citas());
        }

        // Dentro de tu método onCreateView
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Map<Integer, Fragment> fragmentMap = new HashMap<>();
            fragmentMap.put(R.id.nav_bottom_paciente_citas, new fragment_gestion_paciente_citas());
            fragmentMap.put(R.id.nav_bottom_paciente_compras, new fragment_gestion_paciente_compras());
            fragmentMap.put(R.id.nav_bottom_especialista_citas, new fragment_gestion_especialista_citas());
            fragmentMap.put(R.id.nav_bottom_especialista_productos, new fragment_gestion_especialista_productos());
            fragmentMap.put(R.id.nav_bottom_admiministrador_citas, new fragment_gestion_especialista_citas());
            fragmentMap.put(R.id.nav_bottom_administrador_productos, new fragment_gestion_administrador_productos());
            fragmentMap.put(R.id.nav_bottom_administrador_especialistas, new fragment_gestion_administrador_especialistas());

            Fragment selectedFragment = fragmentMap.get(item.getItemId());

            if (selectedFragment != null) {
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.frame_gestion_option_bottom_navigation_view_selected, selectedFragment)
                        .commit();
            }

            return true;
        });


        return view;
    }

    private void loadDefaultFragment(Fragment defaultFragment) {
        getChildFragmentManager().beginTransaction()
                .replace(R.id.frame_gestion_option_bottom_navigation_view_selected, defaultFragment)
                .commit();
    }

}