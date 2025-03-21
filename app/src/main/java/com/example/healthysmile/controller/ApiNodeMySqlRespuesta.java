package com.example.healthysmile.controller;

public class ApiNodeMySqlRespuesta {
    private String message;
    private String mensaje;
    private long idUsuario;
    private long idEspecialista;
    private long idCita;

    public String getMessage() {
        return message;
    }
    public String getMensaje(){return mensaje;}
    public long getIdUsuario() {
        return idUsuario;
    }
    public long getIdEspecialista(){
        return idEspecialista;
    }
    public long getIdCita(){return idCita;}
}
