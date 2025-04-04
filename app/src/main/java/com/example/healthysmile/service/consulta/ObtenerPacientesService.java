package com.example.healthysmile.service.consulta;


import static com.example.healthysmile.service.URLSApisNode.URL_OBTENER_PACIENTES;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.healthysmile.controller.consulta.ObtenerPacientesResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ObtenerPacientesService {

    private RequestQueue requestQueue;

    public ObtenerPacientesService(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    public void obtenerPacientesChat(final ObtenerPacientesResponseListener listener, List<Long> idsPacientes) {
        String idsString = android.text.TextUtils.join(",", idsPacientes);

        String URL_PETICION_OBTENER_PACIENTES = URL_OBTENER_PACIENTES + "?idsUsuarios=" + idsString;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL_PETICION_OBTENER_PACIENTES,
                null,
                response -> procesarPacientes(response, listener),
                error -> {
                    Log.e("Volley", "Error al obtener pacientes", error);
                    listener.onError("Error al cargar pacientes");
                }
        );
        requestQueue.add(jsonArrayRequest);
    }

    private void procesarPacientes(JSONArray response, ObtenerPacientesResponseListener listener) {
        List<String> nombres = new ArrayList<>();
        List<String> correos = new ArrayList<>();
        List<Long> idsPacientes = new ArrayList<>();
        List<String> fotosPerfil = new ArrayList<>();

        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject paciente = response.getJSONObject(i);
                long id = paciente.getLong("idUsuario");
                String nombre = paciente.getString("nombre");
                String correo = paciente.getString("correo");
                String fotoPerfil = paciente.optString("fotoPerfil","No disponible");

                if(fotoPerfil.equals("null")){
                    fotoPerfil = "No disponible";
                }

                nombres.add(nombre);
                correos.add(correo);
                idsPacientes.add(id);
                fotosPerfil.add(fotoPerfil);
            }
            listener.onResponse(nombres, correos, idsPacientes,fotosPerfil);
        } catch (JSONException e) {
            Log.e("Volley", "Error al procesar JSON", e);
            listener.onError("Error al procesar datos de pacientes.");
        }
    }
}
