package com.example.healthysmile.service;

public interface CitaResponseListener {
    void onResponse(long idCita, String motivoCita, long idEspecialista);
    void onCitaNoEncontrada(String mensaje);
    void onError(String error);
}
