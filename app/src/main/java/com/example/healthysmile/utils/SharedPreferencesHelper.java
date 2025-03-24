package com.example.healthysmile.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.File;

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

    public void guardarfotoUsuario(String fotoUsuario){
        SharedPreferences sharedPreferences = context.getSharedPreferences("AppPrefs", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("fotoUsuario",fotoUsuario);
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

    public void guardarCodigoVerificacion(String codigoVerificacion){
        SharedPreferences sharedPreferences = context.getSharedPreferences("AppPrefs", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("codigoVerificacion", codigoVerificacion);
        editor.apply();
    }

    public void eliminarCodigoVerificacion(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("AppPrefs", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("codigoVerificacion");
    }

    public void imprimirDatosSharedPreferences() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);

        try {
            // Recuperar los valores guardados
            String nombreUsuario = sharedPreferences.getString("nombreUsuario", "No disponible");
            String correoUsuario = sharedPreferences.getString("correoUsuario", "No disponible");
            String tipoUsuario = sharedPreferences.getString("tipoUsuario", "No disponible");
            long nivelPermisos = sharedPreferences.getLong("nivelPermisos", -1L); // Usar getLong para valores long
            String fotoUsuario = sharedPreferences.getString("fotoUsuario", "No disponible");
            boolean sesionActiva = sharedPreferences.getBoolean("sesionActiva", false);
            String cedulaProfesionalEsp = sharedPreferences.getString("cedulaProfesionalEsp", "No disponible");
            String descripcionEsp = sharedPreferences.getString("descripcionEsp", "No disponible");
            String especialidadEsp = sharedPreferences.getString("especialidadEsp", "No disponible");
            long idUsuario = sharedPreferences.getLong("idUsuario", -1L); // Usar getLong para valores long
            long idEspecialista = sharedPreferences.getLong("idEspecialista", -1L); // Usar getLong para valores long

            // Imprimir los valores en el log
            Log.d("DatosSharedPreferences", "Nombre Usuario: " + nombreUsuario);
            Log.d("DatosSharedPreferences", "Correo Usuario: " + correoUsuario);
            Log.d("DatosSharedPreferences", "Tipo Usuario: " + tipoUsuario);
            Log.d("DatosSharedPreferences", "Nivel Permisos: " + nivelPermisos);
            Log.d("DatosSharedPreferences", "Foto Usuario: " + fotoUsuario);
            Log.d("DatosSharedPreferences", "Sesión Activa: " + sesionActiva);
            Log.d("DatosSharedPreferences", "Cédula Profesional Especialista: " + cedulaProfesionalEsp);
            Log.d("DatosSharedPreferences", "Descripción Especialista: " + descripcionEsp);
            Log.d("DatosSharedPreferences", "Especialidad Especialista: " + especialidadEsp);
            Log.d("DatosSharedPreferences", "ID Usuario: " + idUsuario);
            Log.d("DatosSharedPreferences", "ID Especialista: " + idEspecialista);
        } catch (Exception e) {
            Log.e("SharedPreferencesError", "Error al imprimir datos de SharedPreferences", e);
        }
    }
}
