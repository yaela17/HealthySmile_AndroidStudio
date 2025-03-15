package com.example.healthysmile.model.relationships;

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
