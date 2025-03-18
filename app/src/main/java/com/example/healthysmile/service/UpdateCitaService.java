package com.example.healthysmile.service;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.healthysmile.controller.ModifyCitaResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateCitaService {
    private static final String URL_ACTUALIZAR_CITA = "http://10.0.2.2:3000/api/modificarCita";
    private RequestQueue requestQueue;

    public UpdateCitaService(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    public void modificarCita(JSONObject requestBody, final ModifyCitaResponseListener listener) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                URL_ACTUALIZAR_CITA,
                requestBody,
                response -> {
                    procesarRespuesta(response, listener);
                },
                error -> {
                    Log.e("Volley", "Error al modificar cita", error);
                    listener.onError("Error al modificar cita");
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    private void procesarRespuesta(JSONObject response, ModifyCitaResponseListener listener) {
        try {
            if (response.has("mensaje")) {
                String mensaje = response.getString("mensaje");
                listener.onResponse(mensaje);
            } else {
                // Aquí podrías manejar otro tipo de error o respuesta
                listener.onError("Error inesperado");
            }
        } catch (JSONException e) {
            Log.e("Volley", "Error al procesar respuesta", e);
            listener.onError("Error al procesar la respuesta del servidor.");
        }
    }
}
