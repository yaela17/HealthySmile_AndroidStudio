package com.example.healthysmile.service.gestion;

import static com.example.healthysmile.service.URLSApisNode.URL_OBTENER_CITAS_PACIENTE;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.healthysmile.controller.gestion.ObtenerCitasPacienteResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ObtenerCitasPacienteService {

    private RequestQueue requestQueue;

    public ObtenerCitasPacienteService(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    public void obtenerCitasPaciente(final ObtenerCitasPacienteResponseListener listener, long idUsuario) {
        String URL_PETICION_OBTENER_CITAS = URL_OBTENER_CITAS_PACIENTE + "?idUsuario=" + idUsuario;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL_PETICION_OBTENER_CITAS,
                null,
                response -> procesarCitasPaciente(response, listener),
                error -> {
                    Log.e("Volley", "Error al obtener citas", error);
                    listener.onError("Error al cargar citas.");
                }
        );
        requestQueue.add(jsonArrayRequest);
    }

    private void procesarCitasPaciente(JSONArray response, ObtenerCitasPacienteResponseListener listener) {
        List<Long> idsCitas = new ArrayList<>();
        List<String> motivosCita = new ArrayList<>();
        List<String> fechasCita = new ArrayList<>();
        List<Long> idsEspecialistas = new ArrayList<>();
        List<String> nombresEspecialistas = new ArrayList<>();

        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject cita = response.getJSONObject(i);
                long idCita = cita.getLong("idCita");
                String motivoCita = cita.getString("motivoCita");
                String fechaCita = cita.getString("fechaCita");
                long idEspecialista = cita.getLong("idEspecialista");
                String nombreEspecialista = cita.getString("nombreEspecialista");

                idsCitas.add(idCita);
                motivosCita.add(motivoCita);
                fechasCita.add(fechaCita);
                idsEspecialistas.add(idEspecialista);
                nombresEspecialistas.add(nombreEspecialista);
            }
            listener.onResponse(idsCitas, motivosCita, fechasCita,idsEspecialistas, nombresEspecialistas);
        } catch (JSONException e) {
            Log.e("Volley", "Error al procesar JSON", e);
            listener.onError("Error al procesar datos de citas.");
        }
    }
}
