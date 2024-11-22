package com.example.healthysmile;

public class Especialista extends Usuario {

    private String cedulaProfesional;
    private String especialidad;
    private String descripcion;

    public Especialista(String idUsuario,String nombreUsuario, String correoUsuario, String contrasenaUsuario,String fotoPerfil, String cedulaProfesional, String descripcion, String especialidad) {
        super(idUsuario,nombreUsuario, correoUsuario, contrasenaUsuario, "Especialista",fotoPerfil);
        this.cedulaProfesional = cedulaProfesional;
        this.descripcion = descripcion;
        this.especialidad = especialidad;
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
