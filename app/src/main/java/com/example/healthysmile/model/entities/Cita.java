package com.example.healthysmile.model.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Cita {
    private Integer idCita;
    private String motivoCita;
    private LocalDate fechaCita;

    public Cita(LocalDate fechaCita, Integer idCita, String motivoCita) {
        this.fechaCita = fechaCita;
        this.idCita = idCita;
        this.motivoCita = motivoCita;
    }

    public LocalDate getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(LocalDate fechaCita) {
        this.fechaCita = fechaCita;
    }

    public Integer getIdCita() {
        return idCita;
    }

    public void setIdCita(Integer idCita) {
        this.idCita = idCita;
    }

    public String getMotivoCita() {
        return motivoCita;
    }

    public void setMotivoCita(String motivoCita) {
        this.motivoCita = motivoCita;
    }
}
