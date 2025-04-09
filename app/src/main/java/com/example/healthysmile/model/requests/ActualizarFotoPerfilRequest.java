package com.example.healthysmile.model.requests;

public class ActualizarFotoPerfilRequest {
    private Integer idUsuario;
    private String foto;

    public ActualizarFotoPerfilRequest(String foto, Integer idUsuario) {
        this.foto = foto;
        this.idUsuario = idUsuario;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }
}
