package com.example.healthysmile.service.tiendaVirtual;

import static com.example.healthysmile.service.URLSApisNode.URL_OBTENER_CARRITO_COMPRA;

import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.healthysmile.controller.tiendaVirtual.ObtenerCarritoCompraResponseListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class ObtenerCarritoCompraService {

    private RequestQueue requestQueue;

    public ObtenerCarritoCompraService(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    public void obtenerCarritoCompra(long idCarritoCompra, long idUsuario, final ObtenerCarritoCompraResponseListener listener) {
            JSONArray jsonRequest = new JSONArray();
            jsonRequest.put(idCarritoCompra);
            jsonRequest.put(idUsuario);

            JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, URL_OBTENER_CARRITO_COMPRA, jsonRequest,
                    response -> {
                        Log.d("ObtenerCarritoCompraService", "Respuesta recibida: " + response.toString());
                        procesarRespuesta(response, listener);
                    },
                    error -> {
                        Log.e("ObtenerCarritoCompraService", "Error en la solicitud", error);
                        listener.onError("Error en la solicitud: " + error.getMessage());
                    });

            requestQueue.add(request);
    }

    private void procesarRespuesta(JSONArray response, ObtenerCarritoCompraResponseListener listener) {
        List<Long> idsProducto = new ArrayList<>();
        List<String> nombresProd = new ArrayList<>();
        List<Long> numerosProd = new ArrayList<>();
        List<String> descripcionesProd = new ArrayList<>();
        List<Double> costosProd = new ArrayList<>();
        List<String> urlsImagen = new ArrayList<>();
        List<Boolean> disponibles = new ArrayList<>();
        List<Integer> numerosProdDisponibles = new ArrayList<>();
        List<Integer> numProdTot = new ArrayList<>();
        List<Double> costTot = new ArrayList<>();

        try {
            Log.d("ObtenerCarritoCompraService", "Procesando productos...");

            for (int i = 0; i < response.length(); i++) {
                JSONObject producto = response.getJSONObject(i);

                long idProducto = producto.optLong("idProd", 0);
                String nombre = producto.optString("nombreProd", "");
                long numero = producto.optLong("numProdCarrito", 0);
                String descripcion = producto.optString("descriProd", "");
                double costo = producto.optDouble("costoProd", 0.0);
                String urlImagen = producto.optString("imagen", "");
                boolean disponible = producto.optInt("disponible", 0) == 1;
                int numeroDisponible = producto.optInt("numProdDisponible", 0);
                int numeroTotal = producto.optInt("numProdTot", 0);
                double costoTotal = producto.optDouble("costTot", 0.0);

                Log.d("ObtenerCarritoCompraService", "Producto " + i + ": id=" + idProducto +
                        ", nombre=" + nombre + ", numero=" + numero + ", descripcion=" + descripcion +
                        ", costo=" + costo + ", urlImagen=" + urlImagen + ", disponible=" + disponible +
                        ", numDisponible=" + numeroDisponible + ", numTotal=" + numeroTotal + ", costoTotal=" + costoTotal);

                idsProducto.add(idProducto);
                nombresProd.add(nombre);
                numerosProd.add(numero);
                descripcionesProd.add(descripcion);
                costosProd.add(costo);
                urlsImagen.add(urlImagen);
                disponibles.add(disponible);
                numerosProdDisponibles.add(numeroDisponible);
                numProdTot.add(numeroTotal);
                costTot.add(costoTotal);
            }

            Log.d("ObtenerCarritoCompraService", "Productos procesados correctamente, total: " + idsProducto.size());
            listener.onObtencionExitosa(idsProducto, nombresProd, numerosProd, descripcionesProd, costosProd, urlsImagen, disponibles, numerosProdDisponibles, numProdTot, costTot);
        } catch (JSONException e) {
            Log.e("ObtenerCarritoCompraService", "Error al procesar JSON", e);
            listener.onError("Error al procesar la respuesta: " + e.getMessage());
        }
    }

}
