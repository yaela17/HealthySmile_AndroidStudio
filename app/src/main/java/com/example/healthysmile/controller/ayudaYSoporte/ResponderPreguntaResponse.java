package com.example.healthysmile.controller.ayudaYSoporte;

public interface ResponderPreguntaResponse {
    void onResponse(String mensaje);
    void onError(String error);
    void onPreguntaNoEncontrada(String mensaje);
}
