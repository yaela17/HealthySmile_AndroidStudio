package com.example.healthysmile.gui.consulta;

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

public class Fragment_cosulta_virtual_init extends Fragment implements View.OnClickListener {

    Button btnEspecialista,btnAgendarCita;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Infla el layout para este fragmento
        View view = inflater.inflate(R.layout.fragment_cosulta_virtual_init, container, false);

        // Vincula el botón y configura el listener
        btnEspecialista = view.findViewById(R.id.fragment_consultavirtual_init_btnEspecialista);
        btnAgendarCita = view.findViewById(R.id.fragment_consultavirtual_init_btnAgendarCita);

        btnEspecialista.setOnClickListener(this);
        btnAgendarCita.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        // Verifica el texto del botón
        String textoBoton = ((Button) v).getText().toString();
        NavController navController = NavHostFragment.findNavController(Fragment_cosulta_virtual_init.this);
        if (textoBoton.equals(getActivity().getApplicationContext().getString(R.string.texto_boton_especialista))) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
            String tipoUsuario = sharedPreferences.getString("tipoUsuario", "Paciente");
            if(tipoUsuario.equals("Paciente")){
                navController.navigate(R.id.fragment_consulta_list_chat);
            }else
                if(tipoUsuario.equals("Especialista") || tipoUsuario.equals("Administrador")){
                    navController.navigate(R.id.fragment_consulta_list_chat_paciente);
                }
        }else
            if(textoBoton.equals(getActivity().getApplicationContext().getString(R.string.texto_boton_agendarcita))){
                navController.navigate(R.id.fragment_consulta_citas);
            }else {
            Toast.makeText(requireContext(), "Acción no reconocida", Toast.LENGTH_SHORT).show();
        }
    }
}
