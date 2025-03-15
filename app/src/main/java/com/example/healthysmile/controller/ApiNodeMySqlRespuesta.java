package com.example.healthysmile.controller;

import com.example.healthysmile.model.entities.Especialista;
import com.example.healthysmile.model.entities.Usuario;

public class ApiNodeMySqlRespuesta {
    private String message;
    private Usuario usuario;  // Campo para el Usuario
    private Especialista especialista;  // Campo para el Especialista

    // Getter para el mensaje
    public String getMessage() {
        return message;
    }

    // Getter para el usuario
    public Usuario getUsuario() {
        return usuario;
    }

    // Setter para el usuario
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    // Getter para el especialista
    public Especialista getEspecialista() {
        return especialista;
    }

    // Setter para el especialista
    public void setEspecialista(Especialista especialista) {
        this.especialista = especialista;
    }

    @Override
    public String toString() {
        return "ApiMySqlRespuesta{" +
                "usuario=" + (usuario != null ? usuario.toString() : "null") +
                ", especialista=" + (especialista != null ? especialista.toString() : "null") +
                '}';
    }
}
