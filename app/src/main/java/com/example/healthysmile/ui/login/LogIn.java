package com.example.healthysmile.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthysmile.NavigationDrawerFragments;
import com.example.healthysmile.R;
import com.example.healthysmile.ConexionFirebaseDB;
import com.example.healthysmile.IconMethods;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Map;

public class LogIn extends AppCompatActivity {

    private EditText fragLogInInputCorreoUsuario;
    private EditText fragLogInInputContrasenaUsuario;
    private Button fragLogInBtnIniciarSesion;
    private ConexionFirebaseDB dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_in);

        fragLogInInputCorreoUsuario = findViewById(R.id.fragLogInInputCorreoUsuario);
        fragLogInInputContrasenaUsuario = findViewById(R.id.fragLogInInputContrasenaUsuario);
        fragLogInBtnIniciarSesion = findViewById(R.id.fragLogInBtnIniciarSesion);

        dbHelper = new ConexionFirebaseDB();
        fragLogInBtnIniciarSesion.setOnClickListener(v -> iniciarSesion());
        IconMethods iconito = new IconMethods();
        // Configura la visibilidad de la contraseña
        iconito.setupPasswordVisibility(fragLogInInputContrasenaUsuario);

        fragLogInBtnIniciarSesion.setOnClickListener(v -> iniciarSesion());
    }

    private void iniciarSesion() {
        String correoUsuario = fragLogInInputCorreoUsuario.getText().toString().trim(); // Usar correo en lugar de nombreUsuario
        String contrasenaUsuario = fragLogInInputContrasenaUsuario.getText().toString().trim();

        if (correoUsuario.isEmpty() || contrasenaUsuario.isEmpty()) {
            Toast.makeText(LogIn.this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificar credenciales utilizando la clase ConexionFirebaseDB
        dbHelper.verificarCredenciales(correoUsuario, contrasenaUsuario, (callback) -> {
            // Depuración: Verificar si la credencial es válida
            if (callback.isValid) {
                // Obtener el documento del usuario
                DocumentSnapshot usuario = callback.usuario;
                if (usuario != null) {
                    // Verifica si el tipo de usuario es "Paciente"
                    if ("Paciente".equals(usuario.getString("tipoUser"))) {
                        // Extraer los datos del usuario
                        String correoUser = usuario.getString("correoUser");
                        String nomUser = usuario.getString("nomUser");
                        long nivelPermisos = ((Long) usuario.get("nivelPermisos")).intValue(); // Asegurarse que se convierte correctamente a int
                        long idUsuario = ((Long) usuario.get("idUsuario")).intValue(); // Obtener el idUsuario

                        // Crear el Intent y enviar los datos con putExtra
                        Intent intentIrHome = new Intent(LogIn.this, NavigationDrawerFragments.class);
                        intentIrHome.putExtra("correoUser", correoUser);  // Ya está correcto
                        intentIrHome.putExtra("nomUser", nomUser);        // Ya está correcto
                        intentIrHome.putExtra("tipoUsuario", "Paciente");  // Aquí estamos enviando tipoUsuario como "Paciente"
                        intentIrHome.putExtra("nivelPermisos", nivelPermisos); // Ya está correcto
                        intentIrHome.putExtra("idUsuario", idUsuario);  // Enviamos el idUsuario
                        startActivity(intentIrHome);
                    } else {
                        if("Especialista".equals(usuario.getString("tipoUser"))){
                            // Acceder a los datos generales del usuario
                            String correoUser = usuario.getString("correoUser");
                            String nomUser = usuario.getString("nomUser");
                            long nivelPermisos = ((Long) usuario.get("nivelPermisos")).intValue();
                            long idUsuario = ((Long) usuario.get("idUsuario")).intValue();

                            // Acceder a los datos específicos del especialista
                            Map<String, Object> especialista = (Map<String, Object>) usuario.get("Especialista");  // Obtener el Map del campo 'especialista'
                            // Si el campo 'especialista' tiene subcampos, los puedes acceder de la siguiente manera:
                            long idEspecialista = ((Long) especialista.get("idEspecialista")).intValue();
                            String descripcion = (String) especialista.get("descripcion");
                            String cedulaProfesional = (String) especialista.get("cedulaProfesional"); // Ejemplo de subcampo
                            String especialidad = (String) especialista.get("especialidad");  // Otro ejemplo de subcampo

                            // Crear el Intent y enviar los datos con putExtra
                            Intent intentIrHome = new Intent(LogIn.this, NavigationDrawerFragments.class);
                            intentIrHome.putExtra("correoUser", correoUser);
                            intentIrHome.putExtra("nomUser", nomUser);
                            intentIrHome.putExtra("tipoUsuario", "Especialista");
                            intentIrHome.putExtra("nivelPermisos", nivelPermisos);
                            intentIrHome.putExtra("idUsuario", idUsuario);
                            intentIrHome.putExtra("idEspecialista",idEspecialista);
                            intentIrHome.putExtra("descripcion",descripcion);
                            intentIrHome.putExtra("cedulaProfesional", cedulaProfesional);
                            intentIrHome.putExtra("especialidad", especialidad);
                            startActivity(intentIrHome);
                        }
                    }
                } else {
                    Toast.makeText(LogIn.this, "Documento no encontrado", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Credenciales incorrectas
                Toast.makeText(LogIn.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
