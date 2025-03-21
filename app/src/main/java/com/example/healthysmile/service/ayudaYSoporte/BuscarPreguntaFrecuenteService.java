package com.example.healthysmile.service.ayudaYSoporte;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.healthysmile.controller.ayudaYSoporte.BuscarPreguntaResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class BuscarPreguntaFrecuenteService {
    private static final String URL_BUSCAR_PREGUNTA_FRECUENTE = "http://10.0.2.2:3000/api/buscarPregunta";
    private final RequestQueue requestQueue;

    public BuscarPreguntaFrecuenteService(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    public void buscarPreguntaFrecuente(String pregunta, final BuscarPreguntaResponse listener) {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("pregunta", pregunta);
        } catch (JSONException e) {
            Log.e("Volley", "Error al crear JSON de solicitud", e);
            listener.onError("Error al crear solicitud de cita");
            return;
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                URL_BUSCAR_PREGUNTA_FRECUENTE,
                requestBody,
                response -> procesarRespuestaBuscarPregunta(response, listener),
                error -> {
                    Log.e("Volley", "Error al obtener cita", error);
                    listener.onError("Error al obtener cita");
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

    private void procesarRespuestaBuscarPregunta(JSONObject response, BuscarPreguntaResponse listener) {
        try {
            if (response.has("mensaje")) {
                String mensaje = response.getString("mensaje");
                listener.onPreguntaNoEncontrada(mensaje);
            } else {
                long idPreguntaFrecuente = response.getLong("idPreguntaFrecuente");
                String pregunta = response.getString("pregunta");
                String respuesta = response.getString("respuesta");
                String nombreUsuario = response.getString("nombreUsuario");
                String nombreEspecialista = response.getString("nombreEspecialista");

                listener.onResponse(idPreguntaFrecuente, pregunta, respuesta,nombreUsuario,nombreEspecialista);
            }
        } catch (JSONException e) {
            Log.e("Volley", "Error al procesar JSON", e);
            listener.onError("Error al procesar datos de la cita.");
        }
    }
}
