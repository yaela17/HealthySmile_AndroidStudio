package com.example.healthysmile.model.entities;

import java.util.Date;

public class Message {
    private Long idEspecialista;
    private Long idUsuario;
    private Long destinatario;
    private Long emisor;
    private Date  fecha;
    private String mensaje;

    public Message() {
        // Constructor vacío necesario para Firestore
    }

    public Message(Long idEspecialista, Long idUsuario, Long destinatario, Long emisor, Date  fecha, String mensaje) {
        this.idEspecialista = idEspecialista;
        this.idUsuario = idUsuario;
        this.destinatario = destinatario;
        this.emisor = emisor;
        this.fecha = fecha;
        this.mensaje = mensaje;
    }

    public Long getIdEspecialista() {
        return idEspecialista;
    }

    public void setIdEspecialista(Long idEspecialista) {
        this.idEspecialista = idEspecialista;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(Long destinatario) {
        this.destinatario = destinatario;
    }

    public Long getEmisor() {
        return emisor;
    }

    public void setEmisor(Long emisor) {
        this.emisor = emisor;
    }

    public Date  getFecha() {
        return fecha;
    }

    public void setFecha(Date  fecha) {
        this.fecha = fecha;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
