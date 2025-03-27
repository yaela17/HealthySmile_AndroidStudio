package com.example.healthysmile.gui.extraAndroid.adaptadores;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.example.healthysmile.R;
import com.example.healthysmile.gui.ayudaYSoporte.Fragment_ayuda_y_soporte;
import com.example.healthysmile.utils.SharedPreferencesHelper;

public class FormularioAgregarPreguntaFrecuente extends DialogFragment {

    private EditText preguntaAgregar;
    private Button btnEnviar;
    private Context context;
    private SharedPreferencesHelper sharedPreferencesHelper;

    public FormularioAgregarPreguntaFrecuente(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Infla el layout del formulario
        View view = inflater.inflate(R.layout.formulario_agregar_pregunta_frecuente, container, false);
        preguntaAgregar = view.findViewById(R.id.formulario_agregar_pregunta_frecuente_edita_text_pregunta);
        btnEnviar = view.findViewById(R.id.formulario_agregar_pregunta_frecuente_btn_enviar);
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String pregunta = preguntaAgregar.getText().toString().trim();
                if (pregunta.isEmpty()) {
                    Log.e("Error", "El correo o la pregunta no pueden estar vacíos");
                    return;
                }

                sharedPreferencesHelper = new SharedPreferencesHelper(context);
                long idUsuario = sharedPreferencesHelper.obtenerIdUsuario();
                Log.d("VerificarCorreo", "ID de Usuario: " + idUsuario);

                // Obtener el fragmento existente en lugar de crear uno nuevo
                FragmentManager fragmentManager = getParentFragmentManager();
                Fragment_ayuda_y_soporte fragmentAyudaYSoporte =
                        (Fragment_ayuda_y_soporte) fragmentManager.findFragmentById(R.id.nav_host_fragment_content_navigation_drawer_fragments);

                if (fragmentAyudaYSoporte != null) {
                    fragmentAyudaYSoporte.insertarPreguntaFrecuente(idUsuario, pregunta);

                    // Limpiar el EditText
                    preguntaAgregar.setText("");

                    // Cerrar el DialogFragment
                    dismiss();
                } else {
                    Log.e("Error", "No se encontró el fragmento activo");
                }
            }
        });
        return view;
    }

}
