package com.example.healthysmile.controller;

public interface ArchivoExisteListener {
    void onResultado(boolean existe);
    void onError(String mensaje);
}
