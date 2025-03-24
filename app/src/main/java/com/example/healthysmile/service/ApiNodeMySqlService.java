package com.example.healthysmile.service;

import com.example.healthysmile.controller.ApiNodeMySqlRespuesta;
import com.example.healthysmile.model.TemplateParams;
import com.example.healthysmile.model.entities.Especialista;
import com.example.healthysmile.model.entities.Usuario;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiNodeMySqlService {
    @POST("crearPaciente")
    Call<ApiNodeMySqlRespuesta> crearPaciente(@Body Usuario paciente);

    @POST("crearEspecialista")
    Call<ApiNodeMySqlRespuesta> crearEspecialista(@Body Especialista especialista);

    @POST("crearPregunta")
    Call<ApiNodeMySqlRespuesta> crearPreguntaFrecuente(@Body Map<String, Object> preguntaFrecuente);

    @POST("crearCita")
    Call<ApiNodeMySqlRespuesta> crearCita(@Body Map<String, Object> citaDatos);

    @POST("verificarCorreo")
    Call<ApiNodeMySqlRespuesta> verificarCorreo(@Query("correoUser") String correo);

    @POST("enviarCorreoVerificacion")
    Call<ApiNodeMySqlRespuesta> enviarCorreoVerificacion(@Body TemplateParams templateParams);

    @POST("actualizarFotoPerfil")
    Call<ApiNodeMySqlRespuesta> actualizarFotoPerfil(@Body Map<String, Object> params);
}
