package com.example.healthysmile.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.healthysmile.R;

public class ReutilizableMethods {
    public void cargarFotoPerfil(Context context, ImageView imageView) {

        Log.d("ReutilizableMethods", "Iniciando carga de foto de perfil");

        SharedPreferences prefs = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);

        String fotoUrl = prefs.getString("fotoUsuario", null);
        String tipoUsuario = prefs.getString("tipoUsuario", "Paciente");

        Log.d("ReutilizableMethods", "fotoUrl recuperada: " + fotoUrl);
        Log.d("ReutilizableMethods", "tipoUsuario recuperado: " + tipoUsuario);

        int defaultImage = "Paciente".equals(tipoUsuario) ?
                R.drawable.default_photo_perfil_paciente :
                R.drawable.default_photo_perfil_especialista;

        Log.d("ReutilizableMethods", "Imagen por defecto seleccionada: " + defaultImage);

        Glide.get(context).clearMemory();
        Log.d("ReutilizableMethods", "Memoria de Glide limpiada.");

        if (fotoUrl != null && !fotoUrl.equals("null")) {
            Log.d("ReutilizableMethods", "Foto URL no es nula, cargando desde URL...");

            Glide.with(context)
                    .load(fotoUrl)
                    .skipMemoryCache(true)
                    .circleCrop()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(defaultImage)
                    .error(defaultImage)
                    .into(imageView);

            Log.d("ReutilizableMethods", "Carga de imagen desde URL completada.");
        } else {
            Log.d("ReutilizableMethods", "Foto URL es nula o 'null', usando imagen por defecto.");
            imageView.setImageDrawable(ContextCompat.getDrawable(context, defaultImage));
        }

        Log.d("ReutilizableMethods", "Carga de foto de perfil finalizada");
    }

}
