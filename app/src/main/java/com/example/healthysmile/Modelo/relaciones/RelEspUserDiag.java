package com.example.healthysmile.Modelo.relaciones;

public class RelEspUserDiag {
    private Integer idRelEspDiag;
    private Integer idUsuario;
    private Integer idDiagnostico;
    private String nomEsp;

    public RelEspUserDiag(Integer idDiagnostico, Integer idRelEspDiag, Integer idUsuario, String nomEsp) {
        this.idDiagnostico = idDiagnostico;
        this.idRelEspDiag = idRelEspDiag;
        this.idUsuario = idUsuario;
        this.nomEsp = nomEsp;
    }
}
