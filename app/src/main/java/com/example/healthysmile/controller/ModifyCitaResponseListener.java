package com.example.healthysmile.controller;

public interface ModifyCitaResponseListener {
    void onResponse(String mensaje);
    void onError(String error);
    void onCitaNoEncontrada(String mensaje);
}
