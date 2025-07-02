package com.example.healthysmile.model.requests;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class ActualizarDescripcionRequest {
    private String nuevaDescripcion;
    private String correo;

    public ActualizarDescripcionRequest(String nuevaDescripcion, String correo) {
        this.nuevaDescripcion = nuevaDescripcion;
        this.correo = correo;
    }
}
