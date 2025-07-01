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

class Fragment_visualizacion_modelos_3d_sensibilidad_dental : Fragment(R.layout.fragment_visualizacion_modelos_3d_sensibilidad_dental) {

    private lateinit var sceneView: SceneView
    private lateinit var loadingView: View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sceneView = view.findViewById(R.id.sceneViewSensibilidad)
        loadingView = view.findViewById(R.id.loadingViewSensibilidad)

        viewLifecycleOwner.lifecycleScope.launch {
            sceneView.cameraNode.position = Position(0f, 0f, 2f)

            val modelFile = "sensibilidad_dental.glb"
            val modelNode = ModelNode(
                sceneView.modelLoader.createModelInstance(modelFile),
                scaleToUnits = 0.5f  // ajusta escala para que no sea gigante
            )
            // modelNode.scale = Scale(0.11f) // eliminar esta línea

            modelNode.position = Position(0f, 0.1f, 0f) // posición centrada, puedes ajustar Y si quieres

            sceneView.addChildNode(modelNode)

            sceneView.cameraNode.lookAt(modelNode)

            loadingView.visibility = View.GONE
        }
    }
}
