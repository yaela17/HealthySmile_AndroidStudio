package com.example.healthysmile.ui.sign_up;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.healthysmile.ConexionFirebaseDB;
import com.example.healthysmile.Adaptadores.CustomSpinnerAdapter;
import com.example.healthysmile.IconMethods;
import com.example.healthysmile.ManejadorShadPreferences;
import com.example.healthysmile.R;

import java.util.HashMap;
import java.util.Map;

public class Sign_Up_Especialista extends Fragment implements View.OnClickListener {
    String[] opComboEspecialidad;
    CustomSpinnerAdapter adaptadorOpComboEspecialidad;
    ConexionFirebaseDB dbHelper;

    EditText inputNombre, inputCorreo, inputContrasena, inputCedulaProfesional;
    EditText inputDescripcion;
    Spinner comboEspecialidad;
    Button btnRegistrarse;
    ManejadorShadPreferences manejadorShadPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign__up__especialista, container, false);

        dbHelper = new ConexionFirebaseDB();
        manejadorShadPreferences = new ManejadorShadPreferences(getContext());

        // Inicializa las vistas
        inputNombre = view.findViewById(R.id.SignUpEspecialistaInputNombre);
        inputCorreo = view.findViewById(R.id.SignUpEspecialistaInputCorreo);
        inputContrasena = view.findViewById(R.id.SignUpEspecialistaInputContrasena);
        inputCedulaProfesional = view.findViewById(R.id.SignUpEspecialistaInputCedulaProfesional);
        comboEspecialidad = view.findViewById(R.id.SignUpEspecialistaComboEspecialidad);
        inputDescripcion = view.findViewById(R.id.SignUpEspecialistaInputDescripcion);
        btnRegistrarse = view.findViewById(R.id.SignUpEspecialistaBtnRegistrarse);
        btnRegistrarse.setOnClickListener(this);

        // Inicializa las opciones del spinner
        opComboEspecialidad = new String[]{
                getString(R.string.opcion_combo_especialidad_selecciona_tu_especialidad),
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
                getString(R.string.opcion_combo_especialidad_higiene_dental)
        };

        // Configura el adaptador personalizado
        adaptadorOpComboEspecialidad = new CustomSpinnerAdapter(getContext(), R.layout.custom_spinner_item, opComboEspecialidad);
        comboEspecialidad.setAdapter(adaptadorOpComboEspecialidad);

        // Configura el listener para el spinner
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
        if (v.getId() == R.id.SignUpEspecialistaBtnRegistrarse) {
            registrarEspecialista();
        }
    }

    private void registrarEspecialista() {
        String nombre = inputNombre.getText().toString().trim();
        String correo = inputCorreo.getText().toString().trim();
        String contrasena = inputContrasena.getText().toString().trim();
        String cedulaProfesional = inputCedulaProfesional.getText().toString().trim();
        String especialidad = comboEspecialidad.getSelectedItem().toString().trim();
        String descripcion = inputDescripcion.getText().toString().trim();
        int nivelPermisos = 2; // Permisos más altos para especialistas

        if (nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty() || cedulaProfesional.isEmpty() ||
                especialidad.equals(getString(R.string.opcion_combo_especialidad_selecciona_tu_especialidad))) {
            Toast.makeText(getActivity(), "Por favor completa todos los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear el mapa de datos
        Map<String, Object> user = new HashMap<>();
        user.put("nomUser", nombre);
        user.put("correoUser", correo);
        user.put("contrasenaUser", contrasena);
        user.put("tipoUser", "Especialista");
        user.put("nivelPermisos", nivelPermisos);

        Map<String, Object> especialistaData = new HashMap<>();
        especialistaData.put("cedulaProfesional", cedulaProfesional);
        especialistaData.put("descripcion", descripcion);
        especialistaData.put("especialidad", especialidad);

        dbHelper.registrarEspecialista(user, especialistaData, documentReference -> {
            Toast.makeText(getActivity(), "Especialista registrado con éxito", Toast.LENGTH_SHORT).show();
            manejadorShadPreferences.guardarEspecialista(nombre,correo,"Especialista",2,cedulaProfesional,descripcion,especialidad);
            limpiarCampos();
        }, e -> {
            Toast.makeText(getActivity(), "Error al registrar especialista", Toast.LENGTH_SHORT).show();
            Log.w("Firestore", "Error al registrar especialista", e);
        });
    }

    private void limpiarCampos() {
        inputNombre.setText("");
        inputCorreo.setText("");
        inputContrasena.setText("");
        inputCedulaProfesional.setText("");
        inputDescripcion.setText("");
        comboEspecialidad.setSelection(0);
    }
}
