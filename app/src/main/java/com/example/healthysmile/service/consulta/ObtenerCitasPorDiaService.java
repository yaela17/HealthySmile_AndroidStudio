package com.example.healthysmile.service.consulta;

import static com.example.healthysmile.service.URLSApisNode.URL_OBTENER_CITAS_DIA;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.healthysmile.controller.consulta.ObtenerCitasPorDiaResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ObtenerCitasPorDiaService {

    private RequestQueue requestQueue;

    public ObtenerCitasPorDiaService(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    public void obtenerCitasPorDia(final String fecha, final long idEspecialista, final ObtenerCitasPorDiaResponseListener listener) {
        String URL_PETICION_CITAS_POR_DIA = URL_OBTENER_CITAS_DIA + "?fecha=" + fecha + "&idEspecialista=" + idEspecialista;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL_PETICION_CITAS_POR_DIA,
                null,
                response -> procesarCitas(response, listener),
                error -> {
                    Log.e("Volley", "Error al obtener citas", error);
                    listener.onError("Error al cargar las citas");
                }
        );
        requestQueue.add(jsonArrayRequest);
    }

    private void procesarCitas(JSONArray response, ObtenerCitasPorDiaResponseListener listener) {
        Log.d("Respuesta del servidor", response.toString());
        List<Long> idsCita = new ArrayList<>();
        List<String> motivosCita = new ArrayList<>();
        List<Long> idsEspecialista = new ArrayList<>();
        List<String> fechasCita = new ArrayList<>();

        try {
            // Verificar si la respuesta contiene datos o un mensaje de error
            if (response.length() == 0) {
                listener.onError("No se recibieron datos de citas.");
                return;
            }

            JSONObject primerObjeto = response.getJSONObject(0);
            if (primerObjeto.has("error")) {
                String mensaje = primerObjeto.getString("error");
                if (mensaje.equals("No existen citas para la fecha y especialista proporcionados")) {
                    listener.onError(mensaje);
                    return;
                }
            }

            // Procesar las citas si hay datos
            for (int i = 0; i < response.length(); i++) {
                JSONObject cita = response.getJSONObject(i);

                long idCita = cita.optLong("idCita", -1);
                String motivoCita = cita.optString("motivoCita", "No hay");
                long idEspecialistaCita = cita.optLong("idEspecialista", -1);
                String fechaCita = cita.optString("fechaCita", "");

                idsCita.add(idCita);
                motivosCita.add(motivoCita);
                idsEspecialista.add(idEspecialistaCita);
                fechasCita.add(fechaCita);
            }

            // Pasar la respuesta al listener
            listener.onResponse(idsCita, motivosCita, idsEspecialista, fechasCita);

        } catch (JSONException e) {
            Log.e("Volley", "Error al procesar JSON", e);
            listener.onError("Error al procesar datos de citas.");
        }
    }
}
