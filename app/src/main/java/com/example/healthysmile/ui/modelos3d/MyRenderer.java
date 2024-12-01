package com.example.healthysmile.ui.modelos3d;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

import org.rajawali3d.Object3D;
import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.loader.LoaderOBJ;
import org.rajawali3d.materials.Material;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.renderer.RajawaliRenderer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyRenderer extends RajawaliRenderer implements GLSurfaceView.Renderer {

    private int modelResourceId;
    private Object3D model;
    private float lastX, lastY;

    // Constructor
    public MyRenderer(Context context, int modelResourceId) {
        super(context);
        this.modelResourceId = modelResourceId;
    }

    @Override
    protected void initScene() {
        // Configura una luz direccional
        DirectionalLight light = new DirectionalLight(1f, -1f, -1f);
        light.setPosition(0, 0, 0);
        getCurrentScene().addLight(light);

        // Configura la cámara
        getCurrentCamera().setPosition(0, 0, -5);
        getCurrentCamera().setLookAt(0, 0, 0);

        // Configura el color de fondo
        getCurrentScene().setBackgroundColor(0.5f, 0.5f, 0.5f, 1.0f);

        try {
            // Carga el modelo desde el archivo .obj
            LoaderOBJ loader = new LoaderOBJ(getContext().getResources(), getTextureManager(), modelResourceId);
            loader.parse();
            model = loader.getParsedObject();

            if (model == null) {
                Log.e("Rajawali", "Error al cargar el modelo");
                return;
            }

            // Ajusta escala y posición del modelo
            model.setScale(1.0f);
            model.setPosition(0, 0, 0);

            // Asigna un material básico si el modelo no tiene
            if (model.getMaterial() == null) {
                Material defaultMaterial = new Material();
                model.setMaterial(defaultMaterial);
            }

            // Agrega el modelo a la escena
            getCurrentScene().addChild(model);

        } catch (Exception e) {
            Log.e("Rajawali", "Error al cargar el modelo", e);
        }
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onRenderSurfaceCreated(config, gl, 50, 50);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // Método estándar, sin necesidad de modificaciones
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // Método estándar, sin necesidad de modificaciones
    }

    @Override
    public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
        // No se utiliza en este caso
    }

    public void onTouchEvent(MotionEvent event) {
        // Maneja los eventos táctiles para rotar el modelo
        if (model != null) {
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastX = x;
                    lastY = y;
                    break;

                case MotionEvent.ACTION_MOVE:
                    float dx = x - lastX;
                    float dy = y - lastY;

                    model.rotate(Vector3.Axis.Y, dx / 5.0f);  // Rotación en el eje Y
                    model.rotate(Vector3.Axis.X, dy / 5.0f);  // Rotación en el eje X

                    lastX = x;
                    lastY = y;
                    break;
            }
        }
    }
}
