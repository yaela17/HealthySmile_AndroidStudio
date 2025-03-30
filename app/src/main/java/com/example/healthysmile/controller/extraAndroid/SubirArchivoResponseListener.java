package com.example.healthysmile.controller.extraAndroid;

public interface SubirArchivoResponseListener {
    void onSubidaExitosa(String fotoUrl);
    void onError(String error);
}