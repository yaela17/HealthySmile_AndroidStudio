package com.example.healthysmile.controller.ayudaYSoporte;

public interface BuscarPreguntaResponse {
    void onResponse(long idPreguntaFrecuente, String pregunta, String respuesta, String nombreUsuario, String nombreEspecialista);
    void onPreguntaNoEncontrada(String mensaje);
    void onError(String error);
}
