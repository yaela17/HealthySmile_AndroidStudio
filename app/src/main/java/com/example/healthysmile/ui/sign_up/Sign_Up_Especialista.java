package com.example.healthysmile.ui.sign_up;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.healthysmile.R;

public class Sign_Up_Especialista extends Fragment implements View.OnClickListener {
    String[] opComboEspecialidad = {"Selecciona tu especialidad","Especialidad1","Especialidad2"};
    ArrayAdapter adaptadorOpComboEspecialidad;

    EditText inputNombre,inputCorreo,inputContrasena,inputCedulaProfesional;
    EditText inputDescripcion;
    Spinner comboEspecialidad;
    Button btnRegistrarse;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = null;
        view = inflater.inflate(R.layout.fragment_sign__up__especialista,container,false);
        inputNombre = view.findViewById(R.id.SignUpEspecialistaInputNombre);
        inputCorreo = view.findViewById(R.id.SignUpEspecialistaInputCorreo);
        inputContrasena = view.findViewById(R.id.SignUpEspecialistaInputContrasena);
        inputDescripcion = view.findViewById(R.id.SignUpEspecialistaInputCedulaProfesional);
        comboEspecialidad = view.findViewById(R.id.SignUpEspecialistaComboEspecialidad);
        adaptadorOpComboEspecialidad = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,opComboEspecialidad);
        adaptadorOpComboEspecialidad.setDropDownViewResource(R.layout.custom_spinner_item);
        comboEspecialidad.setAdapter(adaptadorOpComboEspecialidad);
        btnRegistrarse = view.findViewById(R.id.SignUpEspecialistaBtnRegistrarse);
        btnRegistrarse.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {

    }
}