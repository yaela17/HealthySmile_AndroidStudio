package com.example.healthysmile.service.consulta;

import static com.example.healthysmile.service.URLSApisNode.URL_ELIMINAR_CITA;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.healthysmile.controller.consulta.ModifyCitaResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

public class DeleteCitaService {

    private RequestQueue requestQueue;

    public DeleteCitaService(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    public void eliminarCita(JSONObject requestBody, final ModifyCitaResponseListener listener) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                URL_ELIMINAR_CITA,
                requestBody,
                response -> {
                    procesarRespuesta(response, listener);
                },
                error -> {
                    Log.e("Volley", "Error al eliminar cita", error);
                    listener.onError("Error al eliminar cita");
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    private void procesarRespuesta(JSONObject response, ModifyCitaResponseListener listener) {
        try {
            if (response.has("message")) {
                String mensaje = response.getString("message");
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
