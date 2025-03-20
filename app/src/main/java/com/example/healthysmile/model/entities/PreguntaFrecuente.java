package com.example.healthysmile.model.entities;

public class PreguntaFrecuente {
    private Integer idPreguntaFrecuente;
    private String pregunta;
    private String respuesta;
    private Integer busquedas;

    public Integer getBusquedas() {
        return busquedas;
    }

    public void setBusquedas(Integer busquedas) {
        this.busquedas = busquedas;
    }

    public Integer getIdPreguntaFrecuente() {
        return idPreguntaFrecuente;
    }

    public void setIdPreguntaFrecuente(Integer idPreguntaFrecuente) {
        this.idPreguntaFrecuente = idPreguntaFrecuente;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }
}
