package com.example.healthysmile.model.requests;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ActualizarNombreRequest {
    private String nuevoNombre;
    private String correo;

    public ActualizarNombreRequest(String nuevoNombre, String correo) {
        this.nuevoNombre = nuevoNombre;
        this.correo = correo;
    }
}
