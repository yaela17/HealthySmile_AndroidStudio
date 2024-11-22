package com.example.healthysmile;

public class Usuario {
    private String idUsuario;
    private String nombreUsuario;
    private String correoUsuario;
    private String contrasenaUsuario;
    private String tipoUsuario;
    private int nivelPermisosUsuario;
    private String fotoPerfil;

    public Usuario(String idUsuario,String nombreUsuario, String correoUsuario, String contrasenaUsuario, String tipoUsuario, String fotoPerfil) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.correoUsuario = correoUsuario;
        this.contrasenaUsuario = contrasenaUsuario;
        this.tipoUsuario = tipoUsuario;
        if(tipoUsuario.equals("Paciente")){
            this.nivelPermisosUsuario = 1;
        }else
            if(tipoUsuario.equals("Especilista")){
                this.nivelPermisosUsuario = 2;
            }
        this.fotoPerfil = fotoPerfil;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getCorreoUsuario() {
        return correoUsuario;
    }

    public void setCorreoUsuario(String correoUsuario) {
        this.correoUsuario = correoUsuario;
    }

    public String getContrasenaUsuario() {
        return contrasenaUsuario;
    }

    public void setContrasenaUsuario(String contrasenaUsuario) {
        this.contrasenaUsuario = contrasenaUsuario;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public int getNivelPermisosUsuario() {
        return nivelPermisosUsuario;
    }

    public void setNivelPermisosUsuario(int nivelPermisosUsuario) {
        this.nivelPermisosUsuario = nivelPermisosUsuario;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }
}
