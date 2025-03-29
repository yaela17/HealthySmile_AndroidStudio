package com.example.healthysmile.gui.educacionDental;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.filament.*;
import com.google.android.filament.android.UiHelper;
import com.google.android.filament.utils.ModelViewer;
import com.google.android.filament.utils.Manipulator;
import com.example.healthysmile.R;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class Fragment_vizualizacion_modelos_3d_caries_dentales extends Fragment {
    private SurfaceView surfaceView;
    private ModelViewer modelViewer;
    private Engine engine;
    private UiHelper uiHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_visualizacion_modelos_3d_caries_dentales, container, false);
        surfaceView = view.findViewById(R.id.modelo3dprueba);

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                setupFilament();
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
                modelViewer.getView().setViewport(new Viewport(0, 0, width, height));
            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                engine.destroy();
            }
        });

        return view;
    }

    private void setupFilament() {
        engine = Engine.create();
        uiHelper = new UiHelper(UiHelper.ContextErrorPolicy.DONT_CHECK);
        uiHelper.attachTo(surfaceView);

        Manipulator manipulator = new Manipulator.Builder()
                .targetPosition(0, 0, 0)
                .orbitHomePosition(0, 0, 5)
                .zoomSpeed(0.05f)
                .build(Manipulator.Mode.ORBIT);

        modelViewer = new ModelViewer(surfaceView, engine, uiHelper, manipulator);
        loadModel("scene.gltf");
    }

    private void loadModel(String assetPath) {
        try {
            InputStream inputStream = getContext().getAssets().open(assetPath);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();

            ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
            modelViewer.loadModelGltf(byteBuffer, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
