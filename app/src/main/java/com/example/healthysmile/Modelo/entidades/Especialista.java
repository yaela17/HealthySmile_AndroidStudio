package com.example.healthysmile.Modelo.entidades;

public class Especialista extends Usuario {

    private Integer idEspecialista;
    private String cedulaProfesional;
    private String descripcion;
    private String especialidad;

    public Especialista(String contrasenaUser, String correoUser, String fotoPerfil, Integer idUsuario, Integer nivelPermisos, String nomUser) {
        super(contrasenaUser, correoUser, fotoPerfil, idUsuario, nivelPermisos, nomUser);
    }
}
