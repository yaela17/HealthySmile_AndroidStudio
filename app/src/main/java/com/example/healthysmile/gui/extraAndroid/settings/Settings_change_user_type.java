package com.example.healthysmile.gui.extraAndroid.settings;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.healthysmile.R;


public class Settings_change_user_type extends Fragment implements RadioGroup.OnCheckedChangeListener {

    private RadioGroup rgTipoUsuario;
    private RadioButton rbPaciente;
    private RadioButton rbOtroTipoUsuario;
    String tipoUsuario, otroTipoUsuario;
    Long nivelPermisos, otroNivelPermisos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings_change_user_type, container, false);
        rgTipoUsuario = view.findViewById(R.id.rgTipoUsuario);
        rbPaciente = view.findViewById(R.id.rbPaciente);
        rbOtroTipoUsuario = view.findViewById(R.id.rbOtroTipoUsuario);

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("AppPrefs", requireContext().MODE_PRIVATE);

        nivelPermisos = sharedPreferences.getLong("nivelPermisos", 1);
        otroNivelPermisos = sharedPreferences.getLong("otroNivelPermisos", -1);
        tipoUsuario = sharedPreferences.getString("tipoUsuario", "Paciente");
        otroTipoUsuario = sharedPreferences.getString("otroTiposUsuario", null);

        if(otroTipoUsuario == null){
            rbOtroTipoUsuario.setVisibility(View.GONE);
        }else{
            rbOtroTipoUsuario.setText(tipoUsuario);
            if(rbPaciente.getText().equals(tipoUsuario)){
                rbOtroTipoUsuario.setText(otroTipoUsuario);
            }
        }

        if(rbPaciente.getText().equals(tipoUsuario)){
            rbPaciente.setChecked(true);
        }else{
            rbOtroTipoUsuario.setChecked(true);
        }

        rgTipoUsuario.setOnCheckedChangeListener(this);

        return view;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        SharedPreferences sp = requireContext().getSharedPreferences("AppPrefs", requireContext().MODE_PRIVATE);

        // Valores actuales en preferencias
        String  tipoUsuarioActual        = sp.getString("tipoUsuario",     null);
        long    nivelPermisosActual      = sp.getLong  ("nivelPermisos",   -1);
        String  otroTipoUsuarioActual    = sp.getString("otroTiposUsuario", null);
        long    otroNivelPermisosActual  = sp.getLong  ("otroNivelPermisos", -1);

        // Variables que escribiremos
        String nuevoTipoUsuario        = tipoUsuarioActual;
        long   nuevoNivelPermisos      = nivelPermisosActual;
        String nuevoOtroTipoUsuario    = otroTipoUsuarioActual;
        long   nuevoOtroNivelPermisos  = otroNivelPermisosActual;

        if (checkedId == R.id.rbPaciente && otroTipoUsuarioActual != null) {
            nuevoTipoUsuario        = "Paciente";
            nuevoNivelPermisos      = 1L;
            nuevoOtroTipoUsuario    = tipoUsuarioActual;
            nuevoOtroNivelPermisos  = nivelPermisosActual;
        } else if (checkedId == R.id.rbOtroTipoUsuario) {
            nuevoTipoUsuario        = otroTipoUsuarioActual;
            nuevoNivelPermisos      = otroNivelPermisosActual;
            nuevoOtroTipoUsuario    = tipoUsuarioActual;
            nuevoOtroNivelPermisos  = nivelPermisosActual;
        }

        // Guardar en SharedPreferences
        sp.edit()
                .putString("tipoUsuario",        nuevoTipoUsuario)
                .putLong  ("nivelPermisos",      nuevoNivelPermisos)
                .putString("otroTiposUsuario",   nuevoOtroTipoUsuario)
                .putLong  ("otroNivelPermisos",  nuevoOtroNivelPermisos)
                .apply();

        // Imprimir los cuatro valores resultantes
        Log.d("UserType",
                "tipoUsuario="       + nuevoTipoUsuario       + ", " +
                        "nivelPermisos="     + nuevoNivelPermisos     + ", " +
                        "otroTiposUsuario="  + nuevoOtroTipoUsuario   + ", " +
                        "otroNivelPermisos=" + nuevoOtroNivelPermisos);
    }

}