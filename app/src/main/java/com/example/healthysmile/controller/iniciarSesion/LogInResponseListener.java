package com.example.healthysmile.controller.iniciarSesion;

import com.example.healthysmile.model.entities.Especialista;
import com.example.healthysmile.model.entities.Usuario;

public interface LogInResponseListener {
    void logInUsuario(Usuario usuario);
    void logInEspecialista(Especialista especialista);
    void onError(String error);
}