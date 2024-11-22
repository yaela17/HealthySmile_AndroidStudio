package com.example.healthysmile;

import java.util.Date;

public class Message {
    private String remitenteId;
    private String destinatarioId;
    private String text;
    private Date timestamp;

    public Message() {
        // Constructor vacío necesario para Firestore
    }

    public Message(String remitenteId, String destinatarioId, String text, Date timestamp) {
        this.remitenteId = remitenteId;
        this.destinatarioId = destinatarioId;
        this.text = text;
        this.timestamp = timestamp;
    }

    public String getRemitenteId() {
        return remitenteId;
    }

    public void setRemitenteId(String remitenteId) {
        this.remitenteId = remitenteId;
    }

    public String getDestinatarioId() {
        return destinatarioId;
    }

    public void setDestinatarioId(String destinatarioId) {
        this.destinatarioId = destinatarioId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
