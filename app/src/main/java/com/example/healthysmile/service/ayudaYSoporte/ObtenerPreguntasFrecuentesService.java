package com.example.healthysmile.service.ayudaYSoporte;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.healthysmile.controller.ayudaYSoporte.ObtenerPreguntasFrecuentesResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ObtenerPreguntasFrecuentesService {
    private static final String URL_OBTENER_PREGUNTAS_FRECUENTES = "http://10.0.2.2:3000/api/obtenerPreguntas";

    private RequestQueue requestQueue;

    public ObtenerPreguntasFrecuentesService(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    public void obtenerPreguntasFrecuentes(final ObtenerPreguntasFrecuentesResponse listener) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL_OBTENER_PREGUNTAS_FRECUENTES,
                null,
                response -> procesarObtenerPreguntasFrecuentes(response, listener),
                error -> {
                    Log.e("Volley", "Error al obtener especialistas", error);
                    listener.onError("Error al cargar especialistas");
                }
        );
        requestQueue.add(jsonArrayRequest);
    }

    private void procesarObtenerPreguntasFrecuentes(JSONArray response, ObtenerPreguntasFrecuentesResponse listener) {
        List<Long> idsPreguntasFrecuentes = new ArrayList<>();
        List<String> preguntas = new ArrayList<>();
        List<String> respuestas = new ArrayList<>();
        List<Long> idsUsuarios = new ArrayList<>();
        List<Long> idsEspecialistas = new ArrayList<>();

        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject especialista = response.getJSONObject(i);
                long idPreguntaFrecuente = especialista.optLong("idPreguntaFrecuente",-1);
                String pregunta = especialista.optString("pregunta","Ese bulo");
                String respuesta = especialista.optString("respuesta","Aun no es respondido");
                long idUsuario = especialista.optLong("idUsuario", -1);
                long idEspecialista = especialista.optLong("idEspecialista", -1);

                idsPreguntasFrecuentes.add(idPreguntaFrecuente);
                preguntas.add(pregunta);
                respuestas.add(respuesta);
                idsUsuarios.add(idUsuario);
                idsEspecialistas.add(idEspecialista);
            }
            listener.onResponse(idsPreguntasFrecuentes,preguntas, respuestas, idsUsuarios, idsEspecialistas);
        } catch (JSONException e) {
            Log.e("Volley", "Error al procesar JSON", e);
            listener.onError("Error al procesar datos de especialistas.");
        }
    }
}
