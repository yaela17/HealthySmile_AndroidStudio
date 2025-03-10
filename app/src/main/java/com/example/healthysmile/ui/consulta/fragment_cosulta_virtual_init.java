package com.example.healthysmile.ui.consulta;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.healthysmile.R;

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
        if (textoBoton.equals(getActivity().getApplicationContext().getString(R.string.texto_boton_especialista))) {
            NavController navController = NavHostFragment.findNavController(fragment_cosulta_virtual_init.this);
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
            String tipoUsuario = sharedPreferences.getString("tipoUsuario", "Paciente");
            if(tipoUsuario.equals("Paciente")){
                navController.navigate(R.id.fragment_consulta_list_chat);
            }else
                if(tipoUsuario.equals("Especialista") || tipoUsuario.equals("Administrador")){
                    navController.navigate(R.id.fragment_consulta_list_chat_paciente);
                }

        } else {
            Toast.makeText(requireContext(), "Acción no reconocida", Toast.LENGTH_SHORT).show();
        }
    }
}
