package com.example.healthysmile.model.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity(
        tableName = "mensajes",
        primaryKeys = {"emisor", "fecha"},
        indices = {
                @Index(value = {"id_usuario"}),
                @Index(value = {"destinatario"}),
                @Index(value = {"fecha"})
        }
)
public class MensajeLocalDB {

    @ColumnInfo(name = "id_usuario")
    private Long idUsuario;

    private Long idEspecialista;

    private Long destinatario;

    @NonNull
    private Long emisor;

    @NonNull
    @TypeConverters({com.example.healthysmile.model.DateConverter.class})
    private Date fecha;

    private String mensaje;

    public MensajeLocalDB() {
        // Constructor vacío requerido por Room
    }

    public MensajeLocalDB(Long idEspecialista, Long idUsuario, Long destinatario, Long emisor, @NonNull Date fecha, String mensaje) {
        this.idEspecialista = idEspecialista;
        this.idUsuario = idUsuario;
        this.destinatario = destinatario;
        this.emisor = emisor;
        this.fecha = fecha;
        this.mensaje = mensaje;
    }

    // Getters y setters
    public Long getIdEspecialista() {
        return idEspecialista;
    }

    public void setIdEspecialista(Long idEspecialista) {
        this.idEspecialista = idEspecialista;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(Long destinatario) {
        this.destinatario = destinatario;
    }

    public Long getEmisor() {
        return emisor;
    }

    public void setEmisor(Long emisor) {
        this.emisor = emisor;
    }

    @NonNull
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(@NonNull Date fecha) {
        this.fecha = fecha;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
