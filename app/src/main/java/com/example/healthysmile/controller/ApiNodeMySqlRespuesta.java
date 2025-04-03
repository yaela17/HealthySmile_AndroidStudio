package com.example.healthysmile.controller;

public class ApiNodeMySqlRespuesta {
    private String message;
    private String mensaje;
    private long idUsuario;
    private long idEspecialista;
    private long idCita;
    private long idCarritoCompra;

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
    public long getIdCarritoCompra(){
        return idCarritoCompra;
    }
}
