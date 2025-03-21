package com.example.healthysmile.service;

import android.util.Log;

import com.example.healthysmile.controller.ApiNodeMySqlRespuesta;
import com.example.healthysmile.model.TemplateParams;
import com.example.healthysmile.repository.NodeApiRetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmailServiceJS {

    public void enviarCorreo(TemplateParams templateParams){
        ApiNodeMySqlService apiService = NodeApiRetrofitClient.getApiService();
        apiService.enviarCorreoVerificacion(templateParams).enqueue(new Callback<ApiNodeMySqlRespuesta>() {
            @Override
            public void onResponse(Call<ApiNodeMySqlRespuesta> call, Response<ApiNodeMySqlRespuesta> response) {
                if (response.isSuccessful()) {
                    Log.d("EmailJS", "Correo enviado exitosamente.");
                } else {
                    Log.e("EmailJS", "Error al enviar el correo.");
                }
            }

            @Override
            public void onFailure(Call<ApiNodeMySqlRespuesta> call, Throwable t) {
                Log.e("EmailJS", "Fallo al enviar el correo: " + t.getMessage());
            }
        });
    }
}
