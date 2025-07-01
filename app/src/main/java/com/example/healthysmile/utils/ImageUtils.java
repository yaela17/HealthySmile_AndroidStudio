package com.example.healthysmile.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.transition.Transition;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;

import com.bumptech.glide.request.target.CustomTarget;
import com.example.healthysmile.R;
import com.example.healthysmile.model.DAO.ChatContactoDao;
import com.example.healthysmile.model.LocalDB;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.annotation.Nullable;

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

    public void cargarImagenConGlide(Context contexto, String urlImagen, ImageView imageView, String tipo) {
        if (urlImagen != null && !urlImagen.isEmpty()) {
            Log.d("CargarImagen", "URL de la imagen: " + urlImagen);
            Log.d("CargarImagen", "Tipo de imagen: " + tipo);

            if (tipo.equals("Perfil")) {
                Glide.with(contexto)
                        .asBitmap()
                        .load(urlImagen)
                        .placeholder(R.drawable.foto_a_seleccionar)
                        .error(R.drawable.foto_a_seleccionar)
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @androidx.annotation.Nullable com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
                                imageView.setImageBitmap(resource);
                                Log.d("CargarImagen", "Imagen de perfil cargada correctamente como Bitmap");

                                // Extraer nombre de usuario de la URL
                                String nombreArchivoURL = urlImagen.substring(urlImagen.lastIndexOf('/') + 1); // fotoPerfil_angel.jpg
                                String nombreUsuarioExtraido = nombreArchivoURL.replace("fotoPerfil_", "").replace(".jpg", "").replace(".png", "");

                                // Crear nombre final del archivo
                                String nombreArchivo = "fotoPerfil_" + nombreUsuarioExtraido + ".png";

                                // Guardar imagen en almacenamiento externo
                                String pathGuardado = guardarImagenEnInterno(contexto, resource, nombreArchivo);

                                if (pathGuardado != null) {
                                    Log.d("GuardarImagen", "Imagen guardada en: " + pathGuardado);
                                    new Thread(() -> {
                                        ChatContactoDao dao = LocalDB.getInstance(contexto).chatContactoDao();
                                        dao.actualizarFotoPerfilPorNombre(nombreUsuarioExtraido,pathGuardado);
                                    }).start();
                                } else {
                                    Log.e("GuardarImagen", "Error al guardar la imagen");
                                }
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                                Log.d("CargarImagen", "Carga de imagen cancelada o reemplazada");
                            }
                        });
                return;
            }

            // Otros tipos de imagen
            Glide.with(contexto)
                    .load(urlImagen)
                    .placeholder(R.drawable.foto_a_seleccionar)
                    .error(R.drawable.foto_a_seleccionar)
                    .into(imageView);
        } else {
            Log.d("CargarImagen", "La URL de la imagen está vacía o nula.");
            imageView.setImageResource(R.drawable.foto_a_seleccionar);
        }
    }

    public String guardarImagenEnInterno(Context context, Bitmap bitmap, String nombreArchivo) {
        try {
            // Obtiene el directorio privado interno de tu app
            File directorio = new File(context.getFilesDir(), "HealthySmile");

            if (!directorio.exists()) {
                if (!directorio.mkdirs()) {
                    Log.e("GuardarImagenInterno", "No se pudo crear el directorio: " + directorio.getAbsolutePath());
                    return null;
                }
            }

            File archivoImagen = new File(directorio, nombreArchivo);
            FileOutputStream fos = new FileOutputStream(archivoImagen);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();

            return archivoImagen.getAbsolutePath(); // Ruta interna solo para tu app

        } catch (IOException e) {
            Log.e("GuardarImagenInterno", "Error al guardar la imagen: " + e.getMessage());
            return null;
        }
    }


    public void cargarImagenEnInterno(Context contexto, String nombreUsuario, ImageView imageView) {
        String nombreArchivo = "fotoPerfil_" + nombreUsuario + ".png";

        File archivoImagen = new File(contexto.getFilesDir() + "/HealthySmile", nombreArchivo);

        if (archivoImagen.exists()) {
            Log.d("CargarOffline", "Cargando imagen local desde interno: " + archivoImagen.getAbsolutePath());

            Glide.with(contexto)
                    .load(archivoImagen)
                    .placeholder(R.drawable.foto_a_seleccionar)
                    .error(R.drawable.foto_a_seleccionar)
                    .into(imageView);
        } else {
            Log.e("CargarOffline", "Imagen local no encontrada: " + archivoImagen.getAbsolutePath());
            imageView.setImageResource(R.drawable.foto_a_seleccionar);
        }
    }
}
