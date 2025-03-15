package com.example.healthysmile.model.entities;

public class Usuario {
    private Integer idUsuario;
    private String nomUser;
    private String correoUser;
    private String contrasenaUser;
    private String tipoUser;
    private Integer nivelPermisos;
    private String fotoPerfil;

    public Usuario(String contrasenaUser, String correoUser, String fotoPerfil, Integer idUsuario, Integer nivelPermisos, String nomUser, String tipoUser) {
        this.contrasenaUser = contrasenaUser;
        this.correoUser = correoUser;
        this.fotoPerfil = fotoPerfil;
        this.idUsuario = idUsuario;
        this.nivelPermisos = nivelPermisos;
        this.nomUser = nomUser;
        this.tipoUser = tipoUser;
    }

    public Usuario() {
    }

    public String getContrasenaUser() {
        return contrasenaUser;
    }

    public void setContrasenaUser(String contrasenaUser) {
        this.contrasenaUser = contrasenaUser;
    }

    public String getCorreoUser() {
        return correoUser;
    }

    public void setCorreoUser(String correoUser) {
        this.correoUser = correoUser;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getNivelPermisos() {
        return nivelPermisos;
    }

    public void setNivelPermisos(Integer nivelPermisos) {
        this.nivelPermisos = nivelPermisos;
    }

    public String getNomUser() {
        return nomUser;
    }

    public void setNomUser(String nomUser) {
        this.nomUser = nomUser;
    }

    public String getTipoUser() {
        return tipoUser;
    }

    public void setTipoUser(String tipoUser) {
        this.tipoUser = tipoUser;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "contrasenaUser='" + contrasenaUser + '\'' +
                ", idUsuario=" + idUsuario +
                ", nomUser='" + nomUser + '\'' +
                ", correoUser='" + correoUser + '\'' +
                ", tipoUser='" + tipoUser + '\'' +
                ", nivelPermisos=" + nivelPermisos +
                ", fotoPerfil='" + fotoPerfil + '\'' +
                '}';
    }
}
