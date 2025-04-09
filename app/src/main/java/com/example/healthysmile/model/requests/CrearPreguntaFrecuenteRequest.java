package com.example.healthysmile.model.requests;

public class CrearPreguntaFrecuenteRequest {
    private String pregunta;
    private Integer idUsuario;

    public CrearPreguntaFrecuenteRequest(Integer idUsuario, String pregunta) {
        this.idUsuario = idUsuario;
        this.pregunta = pregunta;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }
}
