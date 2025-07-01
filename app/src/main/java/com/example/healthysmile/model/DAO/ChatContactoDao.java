package com.example.healthysmile.model.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.healthysmile.model.entities.ChatContactoLocalDB;

import java.util.List;

@Dao
public interface ChatContactoDao {

    @Insert
    void insertarContacto(ChatContactoLocalDB contacto);

    @Query("SELECT * FROM chat_contactos")
    List<ChatContactoLocalDB> obtenerTodos();

    @Query("DELETE FROM chat_contactos")
    void eliminarTodos();

    @Query("UPDATE chat_contactos SET fotoPerfil = :fotoPerfil WHERE nombre = :nombre")
    void actualizarFotoPerfilPorNombre(String nombre, String fotoPerfil);

}
