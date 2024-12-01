package com.example.healthysmile.ui.modelos3d;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.healthysmile.R;

import android.opengl.GLSurfaceView;

public class fragment_modelo3d extends Fragment {
    private GLSurfaceView glSurfaceView;
    private MyRenderer renderer;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate el layout del Fragment
        View view = inflater.inflate(R.layout.fragment_modelo3d, container, false);
        int modelResourceId = R.raw.modelo_3d;
        // Inicializar GLSurfaceView
        glSurfaceView = view.findViewById(R.id.glSurfaceView);

        // Configurar el GLSurfaceView con el Renderer
        Context context = requireContext(); // Obtener contexto del Fragment
        renderer = new MyRenderer(context, modelResourceId); // Reemplazar con el ID de tu modelo
        glSurfaceView.setEGLContextClientVersion(2); // OpenGL ES 2.0
        glSurfaceView.setRenderer(renderer);

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (glSurfaceView != null) {
            glSurfaceView.onPause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (glSurfaceView != null) {
            glSurfaceView.onResume();
        }
    }
}
