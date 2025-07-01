package com.example.healthysmile.model.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "chat_contactos")
public class ChatContactoLocalDB {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public long idUsuarioContacto; // Puede ser paciente o especialista
    public String tipoUsuarioContacto; // "Paciente" o "Especialista"
    public String nombre;  // Nuevo campo para el nombre
    public String correo;  // Nuevo campo para el correo
    public String fotoPerfil;  // Nuevo campo para la foto de perfil
}
