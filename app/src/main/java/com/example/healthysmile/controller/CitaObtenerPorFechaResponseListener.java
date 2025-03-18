package com.example.healthysmile.controller;

public interface CitaObtenerPorFechaResponseListener {
    void onResponse(long idCita, String motivoCita, long idEspecialista);
    void onCitaNoEncontrada(String mensaje);
    void onError(String error);
}
