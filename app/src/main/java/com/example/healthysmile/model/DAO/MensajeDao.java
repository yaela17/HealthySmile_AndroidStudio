package com.example.healthysmile.model.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.healthysmile.model.entities.MensajeLocalDB;

import java.util.List;

@Dao
public interface MensajeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertarMensaje(MensajeLocalDB mensaje);


    @Query("SELECT * FROM mensajes WHERE (emisor = :userId AND destinatario = :otherId) OR (emisor = :otherId AND destinatario = :userId) ORDER BY fecha DESC LIMIT :limit")
    List<MensajeLocalDB> obtenerMensajesRecientes(long userId, long otherId, int limit);

    // Versión LiveData para UI reactiva
    @Query("SELECT * FROM mensajes WHERE (emisor = :userId AND destinatario = :otherId) OR (emisor = :otherId AND destinatario = :userId) ORDER BY fecha DESC LIMIT :limit")
    LiveData<List<MensajeLocalDB>> obtenerMensajesRecientesLive(long userId, long otherId, int limit);

    @Query("SELECT * FROM mensajes WHERE (emisor = :userId AND destinatario = :otherId) OR (emisor = :otherId AND destinatario = :userId) ORDER BY fecha ASC")
    List<MensajeLocalDB> obtenerTodosLosMensajes(long userId, long otherId);

}
