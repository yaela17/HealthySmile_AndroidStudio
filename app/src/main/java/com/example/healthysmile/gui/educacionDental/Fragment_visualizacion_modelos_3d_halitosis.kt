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


class Fragment_visualizacion_modelos_3d_halitosis : Fragment(R.layout.fragment_visualizacion_modelos_3d_halitosis) {

    private lateinit var sceneView: SceneView
    private lateinit var loadingView: View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sceneView = view.findViewById(R.id.sceneViewHalitosis)
        loadingView = view.findViewById(R.id.loadingViewHalitosis)

        viewLifecycleOwner.lifecycleScope.launch {
            sceneView.cameraNode.position = Position(0f, 0f, 3f) // un poco más alejada para mejor vista

            val modelFile = "halitosis.glb"
            val modelNode = ModelNode(
                sceneView.modelLoader.createModelInstance(modelFile),
                scaleToUnits = 0.5f // escala adecuada para que no sea gigante ni minúsculo
            )

            // Posición del modelo, ajusta para que no salga de la pantalla
            modelNode.position = Position(0f, -0.3f, 0f)

            sceneView.addChildNode(modelNode)

            sceneView.cameraNode.lookAt(modelNode)

            loadingView.visibility = View.GONE
        }
    }
}
