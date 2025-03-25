package com.example.healthysmile.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;  // Asegúrate de importar la clase correcta
import android.os.Looper;

import com.example.healthysmile.controller.CargarFotoResponseListener;
import com.example.healthysmile.controller.SubirArchivoResponseListener;

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

    private final OkHttpClient client = new OkHttpClient();

    public void subirArchivoSupabase(final File file, final SubirArchivoResponseListener callback) {
        String uploadUrl = SUPABASE_URL + "/storage/v1/object/" + BUCKET_NAME + "/" + file.getName();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(),
                        RequestBody.create(file, MediaType.parse("application/octet-stream")))
                .build();

        Request request = new Request.Builder()
                .url(uploadUrl)
                .header("Authorization", "Bearer " + SUPABASE_KEY)
                .header("apikey", SUPABASE_KEY)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError("Error al subir archivo: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    callback.onError("Error al subir archivo: " + response.code());
                } else {
                    String fotoUrl = response.body().string(); // Suponiendo que la URL se devuelve en el cuerpo de la respuesta
                    callback.onSubidaExitosa(fotoUrl);
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



    public void subirArchivoEnSegundoPlano(final Context context, final File file, final SubirArchivoResponseListener callback) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                subirArchivoSupabase(file, callback);
            }
        });
    }


}
