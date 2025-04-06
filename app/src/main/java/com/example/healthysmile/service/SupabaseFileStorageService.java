package com.example.healthysmile.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;  // Asegúrate de importar la clase correcta
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.healthysmile.controller.ArchivoExisteListener;
import com.example.healthysmile.controller.extraAndroid.CargarFotoResponseListener;
import com.example.healthysmile.controller.extraAndroid.SubirArchivoResponseListener;

import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SupabaseFileStorageService {
    public static final String SUPABASE_URL = "https://numnevpjhspclytqdrrs.supabase.co";
    public static final String SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im51bW5ldnBqaHNwY2x5dHFkcnJzIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDE0OTEzMzYsImV4cCI6MjA1NzA2NzMzNn0.ApL13cK5F-5obMcBctjfHo5Up1F6yllXsOvwH6bVrWQ";
    public static final String BUCKET_NAME = "archivos";
    public static final  String TAG = "Subir fotoPerfil";

    private final OkHttpClient client = new OkHttpClient();

    public void subirArchivoSupabase(Context context, final File file, String tipoArchivo, final SubirArchivoResponseListener callback) {
        // Obtén el nombre de usuario de las preferencias
        SharedPreferences prefs = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        String nombreUsuario = prefs.getString("nombreUsuario", "usuario_desconocido");
        Log.d(TAG, "Nombre de usuario obtenido: " + nombreUsuario);

        // Obtén la extensión del archivo y crea el nombre final según el tipo
        String extension = file.getName().substring(file.getName().lastIndexOf('.'));
        String nombreFinal;

        Log.d(TAG, "Extensión del archivo: " + extension);

        if ("fotoPerfil".equals(tipoArchivo)) {
            nombreFinal = "fotoPerfil_" + nombreUsuario + extension;
            Log.d(TAG, "Archivo de tipo fotoPerfil, nombre final: " + nombreFinal);
        } else if ("fotoProducto".equals(tipoArchivo)) {
            nombreFinal = "fotoProducto_"+ file.getName() + extension;
            Log.d(TAG, "Archivo de tipo fotoProducto, nombre final: " + nombreFinal);
        } else {
            nombreFinal = file.getName();
            Log.d(TAG, "Archivo de tipo desconocido, nombre final: " + nombreFinal);
        }

        // Verifica si el archivo ya existe
        verificarArchivoExiste(nombreFinal, new ArchivoExisteListener() {
            @Override
            public void onResultado(boolean existe) {
                Log.d(TAG, "Resultado de verificación de existencia: " + (existe ? "El archivo existe" : "El archivo no existe"));

                String uploadUrl = SUPABASE_URL + "/storage/v1/object/" + BUCKET_NAME + "/" + nombreFinal;
                Log.d(TAG, "URL de carga: " + uploadUrl);

                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("file", nombreFinal,
                                RequestBody.create(file, MediaType.parse("application/octet-stream")))
                        .build();

                Request.Builder requestBuilder = new Request.Builder()
                        .url(uploadUrl)
                        .header("Authorization", "Bearer " + SUPABASE_KEY)
                        .header("apikey", SUPABASE_KEY);

                // Si el archivo existe, utilizamos PUT para actualizarlo
                if (existe) {
                    Log.d(TAG, "Usando PUT para actualizar archivo");
                    requestBuilder.put(requestBody);
                } else {
                    Log.d(TAG, "Usando POST para subir archivo");
                    requestBuilder.post(requestBody);
                }

                Request request = requestBuilder.build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e(TAG, "Error al subir archivo: " + e.getMessage());
                        callback.onError("Error al subir archivo: " + e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.d(TAG, "Respuesta al subir archivo: " + response.code());

                        if (!response.isSuccessful()) {
                            // Si la respuesta no es exitosa, obtenemos el cuerpo de la respuesta (posiblemente detalles del error)
                            String errorMessage = response.body() != null ? response.body().string() : "Error desconocido";
                            Log.e(TAG, "Error al subir archivo: " + response.code() + " - " + errorMessage);
                            callback.onError("Error al subir archivo: " + response.code() + " - " + errorMessage);
                        } else {
                            // Si la respuesta es exitosa, procesamos la URL de la foto
                            String fotoUrl = response.body().string(); // Suponiendo que la URL se devuelve en el cuerpo de la respuesta
                            Log.d(TAG, "Archivo subido exitosamente, URL: " + fotoUrl);
                            callback.onSubidaExitosa(fotoUrl);
                        }
                    }

                });
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "Error en la verificación de existencia del archivo: " + error);
                callback.onError(error);
            }
        });
    }

    public void subirArchivoEnSegundoPlano(final Context context, final File file, final String tipoArchivo, final SubirArchivoResponseListener callback) {
        Log.d(TAG, "Iniciando subida en segundo plano para archivo: " + file.getName());

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "Ejecutando tarea en segundo plano...");
                subirArchivoSupabase(context, file, tipoArchivo, callback);
            }
        });
    }

    private void verificarArchivoExiste(String fileName, ArchivoExisteListener listener) {
        // Cambiar la URL para no usar '/public/' ya que estás autenticando la solicitud
        String url = SUPABASE_URL + "/storage/v1/object/" + BUCKET_NAME + "/" + fileName;
        Log.d(TAG, "Verificando si existe: " + url);

        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + SUPABASE_KEY)
                .header("apikey", SUPABASE_KEY)
                .head()  // Usamos el método HEAD para verificar solo la existencia
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Fallo al verificar existencia: " + e.getMessage());
                listener.onError("Error al verificar existencia del archivo: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "Respuesta verificación: " + response.code());

                // Revisar si la respuesta es 200 (archivo encontrado) o 404 (archivo no encontrado)
                if (response.code() == 200) {
                    // El archivo existe
                    Log.d(TAG, "El archivo existe");
                    listener.onResultado(true);
                } else if (response.code() == 400) {
                    // El archivo no existe
                    Log.d(TAG, "El archivo no existe");
                    listener.onResultado(false);
                } else {
                    // Para cualquier otro código, mostramos el error completo
                    String errorBody = response.body() != null ? response.body().string() : "No details available";
                    Log.e(TAG, "Error al verificar existencia del archivo: " + response.code() + " - " + response.message() + " - " + errorBody);
                    listener.onError("Error al verificar existencia del archivo: " + response.code() + " - " + response.message() + " - " + errorBody);
                }
            }
        });
    }

    // Método para descargar la imagen desde Supabase y devolverla como Drawable
    public Drawable cargarFoto(Context context, String fotoUrl) throws IOException {
        // Usamos directamente la URL completa que ya contiene el prefijo correcto
        String downloadUrl = fotoUrl;

        // Crear una solicitud para descargar la imagen
        Request request = new Request.Builder()
                .url(downloadUrl)
                .header("Authorization", "Bearer " + SUPABASE_KEY)
                .header("apikey", SUPABASE_KEY)
                .build();

        // Hacer la solicitud
        Response response = client.newCall(request).execute();
        if (response.isSuccessful() && response.body() != null) {
            InputStream inputStream = response.body().byteStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            return Drawable.createFromStream(inputStream, "src");
        } else {
            throw new IOException("Error al descargar la imagen");
        }
    }

    // Método para cargar foto desde Supabase
    public void cargarFotoEnSegundoPlano(final Context context, final String fotoUrl, final CargarFotoResponseListener callback) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // Realizamos la descarga de la imagen en segundo plano
                    Drawable drawable = cargarFoto(context, fotoUrl);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            // Llamamos al callback con el drawable
                            callback.onFotoCargada(drawable);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
