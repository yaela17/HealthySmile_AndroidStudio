package com.example.healthysmile.controller.consulta;

public interface ModifyCitaResponseListener {
    void onResponse(String mensaje);
    void onError(String error);
    void onCitaNoEncontrada(String mensaje);
}
