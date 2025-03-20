package com.example.healthysmile.service;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.healthysmile.controller.consulta.EspecialistaResponseListenerChat;
import com.example.healthysmile.controller.extraAndroid.EspecialistaResponseListenerInicio;
import com.example.healthysmile.controller.consulta.EspecialistaResponseListenerSpinnerCitas;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EspecialistaService {

    private static final String URL_OBTENER_ESPECIALISTAS = "http://10.0.2.2:3000/api/obtenerEspecialistasChatAndroid";
    private RequestQueue requestQueue;

    public EspecialistaService(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    public void obtenerEspecialistasChat(final EspecialistaResponseListenerChat listener) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL_OBTENER_ESPECIALISTAS,
                null,
                response -> procesarEspecialistasChat(response, listener),
                error -> {
                    Log.e("Volley", "Error al obtener especialistas", error);
                    listener.onError("Error al cargar especialistas");
                }
        );
        requestQueue.add(jsonArrayRequest);
    }

    private void procesarEspecialistasChat(JSONArray response, EspecialistaResponseListenerChat listener) {
        List<String> nombres = new ArrayList<>();
        List<String> especialidades = new ArrayList<>();
        List<String> descripciones = new ArrayList<>();
        List<Long> idsEspecialista = new ArrayList<>();

        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject especialista = response.getJSONObject(i);
                long idEspecialista = especialista.getLong("idEspecialista");
                String nombre = especialista.getString("nombre");
                String especialidad = especialista.optString("especialidad", "No especificado");
                String descripcion = especialista.optString("descripcion", "No disponible");

                nombres.add(nombre);
                idsEspecialista.add(idEspecialista);
                especialidades.add(especialidad);
                descripciones.add(descripcion);
            }
            listener.onResponse(nombres, especialidades, descripciones, idsEspecialista);
        } catch (JSONException e) {
            Log.e("Volley", "Error al procesar JSON", e);
            listener.onError("Error al procesar datos de especialistas.");
        }
    }

    public void obtenerEspecialistasInicio(final EspecialistaResponseListenerInicio listener) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL_OBTENER_ESPECIALISTAS,
                null,
                response -> procesarEspecialistasInicio(response, listener),
                error -> {
                    Log.e("Volley", "Error al obtener especialistas", error);
                    listener.onErrorInicio("Error al cargar especialistas");
                }
        );
        requestQueue.add(jsonArrayRequest);
    }

    private void procesarEspecialistasInicio(JSONArray response, EspecialistaResponseListenerInicio listener) {
        List<String> nombres = new ArrayList<>();
        List<String> especialidades = new ArrayList<>();
        List<String> descripciones = new ArrayList<>();
        List<Long> idsEspecialista = new ArrayList<>();
        List<String> cedulasProfesional = new ArrayList<>();
        List<String> fotos = new ArrayList<>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject especialista = response.getJSONObject(i);
                long idEspecialista = especialista.getLong("idEspecialista");
                String nombre = especialista.getString("nombre");
                String especialidad = especialista.optString("especialidad", "No especificado");
                String descripcion = especialista.optString("descripcion", "No disponible");
                String foto = especialista.optString("fotos","default");

                String cedulaProfesional = especialista.optString("cedulaProfesional", "No disponible");


                nombres.add(nombre);
                idsEspecialista.add(idEspecialista);
                especialidades.add(especialidad);
                descripciones.add(descripcion);
                cedulasProfesional.add(cedulaProfesional);
                fotos.add(foto);
            }
            if (nombres.isEmpty()) {
                listener.onErrorInicio("No se encontraron especialistas.");
            } else {
                listener.onResponseInicio(nombres, especialidades, descripciones, idsEspecialista, cedulasProfesional,fotos);
            }
        } catch (JSONException e) {
            Log.e("Volley", "Error al procesar JSON", e);
            listener.onErrorInicio("Error al procesar datos de especialistas.");
        }
    }

    public void obtenerEspecialistasSpinnerCitas(final EspecialistaResponseListenerSpinnerCitas listener) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL_OBTENER_ESPECIALISTAS,
                null,
                response -> procesarEspecialistasSpinnerCitas(response, listener),
                error -> {
                    Log.e("Volley", "Error al obtener especialistas", error);
                    listener.onErrorSpinnerCitas("Error al cargar especialistas");
                }
        );
        requestQueue.add(jsonArrayRequest);
    }

    private void procesarEspecialistasSpinnerCitas(JSONArray response, EspecialistaResponseListenerSpinnerCitas listener) {
        List<String> nombres = new ArrayList<>();
        List<String> especialidades = new ArrayList<>();
        List<Long> idsEspecialista = new ArrayList<>();

        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject especialista = response.getJSONObject(i);

                String nombre = especialista.getString("nombre");
                String especialidad = especialista.optString("especialidad", "No especificado");
                long idEspecialista = especialista.getLong("idEspecialista");

                nombres.add(nombre);
                especialidades.add(especialidad);
                idsEspecialista.add(idEspecialista);
            }

            if (nombres.isEmpty()) {
                listener.onErrorSpinnerCitas("No se encontraron especialistas.");
            } else {
                listener.onResponseSpinnerCitas(nombres, especialidades, idsEspecialista);
            }

        } catch (JSONException e) {
            Log.e("Volley", "Error al procesar JSON", e);
            listener.onErrorSpinnerCitas("Error al procesar datos de especialistas.");
        }
    }

}
