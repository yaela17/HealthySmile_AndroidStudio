package com.example.healthysmile.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthysmile.NavigationDrawerFragments;
import com.example.healthysmile.R;
import com.example.healthysmile.ConexionFirebaseDB;
import com.google.firebase.firestore.DocumentSnapshot;

public class LogIn extends AppCompatActivity {

    private EditText fragLogInInputNombreUsuario;
    private EditText fragLogInInputContrasenaUsuario;
    private Button fragLogInBtnIniciarSesion;
    private ConexionFirebaseDB dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        fragLogInInputNombreUsuario = findViewById(R.id.fragLogInInputNombreUsuario);
        fragLogInInputContrasenaUsuario = findViewById(R.id.fragLogInInputContrasenaUsuario);
        fragLogInBtnIniciarSesion = findViewById(R.id.fragLogInBtnIniciarSesion);

        dbHelper = new ConexionFirebaseDB();  // Inicializa la clase de conexión

        fragLogInBtnIniciarSesion.setOnClickListener(v -> iniciarSesion());
    }

    private void iniciarSesion() {
        String nombreUsuario = fragLogInInputNombreUsuario.getText().toString().trim();
        String contrasenaUsuario = fragLogInInputContrasenaUsuario.getText().toString().trim();

        // Validación de los campos
        if (nombreUsuario.isEmpty() || contrasenaUsuario.isEmpty()) {
            Toast.makeText(LogIn.this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Depuración: Mostrar datos antes de la verificación
        Toast.makeText(LogIn.this, "Nombre de usuario: " + nombreUsuario + ", Contraseña: " + contrasenaUsuario, Toast.LENGTH_SHORT).show();

        // Verificar credenciales utilizando la clase ConexionFirebaseDB
        dbHelper.verificarCredenciales(nombreUsuario, contrasenaUsuario, (callback) -> {
            // Depuración: Verificar si la credencial es válida
            if (callback.isValid) {
                Toast.makeText(LogIn.this, "Credenciales válidas", Toast.LENGTH_SHORT).show();

                // Obtener el documento del usuario
                DocumentSnapshot usuario = callback.usuario;
                if (usuario != null) {
                    Toast.makeText(LogIn.this, "Documento recuperado", Toast.LENGTH_SHORT).show();

                    // Verifica si el tipo de usuario es "Paciente"
                    if ("Paciente".equals(usuario.getString("tipoUser"))) {
                        // Extraer los datos del usuario
                        String correoUser = usuario.getString("correoUser");
                        String nomUser = usuario.getString("nomUser");
                        int nivelPermisos = ((Long) usuario.get("nivelPermisos")).intValue(); // Asegurarse que se convierte correctamente a int

                        // Depuración: Mostrar datos extraídos
                        Toast.makeText(LogIn.this, "Correo: " + correoUser + ", Nombre: " + nomUser + ", Nivel de permisos: " + nivelPermisos, Toast.LENGTH_SHORT).show();
                        // Crear el Intent y enviar los datos con putExtra
                        Intent intentIrHome = new Intent(LogIn.this, NavigationDrawerFragments.class);
                        intentIrHome.putExtra("correoUser", correoUser);  // Ya está correcto
                        intentIrHome.putExtra("nomUser", nomUser);        // Ya está correcto
                        intentIrHome.putExtra("tipoUsuario", "Paciente");  // Aquí estamos enviando tipoUsuario como "Paciente"
                        intentIrHome.putExtra("nivelPermisos", nivelPermisos); // Ya está correcto
                        startActivity(intentIrHome);
                    } else {
                        Toast.makeText(LogIn.this, "El tipo de usuario no es Paciente", Toast.LENGTH_SHORT).show();
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
