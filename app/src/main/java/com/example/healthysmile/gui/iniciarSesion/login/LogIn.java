package com.example.healthysmile.gui.iniciarSesion.login;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthysmile.utils.IconUtils;
import com.example.healthysmile.utils.SharedPreferencesHelper;
import com.example.healthysmile.model.entities.Especialista;
import com.example.healthysmile.gui.NavigationDrawerFragments;
import com.example.healthysmile.R;
import com.example.healthysmile.model.entities.Usuario;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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

        // Iniciar el AsyncTask para hacer la solicitud HTTP y obtener el Usuario
        new LogInTask().execute(correoUsuario, contrasenaUsuario);
    }

    private class LogInTask extends AsyncTask<String, Void, Usuario> {

        @Override
        protected Usuario doInBackground(String... params) {
            String correoUser = params[0];
            String contrasenaUser = params[1];
            String urlString = "http://10.0.2.2:3000/api/LogInUsuario";

            try {
                // Crear el objeto JSON con los datos
                JSONObject loginData = new JSONObject();
                loginData.put("correoUser", correoUser);
                loginData.put("contrasenaUser", contrasenaUser);

                // Realizar la solicitud HTTP
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setDoOutput(true);
                urlConnection.getOutputStream().write(loginData.toString().getBytes());

                // Leer la respuesta
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Imprimir el JSON para verificar la respuesta
                Log.d("LogInResponse", response.toString());

                // Convertir la respuesta JSON a objeto Usuario utilizando Gson
                Gson gson = new Gson();
                Usuario usuario = gson.fromJson(response.toString(), Usuario.class);

                // Si el nivel de permisos es 2, entonces lo tratamos como Especialista
                if (usuario != null && usuario.getNivelPermisos() == 2) {
                    Especialista especialista = gson.fromJson(response.toString(), Especialista.class);
                    return especialista;
                }
                return usuario;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Usuario usuario) {
            if (usuario != null) {
                // Aquí puedes manejar el objeto `usuario` como lo necesites
                Toast.makeText(LogIn.this, "Usuario logueado: " + usuario.getNomUser(), Toast.LENGTH_SHORT).show();

                // Imprimir todos los datos del usuario en la consola
                Log.d("LogIn", "Nombre: " + usuario.getNomUser());
                Log.d("LogIn", "Correo: " + usuario.getCorreoUser());
                Log.d("LogIn", "Tipo de usuario: " + (usuario instanceof Especialista ? "Especialista" : "Paciente"));
                Log.d("LogIn", "Nivel de permisos: " + usuario.getNivelPermisos());

                // Si es un Especialista, imprimir los detalles específicos de Especialista
                if (usuario instanceof Especialista) {
                    Especialista especialistaAutenticado = (Especialista) usuario;
                    Log.d("LogIn", "Cedula Profesional: " + especialistaAutenticado.getCedulaProfesional());
                    Log.d("LogIn", "Descripción: " + especialistaAutenticado.getDescripcion());
                    Log.d("LogIn", "Especialidad: " + especialistaAutenticado.getEspecialidad());
                }

                // Guardar los datos del usuario y redirigir al Home
                if (usuario instanceof Especialista) {
                    Especialista especialistaAutenticado = (Especialista) usuario;
                    manejadorShadPreferences.guardarEspecialista(especialistaAutenticado.getNomUser(),
                            especialistaAutenticado.getCorreoUser(), "Especialista", especialistaAutenticado.getNivelPermisos(),
                            especialistaAutenticado.getCedulaProfesional(),
                            especialistaAutenticado.getDescripcion(),
                            especialistaAutenticado.getEspecialidad());
                    manejadorShadPreferences.guardarIdUsuario(especialistaAutenticado.getIdUsuario());
                    manejadorShadPreferences.guardarIdEspecialista(especialistaAutenticado.getIdEspecialista());
                    Log.d("LogIn", "Usuario Especialista autenticado");
                } else {
                    manejadorShadPreferences.guardarPaciente(usuario.getNomUser(),
                            usuario.getCorreoUser(), "Paciente", usuario.getNivelPermisos());
                    manejadorShadPreferences.guardarIdUsuario(usuario.getIdUsuario());
                    Log.d("LogIn", "Usuario Paciente autenticado");
                }

                // Redirige al Home
                Intent intentIrHome = new Intent(LogIn.this, NavigationDrawerFragments.class);
                startActivity(intentIrHome);

            } else {
                Toast.makeText(LogIn.this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show();
                Log.d("LogIn", "Error: El usuario es nulo");
            }
        }
    }
}
