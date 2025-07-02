package com.example.healthysmile.service.iniciarSesion;

import static com.example.healthysmile.service.URLSApisNode.URL_LOG_IN;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.healthysmile.controller.iniciarSesion.LogInResponseListener;
import com.example.healthysmile.model.entities.Especialista;
import com.example.healthysmile.model.entities.Usuario;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class LogInService {

    private RequestQueue requestQueue;

    public LogInService(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    public void logIn(String correoUser, String contrasenaUser, final LogInResponseListener listener) {
        JSONObject loginData = new JSONObject();
        try {
            loginData.put("correoUser", correoUser);
            loginData.put("contrasenaUser", contrasenaUser);
        } catch (JSONException e) {
            e.printStackTrace();
            listener.onError("Error al crear los datos de login.");
            return;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                URL_LOG_IN,
                loginData,
                response -> procesarRespuestaLogin(response, listener),
                error -> {
                    Log.e("LogInService", "Error al hacer login", error);
                    listener.onError("Error al iniciar sesión.");
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    private void procesarRespuestaLogin(JSONObject response, LogInResponseListener listener) {
        try {
            Gson gson = new Gson();
            Usuario usuario = gson.fromJson(response.toString(), Usuario.class);

            if (usuario != null && usuario.getNivelPermisos() > 1) {
                Especialista especialista = gson.fromJson(response.toString(), Especialista.class);
                listener.logInEspecialista(especialista);
            } else {
                listener.logInUsuario(usuario);
            }
        }catch (Exception e) {
            Log.e("LogInService", "Error general en login", e);
            listener.onError("Error desconocido.");
        }
    }
}
