package com.example.healthysmile.gui.modelos3d;

import android.content.Context;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;

import fi.iki.elonen.NanoHTTPD;

public class SimpleHttpServer extends NanoHTTPD {

    private Context context;

    public SimpleHttpServer(Context context) {
        super(8081); // Cambié el puerto a 8081 para evitar conflictos con otros servicios
        this.context = context;  // Guardamos el contexto
    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri(); // Ruta solicitada por el navegador
        Log.d("HttpServer", "Solicitada la URI: " + uri);  // Depuración de URI

        // Servir los archivos de la carpeta assets a través del puerto 8081
        if (uri.equals("/modelo.glb")) {
            Log.d("HttpServer", "Solicitado modelo.glb");
            InputStream modeloStream = getAssetFile("modelo.glb"); // Ruta directa a los archivos
            if (modeloStream != null) {
                Log.d("HttpServer", "Archivo modelo.glb encontrado y enviado");
                return newChunkedResponse(Response.Status.OK, "model/gltf-binary", modeloStream);
            } else {
                Log.e("HttpServer", "Archivo modelo.glb no encontrado");
                return newFixedLengthResponse(Response.Status.NOT_FOUND, "text/plain", "File not found");
            }
        } else if (uri.equals("/fondo_3d.png")) {
            Log.d("HttpServer", "Solicitado fondo_3d.png");
            InputStream fondoStream = getAssetFile("fondo_3d.png"); // Ruta directa a los archivos
            if (fondoStream != null) {
                Log.d("HttpServer", "Archivo fondo_3d.png encontrado y enviado");
                return newChunkedResponse(Response.Status.OK, "image/png", fondoStream);
            } else {
                Log.e("HttpServer", "Archivo fondo_3d.png no encontrado");
                return newFixedLengthResponse(Response.Status.NOT_FOUND, "text/plain", "File not found");
            }
        } else {
            Log.d("HttpServer", "Ruta no encontrada: " + uri);
            return newFixedLengthResponse(Response.Status.NOT_FOUND, "text/plain", "File not found");
        }
    }

    // Método para obtener el archivo desde los assets usando el contexto
    private InputStream getAssetFile(String filePath) {
        try {
            Log.d("HttpServer", "Buscando archivo en assets: " + filePath);  // Depuración al buscar archivo en assets
            return context.getAssets().open(filePath);  // Usamos el contexto para acceder a los assets
        } catch (IOException e) {
            Log.e("HttpServer", "Error al abrir archivo: " + e.getMessage(), e);  // Depuración en caso de error
            return null;
        }
    }

    // Método para iniciar el servidor
    public void startServer() {
        try {
            Log.d("HttpServer", "Iniciando servidor...");
            start();  // Inicia el servidor
            Log.d("HttpServer", "Servidor iniciado en puerto 8081");
        } catch (IOException e) {
            Log.e("HttpServer", "Error al iniciar servidor: " + e.getMessage(), e);  // Depuración al manejar error
        }
    }

    // Método para detener el servidor
    public void stopServer() {
        Log.d("HttpServer", "Deteniendo servidor...");
        stop();  // Detiene el servidor
        Log.d("HttpServer", "Servidor detenido");
    }
}
