package com.example.healthysmile.model.entities;

public class Especialista extends Usuario {

    private Integer idEspecialista;
    private String cedulaProfesional;
    private String descripcion;
    private String especialidad;

    public Especialista(String contrasenaUser, String correoUser, String fotoPerfil, Integer idUsuario, Integer nivelPermisos, String nomUser, String tipoUser, String cedulaProfesional, String descripcion, String especialidad, Integer idEspecialista) {
        super(contrasenaUser, correoUser, fotoPerfil, idUsuario, nivelPermisos, nomUser, tipoUser);
        this.cedulaProfesional = cedulaProfesional;
        this.descripcion = descripcion;
        this.especialidad = especialidad;
        this.idEspecialista = idEspecialista;
    }

    public Especialista(){

    }

    public String getCedulaProfesional() {
        return cedulaProfesional;
    }

    public void setCedulaProfesional(String cedulaProfesional) {
        this.cedulaProfesional = cedulaProfesional;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public Integer getIdEspecialista() {
        return idEspecialista;
    }

    public void setIdEspecialista(Integer idEspecialista) {
        this.idEspecialista = idEspecialista;
    }

    @Override
    public String toString() {
        return "Especialista{" +
                "cedulaProfesional='" + cedulaProfesional + '\'' +
                ", idEspecialista=" + idEspecialista +
                ", descripcion='" + descripcion + '\'' +
                ", especialidad='" + especialidad + '\'' +
                '}';
    }
}
