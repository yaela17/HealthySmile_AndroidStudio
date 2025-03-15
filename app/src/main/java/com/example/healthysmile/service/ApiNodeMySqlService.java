package com.example.healthysmile.service;

import com.example.healthysmile.controller.ApiNodeMySqlRespuesta;
import com.example.healthysmile.model.entities.Especialista;
import com.example.healthysmile.model.entities.Usuario;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiNodeMySqlService {
    @POST("api/crearPaciente")
    Call<ApiNodeMySqlRespuesta> crearPaciente(@Body Usuario paciente);

    @POST("api/crearEspecialista")
    Call<ApiNodeMySqlRespuesta> crearEspecialista(@Body Especialista especialista);

    @POST("api/LogInUsuario")
    Call<ApiNodeMySqlRespuesta> LoginInUsuario(@Body Usuario usuario);
}
