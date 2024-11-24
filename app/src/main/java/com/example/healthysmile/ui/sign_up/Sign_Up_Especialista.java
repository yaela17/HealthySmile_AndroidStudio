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
    String[] opComboEspecialidad;
    CustomSpinnerAdapter adaptadorOpComboEspecialidad;

    EditText inputNombre,inputCorreo,inputContrasena,inputCedulaProfesional;
    EditText inputDescripcion;
    Spinner comboEspecialidad;
    Button btnRegistrarse;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign__up__especialista, container, false);

        opComboEspecialidad = new String[]{getString(R.string.opcion_combo_especialidad_selecciona_tu_especialidad),
                getString(R.string.opcion_combo_especialidad_odontologia_general),
                getString(R.string.opcion_combo_especialidad_ortodoncia),
                getString(R.string.opcion_combo_especialidad_periodoncia),
                getString(R.string.opcion_combo_especialidad_endodoncia),
                getString(R.string.opcion_combo_especialidad_cirugia_bucal),
                getString(R.string.opcion_combo_especialidad_implantologia),
                getString(R.string.opcion_combo_especialidad_odontopediatria),
                getString(R.string.opcion_combo_especialidad_odontologia_estetica),
                getString(R.string.opcion_combo_especialidad_prostodoncia),
                getString(R.string.opcion_combo_especialidad_radiologia_oral_y_maxilofacial),
                getString(R.string.opcion_combo_especialidad_medicina_oral),
                getString(R.string.opcion_combo_especialidad_patologia_oral),
                getString(R.string.opcion_combo_especialidad_odontologia_forense),
                getString(R.string.opcion_combo_especialidad_odontologia_geriatrica),
                getString(R.string.opcion_combo_especialidad_odontologia_preventiva),
                getString(R.string.opcion_combo_especialidad_terapia_orofacial),
                getString(R.string.opcion_combo_especialidad_oclusion),
                getString(R.string.opcion_combo_especialidad_sistemas_dentales_digitales),
                getString(R.string.opcion_combo_especialidad_higiene_dental)};

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