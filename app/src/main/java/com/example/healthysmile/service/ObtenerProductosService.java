package com.example.healthysmile.service;

import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.healthysmile.controller.ObtenerProductosResponseListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import static com.example.healthysmile.service.URLSApisNode.URL_OBTENER_PRODUCTOS;

public class ObtenerProductosService {
    private RequestQueue requestQueue;

    public ObtenerProductosService(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    public void obtenerProductos(final ObtenerProductosResponseListener listener) {
        Log.d("ObtenerProductosService", "Realizando solicitud a: " + URL_OBTENER_PRODUCTOS);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL_OBTENER_PRODUCTOS,
                null,
                response -> {
                    Log.d("ObtenerProductosService", "Respuesta recibida: " + response.toString());
                    procesarProductos(response, listener);
                },
                error -> {
                    Log.e("ObtenerProductosService", "Error al obtener productos", error);
                    listener.onError("Error al cargar productos");
                }
        );
        requestQueue.add(jsonArrayRequest);
    }

    private void procesarProductos(JSONArray response, ObtenerProductosResponseListener listener) {
        List<Long> idsProducto = new ArrayList<>();
        List<String> nombresProd = new ArrayList<>();
        List<Long> numerosProd = new ArrayList<>();
        List<String> descripcionesProd = new ArrayList<>();
        List<Double> costosProd = new ArrayList<>();
        List<Integer> compras = new ArrayList<>();
        List<String> urlsImagen = new ArrayList<>();
        List<Boolean> disponibles = new ArrayList<>();

        try {
            Log.d("ObtenerProductosService", "Procesando productos...");
            for (int i = 0; i < response.length(); i++) {
                JSONObject producto = response.getJSONObject(i);

                long idProducto = producto.optLong("idProd", 0);
                String nombre = producto.optString("nombreProd", "");
                long numero = producto.optLong("numProd", 0);
                String descripcion = producto.optString("descriProd", "");
                double costo = producto.optDouble("costoProd", 0.0);
                int compra = producto.optInt("compras", 0);
                String urlImagen = producto.optString("imagen", "");
                int disponibleInt = producto.optInt("disponible", 0);
                boolean disponible = (disponibleInt == 1);



                Log.d("ObtenerProductosService", "Producto " + i + ": id=" + idProducto +
                        ", nombre=" + nombre + ", numero=" + numero + ", descripcion=" + descripcion +
                        ", costo=" + costo + ", compras=" + compra + ", urlImagen=" + urlImagen + " disponible" + disponible);

                idsProducto.add(idProducto);
                nombresProd.add(nombre);
                numerosProd.add(numero);
                descripcionesProd.add(descripcion);
                costosProd.add(costo);
                compras.add(compra);
                urlsImagen.add(urlImagen);
                disponibles.add(disponible);
            }

            Log.d("ObtenerProductosService", "Productos procesados correctamente, total: " + idsProducto.size());
            listener.onObtencionExitosa(idsProducto, nombresProd, numerosProd, descripcionesProd, costosProd, compras, urlsImagen,disponibles);
        } catch (JSONException e) {
            Log.e("ObtenerProductosService", "Error al procesar JSON", e);
            listener.onError("Error al procesar datos de productos.");
        }
    }
}
