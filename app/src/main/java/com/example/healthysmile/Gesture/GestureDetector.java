package com.example.healthysmile.Gesture;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;

public class GestureDetector implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private float lastX;
    private long lastTime;
    private final Context context;
    private final GestureCallback callback;

    // Interfaz para notificar gestos
    public interface GestureCallback {
        void onGestureDetected(String gestureName);
    }

    public GestureDetector(Context context, GestureCallback callback) {
        this.context = context;
        this.callback = callback;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
    }

    public void start() {
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        }
    }

    public void stop() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        long currentTime = System.currentTimeMillis();

        // Detecta movimientos menos bruscos hacia la derecha o izquierda
        if ((currentTime - lastTime) > 250) { // Tiempo ajustado a 250 ms
            float deltaX = x - lastX;

            if (Math.abs(deltaX) > 3) { // Umbral ajustado a 3 para detectar movimientos más pequeños
                if (deltaX > 0) {
                    String gestureName = "Movimiento a la derecha";
                    Toast.makeText(context, gestureName, Toast.LENGTH_SHORT).show();
                    if (callback != null) callback.onGestureDetected("SwipeRight");
                } else {
                    String gestureName = "Movimiento a la izquierda";
                    Toast.makeText(context, gestureName, Toast.LENGTH_SHORT).show();
                    if (callback != null) callback.onGestureDetected("SwipeLeft");
                }
            }

            lastTime = currentTime;
            lastX = x;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // No se utiliza en este caso
    }
}
