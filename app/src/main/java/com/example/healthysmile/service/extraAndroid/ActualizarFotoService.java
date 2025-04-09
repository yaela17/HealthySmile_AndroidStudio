package com.example.healthysmile.service.extraAndroid;

import static com.example.healthysmile.service.SupabaseFileStorageService.SUPABASE_URL;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.example.healthysmile.controller.ApiNodeMySqlRespuesta;
import com.example.healthysmile.model.requests.ActualizarFotoPerfilRequest;
import com.example.healthysmile.repository.NodeApiRetrofitClient;
import com.example.healthysmile.service.ApiNodeMySqlService;
import com.example.healthysmile.service.SupabaseFileStorageService;
import com.example.healthysmile.controller.extraAndroid.SubirArchivoResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActualizarFotoService {

    private Context context;

    public ActualizarFotoService(Context context) {
        this.context = context;
    }


    public void actualizarFoto(final File file) {
        SupabaseFileStorageService supabaseFileStorageService = new SupabaseFileStorageService();

        // Usamos el método asincrónico para subir el archivo
        supabaseFileStorageService.subirArchivoEnSegundoPlano(context, file,"fotoPerfil",new SubirArchivoResponseListener() {
            @Override
            public void onSubidaExitosa(String fotoUrl) {
                // Una vez que el archivo ha sido subido, se obtiene la URL
                Log.d("FotoPerfil", "Archivo subido exitosamente, URL: " + fotoUrl);
                manejarResultadoSubida(fotoUrl);
            }

            @Override
            public void onError(String error) {
                // Si ocurre un error en la subida
                Log.e("FotoPerfil", "Error al subir archivo: " + error);
            }
        });
    }

    private void manejarResultadoSubida(String fotoUrl) {
        if (fotoUrl.equals("Error al subir archivo")) {
            Log.e("FotoPerfil", "Error al subir la foto");
            Toast.makeText(context, "Error al subir la foto", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Paso 1: Parsear la URL JSON antes de guardar
            JSONObject jsonObject = new JSONObject(fotoUrl);
            String key = jsonObject.getString("Key"); // Extraer la clave del JSON

            // Paso 2: Construir la URL de descarga correctamente
            // Aquí no agregamos "archivos/" otra vez porque Supabase ya lo maneja automáticamente
            String fotoUrlCompleta = SUPABASE_URL + "/storage/v1/object/public/"  + key;

            // Paso 3: Guardar la URL completa en SharedPreferences
            SharedPreferences prefs = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("fotoUsuario", fotoUrlCompleta);
            editor.apply(); // Guardar foto URL

            // Paso 4: Obtener el idUsuario desde SharedPreferences
            long idUsuario = prefs.getLong("idUsuario", -1); // Recuperar idUsuario

            if (idUsuario == -1) {
                Log.e("FotoPerfil", "No se encontró el idUsuario en SharedPreferences");
                return;
            }

            // Paso 5: Crear parámetros para la actualización en la base de datos
            ActualizarFotoPerfilRequest actualizarFotoPerfilRequest = new ActualizarFotoPerfilRequest(fotoUrlCompleta,(int)idUsuario);

            // Paso 6: Llamar al servicio Retrofit para actualizar la foto de perfil en la base de datos
            ApiNodeMySqlService apiService = NodeApiRetrofitClient.getApiService();
            Call<ApiNodeMySqlRespuesta> call = apiService.actualizarFotoPerfil(actualizarFotoPerfilRequest);

            call.enqueue(new Callback<ApiNodeMySqlRespuesta>() {
                @Override
                public void onResponse(Call<ApiNodeMySqlRespuesta> call, Response<ApiNodeMySqlRespuesta> response) {
                    if (response.isSuccessful()) {
                        ApiNodeMySqlRespuesta respuesta = response.body();
                        if (respuesta != null) {
                            Log.d("FotoPerfil", "Foto de perfil actualizada correctamente: " + respuesta.toString());
                        } else {
                            Log.e("FotoPerfil", "Respuesta vacía del servidor");
                        }
                    } else {
                        try {
                            // Leer el cuerpo de la respuesta de error
                            String errorBody = response.errorBody() != null ? response.errorBody().string() : "Error desconocido";
                            Log.e("FotoPerfil", "Error al actualizar la foto de perfil: " + errorBody);
                        } catch (IOException e) {
                            Log.e("FotoPerfil", "Error al leer el cuerpo de error: " + e.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<ApiNodeMySqlRespuesta> call, Throwable t) {
                    Log.e("FotoPerfil", "Error de conexión: " + t.getMessage());
                }
            });

        } catch (JSONException e) {
            Log.e("FotoPerfil", "Error al parsear la URL JSON: " + e.getMessage());
        }
    }
}
