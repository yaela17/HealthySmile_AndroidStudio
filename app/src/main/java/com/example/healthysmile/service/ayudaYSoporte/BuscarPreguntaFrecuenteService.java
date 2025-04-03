package com.example.healthysmile.service.ayudaYSoporte;

import static com.example.healthysmile.service.URLSApisNode.URL_BUSCAR_PREGUNTA_FRECUENTE;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.healthysmile.controller.ayudaYSoporte.BuscarPreguntaResponse;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class BuscarPreguntaFrecuenteService {
    private final RequestQueue requestQueue;

    public BuscarPreguntaFrecuenteService(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    public void buscarPreguntaFrecuente(String pregunta, final BuscarPreguntaResponse listener) {
        String url = URL_BUSCAR_PREGUNTA_FRECUENTE + "?pregunta=" + pregunta;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> procesarRespuestaBuscarPregunta(response, listener),
                error -> {
                    Log.e("Volley", "Error al obtener preguntas frecuentes", error);
                    listener.onError("Error al obtener preguntas frecuentes");
                }
        );

        requestQueue.add(jsonArrayRequest);
    }

    private void procesarRespuestaBuscarPregunta(JSONArray response, BuscarPreguntaResponse listener) {
        try {
            if (response.length() == 0) {
                listener.onPreguntasNoEncontradas("No se encontraron preguntas frecuentes");
            } else {
                List<Long> idPreguntasFrecuentes = new ArrayList<>();
                List<String> preguntas = new ArrayList<>();
                List<String> respuestas = new ArrayList<>();
                List<Long> idsUsuarios = new ArrayList<>();
                List<Long> idsEspecialistas = new ArrayList<>();

                for (int i = 0; i < response.length(); i++) {
                    idPreguntasFrecuentes.add(response.getJSONObject(i).getLong("idPreguntaFrecuente"));
                    preguntas.add(response.getJSONObject(i).getString("pregunta"));
                    respuestas.add(response.getJSONObject(i).getString("respuesta"));
                    idsUsuarios.add(response.getJSONObject(i).getLong("idUsuario"));
                    idsEspecialistas.add(response.getJSONObject(i).optLong("idEspecialista",-1));
                }

                listener.onResponse(idPreguntasFrecuentes, preguntas, respuestas, idsUsuarios, idsEspecialistas);
            }
        } catch (JSONException e) {
            Log.e("Volley", "Error al procesar JSON", e);
            listener.onError("Error al procesar datos de las preguntas frecuentes.");
        }
    }
}
