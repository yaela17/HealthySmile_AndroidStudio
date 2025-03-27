package com.example.healthysmile.gui.extraAndroid.adaptadores;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.healthysmile.R;

import java.util.ArrayList;
import java.util.List;

public class FormularioModificarCitaPaciente extends DialogFragment {

    private String date;
    private EditText editTextMotivo;
    private Spinner spinnerHorarios, spinnerEspecialistas;
    private Button btnGuardar;
    private List<Long> idsEspecialistas = new ArrayList<>();
    long idEspecialistaSeleccionado;

    public FormularioModificarCitaPaciente(String date,long idEspecialistaSeleccionado) {
        this.date = date;
        this.idEspecialistaSeleccionado = idEspecialistaSeleccionado;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Infla el layout del formulario
        View view = inflater.inflate(R.layout.formulario_modificar_cita_paciente, container, false);

        // Referencias a los elementos del formulario
        editTextMotivo = view.findViewById(R.id.FormularioModificarCitaInputMotivoCita);
        spinnerHorarios = view.findViewById(R.id.FormularioModificarCitaComboHorariosDisponibles);
        spinnerEspecialistas = view.findViewById(R.id.FormularioModificarCitaComboEspecialistas);
        btnGuardar = view.findViewById(R.id.FormularioModificarCitaBtnGuardar);

        // Acción para guardar los cambios (puedes agregar validaciones o lógica de negocio aquí)
        btnGuardar.setOnClickListener(v -> {
            // Lógica para guardar la cita modificada
            String motivo = editTextMotivo.getText().toString();
            String hora = (String) spinnerHorarios.getSelectedItem(); // Obtiene la hora seleccionada
            String especialista = (String) spinnerEspecialistas.getSelectedItem(); // Obtiene el especialista seleccionado

            // Lógica para verificar si los campos están vacíos
            if (motivo.isEmpty() || hora == null || especialista == null) {
                Toast.makeText(getContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                // Aquí puedes hacer algo con los datos, como enviarlos al servidor
                Toast.makeText(getContext(), "Cita modificada", Toast.LENGTH_SHORT).show();
                dismiss(); // Cierra el formulario
            }
        });
        return view;
    }
}
