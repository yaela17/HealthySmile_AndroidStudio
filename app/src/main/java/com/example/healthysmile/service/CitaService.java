package com.example.healthysmile.service;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class CitaService {

    private static final String URL_OBTENER_CITA_POR_FECHA = "http://10.0.2.2:3000/api/obtenerCitaPorFecha";
    private RequestQueue requestQueue;

    public CitaService(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    public void obtenerCitaPorFecha(long idUsuario, String fecha, String hora, final CitaResponseListener listener) {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("idUsuario", idUsuario);
            requestBody.put("fecha", fecha);
            requestBody.put("hora", hora);
        } catch (JSONException e) {
            Log.e("Volley", "Error al crear JSON de solicitud", e);
            listener.onError("Error al crear solicitud de cita");
            return;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                URL_OBTENER_CITA_POR_FECHA,
                requestBody,
                response -> procesarCita(response, listener),
                error -> {
                    Log.e("Volley", "Error al obtener cita", error);
                    listener.onError("Error al obtener cita");
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

    private void procesarCita(JSONObject response, CitaResponseListener listener) {
        try {
            if (response.has("mensaje")) {
                // Si la API devuelve un mensaje, significa que no hay cita
                String mensaje = response.getString("mensaje");
                listener.onCitaNoEncontrada(mensaje);
            } else {
                // Si la API devuelve los datos de la cita
                long idCita = response.getLong("idCita");
                String motivoCita = response.getString("motivoCita");
                long idEspecialista = response.getLong("idEspecialista");

                listener.onResponse(idCita, motivoCita, idEspecialista);
            }
        } catch (JSONException e) {
            Log.e("Volley", "Error al procesar JSON", e);
            listener.onError("Error al procesar datos de la cita.");
        }
    }
}
