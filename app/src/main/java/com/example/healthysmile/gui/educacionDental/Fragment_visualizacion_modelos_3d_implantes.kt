package com.example.healthysmile.gui.educacionDental

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.healthysmile.R
import io.github.sceneview.SceneView
import io.github.sceneview.math.Position
import io.github.sceneview.math.Scale
import io.github.sceneview.node.ModelNode
import kotlinx.coroutines.launch

class Fragment_visualizacion_modelos_3d_implantes : Fragment(R.layout.fragment_visualizacion_modelos_3d_implantes) {

    private lateinit var sceneView: SceneView
    private lateinit var loadingView: View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sceneView = view.findViewById(R.id.sceneViewImplantes)
        loadingView = view.findViewById(R.id.loadingViewImplantes)

        viewLifecycleOwner.lifecycleScope.launch {

            // Configuración de la cámara para mantenerla alejada
            sceneView.cameraNode.position = Position(0f, 0f, 20f)

            // Cargar el modelo 3D
            val modelFile = "implantes.glb"
            val modelNode = ModelNode(
                sceneView.modelLoader.createModelInstance(modelFile),
                scaleToUnits = 2f
            )
            modelNode.scale = Scale(0.005f)
            sceneView.addChildNode(modelNode)

            sceneView.cameraNode.lookAt(Position(0f, 0f, 20f))

            loadingView.visibility = View.GONE
        }
    }
}
