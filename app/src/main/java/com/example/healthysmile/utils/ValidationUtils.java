package com.example.healthysmile.utils;

import android.content.Context;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.healthysmile.R;

public class ValidationUtils {
    public boolean validarRegistroPaciente(EditText nombre, EditText correo, EditText contrasena) {
        boolean nombreValido = validarNombre(nombre);
        boolean correoValido = validarCorreo(correo);
        boolean contrasenaValida = validarContrasena(contrasena);
        return nombreValido && correoValido && contrasenaValida;
    }

    public boolean validarRegistroEspecialista(EditText inputNombre, EditText inputCorreo, EditText inputContrasena,
                                               EditText inputCedulaProfesional, Spinner comboEspecialidad, Context context) {
        boolean esNombreValido = validarNombre(inputNombre);
        boolean esCorreoValido = validarCorreo(inputCorreo);
        boolean esContrasenaValida = validarContrasena(inputContrasena);
        boolean esCedulaValida = validarCedulaProfesional(inputCedulaProfesional);
        boolean esEspecialidadValida = validarEspecialidad(comboEspecialidad, context);

        // Si algún campo es inválido, retorna false
        return esNombreValido && esCorreoValido && esContrasenaValida && esCedulaValida && esEspecialidadValida;
    }


    public boolean validarNombre(EditText campoNombre) {
        String nombre = campoNombre.getText().toString().trim();
        campoNombre.setError(null);

        if (nombre.isEmpty()) {
            campoNombre.setError("Este campo es obligatorio");
            return false;
        } else if (nombre.length() < 5) {
            campoNombre.setError("Debe tener al menos 5 caracteres");
            return false;
        }
        return true;
    }

    public boolean validarCorreo(EditText campoCorreo) {
        String correo = campoCorreo.getText().toString().trim();
        campoCorreo.setError(null);

        if (correo.isEmpty()) {
            campoCorreo.setError("Este campo es obligatorio");
            return false;
        }

        if (!correo.contains("@")) {
            campoCorreo.setError("Falta el símbolo @");
            return false;
        }

        String[] partesCorreo = correo.split("@");

        if (partesCorreo.length != 2) {
            campoCorreo.setError("El correo debe tener solo un @");
            return false;
        }

        String nombreUsuario = partesCorreo[0];
        String dominio = partesCorreo[1];

        if (nombreUsuario.isEmpty()) {
            campoCorreo.setError("Debe haber texto antes del @");
            return false;
        }

        if (dominio.isEmpty()) {
            campoCorreo.setError("Debe haber texto después del @");
            return false;
        }

        if (!dominio.contains(".")) {
            campoCorreo.setError("Falta el punto en el dominio (ej: .com)");
            return false;
        }

        String[] partesDominio = dominio.split("\\.");
        if (partesDominio.length < 2 || partesDominio[0].isEmpty() || partesDominio[1].isEmpty()) {
            campoCorreo.setError("Dominio inválido (ej: ejemplo.com)");
            return false;
        }
        return true;
    }


    public boolean validarContrasena(EditText campoContrasena) {
        String contrasena = campoContrasena.getText().toString().trim();
        campoContrasena.setError(null);

        if (contrasena.isEmpty()) {
            campoContrasena.setError("Este campo es obligatorio");
            return false;
        } else if (contrasena.length() < 6) {
            campoContrasena.setError("Debe tener al menos 6 caracteres");
            return false;
        }
        return true;
    }

    public boolean validarCedulaProfesional(EditText campoCedula) {
        String cedula = campoCedula.getText().toString().trim();
        campoCedula.setError(null);

        if (cedula.isEmpty()) {
            campoCedula.setError("Este campo es obligatorio");
            return false;
        } else if (cedula.length() != 8) {
            campoCedula.setError("Debe contener exactamente 8 caracteres");
            return false;
        }
        return true;
    }

    public boolean validarEspecialidad(Spinner comboEspecialidad, Context context) {
        String especialidad = comboEspecialidad.getSelectedItem().toString().trim();

        if (especialidad.equals(context.getString(com.example.healthysmile.R.string.opcion_combo_especialidad_selecciona_tu_especialidad))) {
            Toast.makeText(context, "Por favor selecciona una especialidad válida", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
