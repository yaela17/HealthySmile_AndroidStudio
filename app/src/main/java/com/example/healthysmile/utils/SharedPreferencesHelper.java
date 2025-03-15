package com.example.healthysmile.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {

    private Context context;

    public SharedPreferencesHelper(Context context) {
        this.context = context;
    }

    public void guardarPaciente(String nombreUsuario,String correoUsuario,String tipoUsuario, long nivelPermisos){
        SharedPreferences sharedPreferences = context.getSharedPreferences("AppPrefs", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("nombreUsuario", nombreUsuario);
        editor.putString("correoUsuario", correoUsuario);
        editor.putString("tipoUsuario",tipoUsuario);
        editor.putLong("nivelPermisos",nivelPermisos);
        editor.putString("fotoUsuario",null);
        editor.putBoolean("sesionActiva",true);
        editor.apply();
    }

    public void guardarEspecialista(String nombreUsuario,
                                    String correoUsuario, String tipoUsuario, long nivelPermisos,
                                    String cedulaProfesionalEsp, String descripcionEsp, String especialidadEsp){
        guardarPaciente(nombreUsuario,correoUsuario,tipoUsuario,nivelPermisos);
        SharedPreferences sharedPreferences = context.getSharedPreferences("AppPrefs", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("cedulaProfesionalEsp", cedulaProfesionalEsp);
        editor.putString("descripcionEsp", descripcionEsp);
        editor.putString("especialidadEsp",especialidadEsp);
        editor.apply();
    }

    public void guardarIdUsuario(long idUsuario){
        SharedPreferences sharedPreferences = context.getSharedPreferences("AppPrefs", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("idUsuario", idUsuario);
        editor.apply();
    }

    public void guardarIdEspecialista(long idEspecialista){
        SharedPreferences sharedPreferences = context.getSharedPreferences("AppPrefs", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("idEspecialista", idEspecialista);
        editor.apply();
    }

    public void terminarSesion(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("AppPrefs", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("nombreUsuario");
        editor.remove("correoUsuario");
        editor.remove("tipoUsuario");
        editor.remove("nivelPermisos");
        editor.remove("fotoUsuario");
        editor.putBoolean("sesionActiva",false);
        editor.remove("cedulaProfesionalEsp");
        editor.remove("descripcionEsp");
        editor.remove("especialidadEsp");
        editor.remove("idUsuario");
        editor.remove("idEspecialista");
        editor.apply();
    }

}
