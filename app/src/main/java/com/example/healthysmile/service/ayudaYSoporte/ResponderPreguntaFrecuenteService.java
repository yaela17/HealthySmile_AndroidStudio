package com.example.healthysmile.service.ayudaYSoporte;

import static com.example.healthysmile.service.URLSApisNode.URL_RESPONDER_PREGUNTA_FRECUENTE;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.healthysmile.controller.ayudaYSoporte.ResponderPreguntaResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class ResponderPreguntaFrecuenteService {


    private RequestQueue requestQueue;

    public ResponderPreguntaFrecuenteService(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    public void responderPreguntaFrecuente(long idPreguntaFrecuente, long idEspecialista, String respuesta, final ResponderPreguntaResponse listener) {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("idPreguntaFrecuente", idPreguntaFrecuente);
            requestBody.put("idEspecialista", idEspecialista);
            requestBody.put("respuesta", respuesta);
        } catch (JSONException e) {
            Log.e("Volley", "Error al crear JSON de solicitud", e);
            listener.onError("Error al crear solicitud de cita");
            return;
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                URL_RESPONDER_PREGUNTA_FRECUENTE,
                requestBody,
                response -> procesarRespuestaResponderPregunta(response, listener),
                error -> {
                    Log.e("Volley", "Error al obtener cita", error);
                    listener.onError("Error al obtener cita");
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

    private void procesarRespuestaResponderPregunta(JSONObject response, ResponderPreguntaResponse listener) {
        try {
            if (response.has("mensaje")) {
                String mensaje = response.getString("mensaje");
                listener.onResponse(mensaje);
            } else {
                listener.onError("Error inesperado");
            }
        } catch (JSONException e) {
            Log.e("Volley", "Error al procesar respuesta", e);
            listener.onError("Error al procesar la respuesta del servidor.");
        }
    }
}
