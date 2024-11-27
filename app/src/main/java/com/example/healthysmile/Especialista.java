package com.example.healthysmile;

public class Especialista extends Usuario {

    private long idEspecialista;
    private String cedulaProfesional;
    private String especialidad;
    private String descripcion;

    public Especialista(long idUsuario,String nombreUsuario, String correoUsuario, String contrasenaUsuario,String fotoPerfil,long idEspecialista, String cedulaProfesional, String descripcion, String especialidad) {
        super(idUsuario,nombreUsuario, correoUsuario, contrasenaUsuario, "Especialista",fotoPerfil);
        this.idEspecialista = idEspecialista;
        this.cedulaProfesional = cedulaProfesional;
        this.descripcion = descripcion;
        this.especialidad = especialidad;
    }

    public long getIdEspecialista() {
        return idEspecialista;
    }

    public void setIdEspecialista(long idEspecialista) {
        this.idEspecialista = idEspecialista;
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
}
