package com.example.healthysmile.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.healthysmile.model.DAO.ChatContactoDao;
import com.example.healthysmile.model.entities.ChatContactoLocalDB;
import com.example.healthysmile.model.entities.MensajeLocalDB;
import com.example.healthysmile.model.DAO.MensajeDao;

@Database(
        entities = {MensajeLocalDB.class, ChatContactoLocalDB.class}, // ← AÑADE AQUÍ LA NUEVA ENTIDAD
        version = 5
)
@TypeConverters({DateConverter.class})
public abstract class LocalDB extends RoomDatabase {

    private static volatile LocalDB INSTANCE;

    public abstract ChatContactoDao chatContactoDao();

    public abstract MensajeDao messageDao();

    public static LocalDB getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (LocalDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    LocalDB.class, "healthysmile_messages_db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
