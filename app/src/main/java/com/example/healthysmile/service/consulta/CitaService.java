package com.example.healthysmile.service.consulta;

import android.content.Context;
import android.util.Log;

import com.example.healthysmile.controller.ApiNodeMySqlRespuesta;
import com.example.healthysmile.controller.consulta.ModifyCitaResponseListener;
import com.example.healthysmile.repository.NodeApiRetrofitClient;
import com.example.healthysmile.service.ApiNodeMySqlService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CitaService {

    private final ApiNodeMySqlService apiService;
    private final UpdateCitaService updateCitaService;
    private final DeleteCitaService deleteCitaService;

    public CitaService(Context context) {
        this.apiService = NodeApiRetrofitClient.getApiService();
        this.updateCitaService = new UpdateCitaService(context);
        this.deleteCitaService = new DeleteCitaService(context);
    }

    public void crearCita(String fecha, String hora, String motivo, long idUsuario, long idEspecialista) {
        Map<String, Object> citaDatos = new HashMap<>();
        citaDatos.put("fecha", fecha);
        citaDatos.put("hora", hora);
        citaDatos.put("motivo", motivo);
        citaDatos.put("idUsuario", idUsuario);
        citaDatos.put("idEspecialista", idEspecialista);

        apiService.crearCita(citaDatos).enqueue(new Callback<ApiNodeMySqlRespuesta>() {
            @Override
            public void onResponse(Call<ApiNodeMySqlRespuesta> call, Response<ApiNodeMySqlRespuesta> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("CitaCreada", response.body().getMessage());
                } else {
                    Log.d("CitaCreada", "Error al crear la cita: Respuesta no válida");
                }
            }

            @Override
            public void onFailure(Call<ApiNodeMySqlRespuesta> call, Throwable t) {
                Log.d("CitaCreada", "Error en la conexión: " + t.getMessage());
            }
        });
    }

    public void modificarCita(long idUsuario, String fecha, String hora, String nuevoMotivo, long nuevoEspecialista, ModifyCitaResponseListener listener) {
        // Crear el JSON con los datos de la cita modificada
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("idUsuario", idUsuario);
            requestBody.put("fecha", fecha);
            requestBody.put("hora", hora);
            requestBody.put("nuevaHora", hora);
            requestBody.put("nuevoMotivo", nuevoMotivo);
            requestBody.put("nuevoEspecialista", nuevoEspecialista);
        } catch (JSONException e) {
            Log.e("ModificarCita", "Error al crear el JSON", e);
            listener.onError("Error al crear los datos para la cita");
            return;
        }

        updateCitaService.modificarCita(requestBody, new ModifyCitaResponseListener() {
            @Override
            public void onResponse(String mensaje) {
                Log.d("ModificarCita", "Cita modificada con éxito: " + mensaje);
                listener.onResponse(mensaje);
            }

            @Override
            public void onError(String error) {
                Log.e("ModificarCita", "Error al modificar la cita: " + error);
                listener.onError(error);
            }

            @Override
            public void onCitaNoEncontrada(String mensaje) {
                Log.w("ModificarCita", "Cita no encontrada: " + mensaje);
                listener.onCitaNoEncontrada(mensaje);
            }
        });
    }

    public void eliminarCita(long idUsuario, String fecha, String hora, ModifyCitaResponseListener listener) {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("idUsuario", idUsuario);
            requestBody.put("fecha", fecha);
            requestBody.put("hora", hora);
        } catch (JSONException e) {
            Log.e("EliminarCita", "Error al crear el JSON", e);
            listener.onError("Error al crear los datos para eliminar la cita");
            return;
        }

        // Llamar al servicio de eliminación de citas
        deleteCitaService.eliminarCita(requestBody, new ModifyCitaResponseListener() {
            @Override
            public void onResponse(String mensaje) {
                Log.d("EliminarCita", "Cita eliminada con éxito: " + mensaje);
                listener.onResponse(mensaje);
            }

            @Override
            public void onError(String error) {
                Log.e("EliminarCita", "Error al eliminar la cita: " + error);
                listener.onError(error);
            }

            @Override
            public void onCitaNoEncontrada(String mensaje) {
                Log.w("EliminarCita", "Cita no encontrada: " + mensaje);
                listener.onCitaNoEncontrada(mensaje);
            }
        });
    }
}
