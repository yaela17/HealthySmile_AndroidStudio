package com.example.healthysmile.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.healthysmile.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class ImageUtils {

    // Convertir URI a archivo temporal
    public File convertUriToFile(Context context, Uri uri) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            File tempFile = new File(context.getCacheDir(), getFileName(context, uri));
            FileOutputStream outputStream = new FileOutputStream(tempFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.close();
            inputStream.close();
            return tempFile;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Obtener el nombre del archivo desde la URI
    @SuppressLint("Range")
    public String getFileName(Context context, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = context.getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public void cargarImagenConGlide(Context contexto, String urlImagen, ImageView imageView) {
        if (urlImagen != null && !urlImagen.isEmpty()) {
            Log.d("CargarImagen", "URL de la imagen: " + urlImagen); // Verificar la URL

            Glide.with(contexto)
                    .load(urlImagen)
                    .placeholder(R.drawable.foto_a_seleccionar) // Imagen predeterminada mientras se carga
                    .error(R.drawable.foto_a_seleccionar) // Imagen predeterminada si hay un error
                    .into(imageView);
        } else {
            Log.d("CargarImagen", "La URL de la imagen está vacía o nula.");
            imageView.setImageResource(R.drawable.foto_a_seleccionar); // Imagen por defecto
        }
    }
}
