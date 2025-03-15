package com.example.healthysmile.model.relationships;

public class RelEspUserCita {
    private Integer idRelEspUserCita;
    private Integer idEspecialista;
    private Integer idUsuario;
    private Integer idCita;

    public RelEspUserCita(Integer idCita, Integer idEspecialista, Integer idRelEspUserCita, Integer idUsuario) {
        this.idCita = idCita;
        this.idEspecialista = idEspecialista;
        this.idRelEspUserCita = idRelEspUserCita;
        this.idUsuario = idUsuario;
    }
}
