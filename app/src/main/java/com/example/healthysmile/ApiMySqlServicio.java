package com.example.healthysmile;

import com.example.healthysmile.Modelo.entidades.Usuario;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiMySqlServicio {
    @POST("api/crearPaciente")
    Call<ApiMySqlRespuesta> crearPaciente(@Body Usuario paciente);
}
