package com.example.healthysmile.controller;

public interface SubirArchivoResponseListener {
    void onSubidaExitosa(String fotoUrl);
    void onError(String error);
}