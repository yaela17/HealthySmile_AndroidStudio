package com.example.healthysmile.controller.iniciarSesion;

import com.example.healthysmile.model.entities.Usuario;

public interface LogInResponseListener {
    void onSuccess(Usuario usuario);
    void onError(String error);
}