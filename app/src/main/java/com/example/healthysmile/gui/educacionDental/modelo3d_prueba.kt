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

class Modelo3dPrueba : Fragment(R.layout.fragment_modelo3d_prueba) {

    private lateinit var sceneView: SceneView
    private lateinit var loadingView: View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sceneView = view.findViewById(R.id.sceneView)
        loadingView = view.findViewById(R.id.loadingView)

        viewLifecycleOwner.lifecycleScope.launch {

            // Configuración de la cámara para mantenerla alejada
            sceneView.cameraNode.position = Position(0f, 0f, 20f)

            // Cargar el modelo 3D
            val modelFile = "modelito.glb"
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
