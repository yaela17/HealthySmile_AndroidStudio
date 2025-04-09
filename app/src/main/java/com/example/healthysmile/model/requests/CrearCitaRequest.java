package com.example.healthysmile.model.requests;

public class CrearCitaRequest {
    private String fecha;
    private String hora;
    private String motivo;
    private Integer idUsuario;
    private Integer idEspecialista;

    public CrearCitaRequest(String fecha, String hora, Integer idEspecialista, Integer idUsuario, String motivo) {
        this.fecha = fecha;
        this.hora = hora;
        this.idEspecialista = idEspecialista;
        this.idUsuario = idUsuario;
        this.motivo = motivo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Integer getIdEspecialista() {
        return idEspecialista;
    }

    public void setIdEspecialista(Integer idEspecialista) {
        this.idEspecialista = idEspecialista;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
