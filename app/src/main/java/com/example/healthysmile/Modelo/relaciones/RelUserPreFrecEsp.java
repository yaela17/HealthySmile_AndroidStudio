package com.example.healthysmile.Modelo.relaciones;

public class RelUserPreFrecEsp {
    private Integer idRelUserPreFrecEsp;
    private Integer idPreguntaFrecuente;
    private Integer idUsuario;
    private String nomEsp;

    public RelUserPreFrecEsp(Integer idPreguntaFrecuente, Integer idRelUserPreFrecEsp, Integer idUsuario, String nomEsp) {
        this.idPreguntaFrecuente = idPreguntaFrecuente;
        this.idRelUserPreFrecEsp = idRelUserPreFrecEsp;
        this.idUsuario = idUsuario;
        this.nomEsp = nomEsp;
    }
}
