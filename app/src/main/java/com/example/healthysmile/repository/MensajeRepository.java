package com.example.healthysmile.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.healthysmile.model.LocalDB;
import com.example.healthysmile.model.DAO.MensajeDao;
import com.example.healthysmile.model.entities.MensajeLocalDB;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MensajeRepository {

    private final MensajeDao mensajeDao;
    private final ExecutorService executorService;

    public MensajeRepository(Context context) {
        LocalDB db = LocalDB.getInstance(context);
        mensajeDao = db.messageDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insertarMensaje(MensajeLocalDB mensaje) {
        executorService.execute(() -> mensajeDao.insertarMensaje(mensaje));
    }

    public List<MensajeLocalDB> obtenerMensajesRecientes(long userId, long otherId, int limit) {
        return mensajeDao.obtenerMensajesRecientes(userId, otherId, limit);
    }

    public LiveData<List<MensajeLocalDB>> obtenerMensajesRecientesLive(long userId, long otherId, int limit) {
        return mensajeDao.obtenerMensajesRecientesLive(userId, otherId, limit);
    }

    public List<MensajeLocalDB> obtenerTodosLosMensajes(long userId, long otherId) {
        return mensajeDao.obtenerTodosLosMensajes(userId, otherId);
    }

}
