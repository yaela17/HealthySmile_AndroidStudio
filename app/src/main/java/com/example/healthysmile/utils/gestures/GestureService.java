package com.example.healthysmile.utils.gestures;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class GestureService extends Service {
    private GestureDetector gestureDetector;

    @Override
    public void onCreate() {
        super.onCreate();
        gestureDetector = new GestureDetector(this, gestureName -> {
            // Notificar los gestos detectados
            notifyGesture(gestureName);
        });
        gestureDetector.start(); // Inicia la escucha de movimientos
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        gestureDetector.stop(); // Detén la escucha al destruir el servicio
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null; // No necesitamos vincular este servicio
    }

    private void notifyGesture(String gestureName) {
        Intent intent = new Intent("com.example.GESTURE_DETECTED");
        intent.putExtra("gesture", gestureName);
        sendBroadcast(intent); // Enviar el evento a los receptores
    }
}
