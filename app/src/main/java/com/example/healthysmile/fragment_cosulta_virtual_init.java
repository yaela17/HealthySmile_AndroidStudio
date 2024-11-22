package com.example.healthysmile;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class fragment_cosulta_virtual_init extends Fragment implements View.OnClickListener {

    Button btnEspecialista;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Infla el layout para este fragmento
        View view = inflater.inflate(R.layout.fragment_cosulta_virtual_init, container, false);

        // Vincula el botón y configura el listener
        btnEspecialista = view.findViewById(R.id.fragment_consultavirtual_init_btnEspecialista);
        btnEspecialista.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        // Verifica el texto del botón
        String textoBoton = ((Button) v).getText().toString();
        if (textoBoton.equals("ESPECIALISTA")) {
            // Obtiene el NavController desde el NavHostFragment
            NavController navController = NavHostFragment.findNavController(fragment_cosulta_virtual_init.this);

            // Navega al destino usando la acción definida en el gráfico de navegación
            navController.navigate(R.id.action_fragment_cosulta_virtual_init_to_fragment_consulta_virtual_especialista);
        } else {
            Toast.makeText(requireContext(), "Acción no reconocida", Toast.LENGTH_SHORT).show();
        }
    }
}
