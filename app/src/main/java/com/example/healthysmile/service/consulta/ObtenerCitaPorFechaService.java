package com.example.healthysmile.service.consulta;

import static com.example.healthysmile.service.URLSApisNode.URL_OBTENER_CITA_POR_FECHA;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.healthysmile.controller.consulta.CitaObtenerPorFechaResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

public class ObtenerCitaPorFechaService {


    private RequestQueue requestQueue;

    public ObtenerCitaPorFechaService(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    public void obtenerCitaPorFecha(long idUsuario, String fecha, String hora, final CitaObtenerPorFechaResponseListener listener) {
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
                response -> procesarCitaPorFecha(response, listener),
                error -> {
                    Log.e("Volley", "Error al obtener cita", error);
                    listener.onError("Error al obtener cita");
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

    private void procesarCitaPorFecha(JSONObject response, CitaObtenerPorFechaResponseListener listener) {

        Log.d("Respuesta del servidor fecha", response.toString());
        try {
            if (response.has("mensaje")) {
                String mensaje = response.getString("mensaje");
                listener.onCitaNoEncontrada(mensaje);
            } else {
                long idCita = response.optLong("idCita",-1);
                String motivoCita = response.optString("motivoCita","No hay motivo");
                long idEspecialista = response.optLong("idEspecialista",-1);

                listener.onResponse(idCita, motivoCita, idEspecialista);
            }
        } catch (JSONException e) {
            Log.e("Volley", "Error al procesar JSON", e);
            listener.onError("Error al procesar datos de la cita.");
        }
    }
}
