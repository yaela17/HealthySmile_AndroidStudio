package com.example.healthysmile.ui.modelos3d;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.util.Log;

import com.example.healthysmile.R;

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
        // No es necesario sobreescribir este método, ya que la inicialización se hace en onSurfaceCreated
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onRenderSurfaceCreated(config, gl, 50, 50);

        try {
            // Configuración de la luz direccional (DirectionalLight) para iluminar desde una dirección específica
            DirectionalLight light = new DirectionalLight(1f, -1f, -1f);  // Dirección de la luz
            light.setPosition(0, 0, 0);
            getCurrentScene().addLight(light);  // Añadir luz a la escena

            // Cargar el modelo OBJ
            LoaderOBJ loader = new LoaderOBJ(getContext().getResources(), getTextureManager(), modelResourceId);
            loader.parse();

            if (loader.getParsedObject() == null) {
                // Si el modelo no se carga correctamente, loguear el error en el log
                Log.e("Rajawali", "Error al cargar el modelo");
            } else {
                // Si el modelo se carga correctamente
                model = loader.getParsedObject();
                model.setScale(0.5f);  // Ajustar la escala del modelo
                model.setPosition(0, 0, 0);  // Ajustar la posición del modelo

                // Crear un material básico y asignarlo al modelo
                Material material = new Material();
                model.setMaterial(material);

                // Agregar el modelo a la escena
                getCurrentScene().addChild(model);
            }

        } catch (Exception e) {
            // En caso de error, registrar el error en el log
            Log.e("Rajawali", "Error al cargar el modelo", e);
        }

        // Configuración de la cámara
        getCurrentCamera().setPosition(0, 0, -5);  // Posición de la cámara
        getCurrentCamera().setLookAt(0, 0, 0);  // Asegurarse de que la cámara mire hacia el centro de la escena (modelo)

        // Configuración del color de fondo
        getCurrentScene().setBackgroundColor(0.1f, 0.1f, 0.1f,0.1f);  // Fondo oscuro para mejorar visibilidad
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // Método no utilizado en este caso
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // Método no utilizado en este caso
    }

    @Override
    public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
        // Este método no está siendo utilizado por el momento
    }

    @Override
    public void onTouchEvent(MotionEvent event) {
        // Si el modelo existe, podemos moverlo con los toques
        if (model != null) {
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastX = x;
                    lastY = y;
                    break;

                case MotionEvent.ACTION_MOVE:
                    // Calcular el movimiento del ratón o el dedo
                    float dx = x - lastX;
                    float dy = y - lastY;

                    // Rotar el modelo en base al movimiento del cursor
                    model.rotate(Vector3.Axis.Y, dx / 5.0f);  // Rotación en el eje Y
                    model.rotate(Vector3.Axis.X, dy / 5.0f);  // Rotación en el eje X

                    // Actualizar las posiciones de referencia
                    lastX = x;
                    lastY = y;
                    break;
            }
        }
    }
}
