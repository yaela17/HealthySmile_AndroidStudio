package com.example.healthysmile.Modelo.entidades;

public class Usuario {
    private Integer idUsuario;
    private String nomUser;
    private String correoUser;
    private String contrasenaUser;
    private Integer nivelPermisos;
    private String fotoPerfil;

    public Usuario(String contrasenaUser, String correoUser, String fotoPerfil, Integer idUsuario, Integer nivelPermisos, String nomUser) {
        this.contrasenaUser = contrasenaUser;
        this.correoUser = correoUser;
        this.fotoPerfil = fotoPerfil;
        this.idUsuario = idUsuario;
        this.nivelPermisos = nivelPermisos;
        this.nomUser = nomUser;
    }
}
