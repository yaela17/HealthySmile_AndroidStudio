package com.example.healthysmile.ui.sign_up;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.healthysmile.CustomSpinnerAdapter;
import com.example.healthysmile.IconMethods;
import com.example.healthysmile.R;

public class Sign_Up_Especialista extends Fragment implements View.OnClickListener {
    String[] opComboEspecialidad = {"Selecciona tu especialidad","Odontología General",
            "Ortodoncia", "Periodoncia", "Endodoncia", "Cirugía Bucal", "Implantología",
            "Odontopediatría", "Odontología Estética", "Prostodoncia", "Radiología Oral y Maxilofacial",
            "Medicina Oral", "Patología Oral", "Odontología Forense", "Odontología Geriátrica",
            "Odontología Preventiva", "Terapia Orofacial", "Oclusión", "Sistemas Dentales Digitales",
            "Higiene Dental"};
    CustomSpinnerAdapter adaptadorOpComboEspecialidad;

    EditText inputNombre,inputCorreo,inputContrasena,inputCedulaProfesional;
    EditText inputDescripcion;
    Spinner comboEspecialidad;
    Button btnRegistrarse;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign__up__especialista, container, false);

        // Inicializa las vistas
        inputNombre = view.findViewById(R.id.SignUpEspecialistaInputNombre);
        inputCorreo = view.findViewById(R.id.SignUpEspecialistaInputCorreo);
        inputContrasena = view.findViewById(R.id.SignUpEspecialistaInputContrasena);
        inputCedulaProfesional = view.findViewById(R.id.SignUpEspecialistaInputCedulaProfesional);
        comboEspecialidad = view.findViewById(R.id.SignUpEspecialistaComboEspecialidad);
        inputDescripcion = view.findViewById(R.id.SignUpEspecialistaInputDescripcion);
        btnRegistrarse = view.findViewById(R.id.SignUpEspecialistaBtnRegistrarse);
        btnRegistrarse.setOnClickListener(this);

        // Configura el adaptador personalizado
        adaptadorOpComboEspecialidad = new CustomSpinnerAdapter(getContext(), R.layout.custom_spinner_item, opComboEspecialidad);
        comboEspecialidad.setAdapter(adaptadorOpComboEspecialidad);

        // Actualiza la posición seleccionada cuando el usuario elige un elemento
        comboEspecialidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                adaptadorOpComboEspecialidad.setSelectedPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                adaptadorOpComboEspecialidad.setSelectedPosition(-1);
            }
        });

        IconMethods icono = new IconMethods();
        icono.setupPasswordVisibility(inputContrasena);

        return view;
    }


    @Override
    public void onClick(View v) {

    }
}