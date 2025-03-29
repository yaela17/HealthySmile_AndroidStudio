package com.example.healthysmile.service;

import static com.example.healthysmile.service.SupabaseFileStorageService.SUPABASE_URL;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;
import com.example.healthysmile.controller.SubirArchivoResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;


public class ActualizarFotoProductoService {

    private Context context;

    public ActualizarFotoProductoService(Context context) {
        this.context = context;
    }

    public void actualizarFotoProducto(final File file) {
        SupabaseFileStorageService supabaseFileStorageService = new SupabaseFileStorageService();

        supabaseFileStorageService.subirArchivoEnSegundoPlano(context, file, new SubirArchivoResponseListener() {
            @Override
            public void onSubidaExitosa(String fotoUrl) {
                Log.d("FotoProducto", "Archivo subido exitosamente, URL: " + fotoUrl);
                manejarResultadoSubida(fotoUrl);
            }
            @Override
            public void onError(String error) {
                Log.e("FotoProducto", "Error al subir archivo: " + error);
                Toast.makeText(context, "Error al subir la foto del producto", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void manejarResultadoSubida(String fotoUrl) {
        if (fotoUrl.equals("Error al subir archivo")) {
            Log.e("FotoProducto", "Error al subir la foto");
            Toast.makeText(context, "Error al subir la foto del producto", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            JSONObject jsonObject = new JSONObject(fotoUrl);
            String key = jsonObject.getString("Key");

            String fotoUrlCompleta = SUPABASE_URL + "/storage/v1/object/public/" + key;

            SharedPreferences prefs = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("urlFotoProducto", fotoUrlCompleta);
            editor.apply();
            Log.d("FotoProducto", "URL de la foto del producto guardada correctamente: " + fotoUrlCompleta);
        } catch (JSONException e) {
            Log.e("FotoProducto", "Error al parsear la URL JSON: " + e.getMessage());
        }
    }
}
