package com.example.healthysmile.gui.iniciarSesion.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthysmile.controller.iniciarSesion.LogInResponseListener;
import com.example.healthysmile.service.iniciarSesion.LogInService;
import com.example.healthysmile.utils.IconUtils;
import com.example.healthysmile.utils.SharedPreferencesHelper;
import com.example.healthysmile.model.entities.Especialista;
import com.example.healthysmile.gui.NavigationDrawerFragments;
import com.example.healthysmile.R;
import com.example.healthysmile.model.entities.Usuario;

public class LogIn extends AppCompatActivity {

    private EditText fragLogInInputCorreoUsuario;
    private EditText fragLogInInputContrasenaUsuario;
    private Button fragLogInBtnIniciarSesion;
    SharedPreferencesHelper manejadorShadPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_in);

        manejadorShadPreferences = new SharedPreferencesHelper(this);

        fragLogInInputCorreoUsuario = findViewById(R.id.fragLogInInputCorreoUsuario);
        fragLogInInputContrasenaUsuario = findViewById(R.id.fragLogInInputContrasenaUsuario);
        fragLogInBtnIniciarSesion = findViewById(R.id.fragLogInBtnIniciarSesion);

        fragLogInBtnIniciarSesion.setOnClickListener(v -> iniciarSesion());
        IconUtils iconito = new IconUtils();
        iconito.setupPasswordVisibility(fragLogInInputContrasenaUsuario);
    }

    private void iniciarSesion() {
        String correoUsuario = fragLogInInputCorreoUsuario.getText().toString().trim();
        String contrasenaUsuario = fragLogInInputContrasenaUsuario.getText().toString().trim();

        if (correoUsuario.isEmpty() || contrasenaUsuario.isEmpty()) {
            Toast.makeText(LogIn.this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        LogInService logInService = new LogInService(LogIn.this);

        logInService.logIn(correoUsuario, contrasenaUsuario, new LogInResponseListener() {
            @Override
            public void logInUsuario(Usuario usuario) {
                if (usuario != null) {
                        manejadorShadPreferences.guardarPaciente(usuario.getNomUser(),
                                usuario.getCorreoUser(), usuario.getTipoUser(), usuario.getNivelPermisos());
                        manejadorShadPreferences.guardarIdUsuario(usuario.getIdUsuario());
                        manejadorShadPreferences.guardarfotoUsuario(usuario.getFotoPerfil());
                        Log.d("LogIn", "Usuario Paciente autenticado");
                    Intent intentIrHome = new Intent(LogIn.this, NavigationDrawerFragments.class);
                    startActivity(intentIrHome);
                } else {
                    Toast.makeText(LogIn.this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show();
                    Log.d("LogIn", "Error: El usuario es nulo");
                }
            }

            @Override
            public void logInEspecialista(Especialista especialista) {
                if(especialista != null) {
                    manejadorShadPreferences.guardarEspecialista(especialista.getNomUser(),
                            especialista.getCorreoUser(), especialista.getTipoUser(), especialista.getNivelPermisos(),
                            especialista.getCedulaProfesional(),
                            especialista.getDescripcion(),
                            especialista.getEspecialidad());
                    manejadorShadPreferences.guardarIdUsuario(especialista.getIdUsuario());
                    manejadorShadPreferences.guardarIdEspecialista(especialista.getIdEspecialista());
                    manejadorShadPreferences.guardarfotoUsuario(especialista.getFotoPerfil());
                    Log.d("LogIn", "Usuario Especialista autenticado");
                    Intent intentIrHome = new Intent(LogIn.this, NavigationDrawerFragments.class);
                    startActivity(intentIrHome);
                }else {
                    Toast.makeText(LogIn.this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show();
                    Log.d("LogIn", "Error: El usuario es nulo");
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(LogIn.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
