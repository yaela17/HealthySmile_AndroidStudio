package com.example.healthysmile.ui.sign_up;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.healthysmile.IconMethods;
import com.example.healthysmile.R;
import com.example.healthysmile.ConexionFirebaseDB;

import java.util.HashMap;
import java.util.Map;

public class Sign_Up_Paciente extends Fragment {

    private EditText fragSignUpInputNombreUsuario, fragSignUpInputCorreo, fragSignUpInputContrasena;
    private Button fragSignUpBtnRegistrarse;
    private ConexionFirebaseDB dbHelper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign__up__paciente, container, false);

        // Inicializar la clase de conexión Firebase
        dbHelper = new ConexionFirebaseDB();

        fragSignUpInputNombreUsuario = view.findViewById(R.id.fragSignUpPacienteInputNombreUsuario);
        fragSignUpInputCorreo = view.findViewById(R.id.fragSignUpPacienteInputCorreo);
        fragSignUpInputContrasena = view.findViewById(R.id.fragSignUpPacienteInputContrasena);
        fragSignUpBtnRegistrarse = view.findViewById(R.id.fragSignUpPacienteBtnRegistrarse);

        // Configurar el botón de registro
        fragSignUpBtnRegistrarse.setOnClickListener(v -> registrarUsuario());

        IconMethods icono = new IconMethods();
        icono.setupPasswordVisibility(fragSignUpInputContrasena);

        return view;
    }

    private void registrarUsuario() {
        String nombre = fragSignUpInputNombreUsuario.getText().toString().trim();
        String correo = fragSignUpInputCorreo.getText().toString().trim();
        String contrasena = fragSignUpInputContrasena.getText().toString().trim();
        int nivelPermisos = 1; // Nivel de permisos fijo

        // Validar campos
        if (nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
            Toast.makeText(getActivity(), "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear un nuevo usuario en un mapa de datos
        Map<String, Object> user = new HashMap<>();
        user.put("nomUser", nombre);
        user.put("correoUser", correo);
        user.put("contrasenaUser", contrasena);
        user.put("tipoUser", "Paciente"); // Tipo de usuario fijo
        user.put("nivelPermisos", nivelPermisos);

        // Registrar el usuario utilizando ConexionFirebaseDB
        dbHelper.registrarUsuario(user, documentReference -> {
            Toast.makeText(getActivity(), "Usuario registrado con éxito", Toast.LENGTH_SHORT).show();
            Log.d("Firestore", "Usuario registrado con ID: " + documentReference.getId());
            limpiarCampos();
        }, e -> {
            Toast.makeText(getActivity(), "Error al registrar usuario", Toast.LENGTH_SHORT).show();
            Log.w("Firestore", "Error al registrar usuario", e);
        });
    }

    private void limpiarCampos() {
        fragSignUpInputNombreUsuario.setText("");
        fragSignUpInputCorreo.setText("");
        fragSignUpInputContrasena.setText("");
    }
}
