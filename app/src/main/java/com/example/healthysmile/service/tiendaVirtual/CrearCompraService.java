package com.example.healthysmile.service.tiendaVirtual;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.healthysmile.controller.APINodeMySqlRespuestaMensaje;
import com.example.healthysmile.model.requests.CrearCompraRequest;
import com.example.healthysmile.repository.NodeApiRetrofitClient;
import com.example.healthysmile.utils.SharedPreferencesHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearCompraService {

    private static final String TAG = "CrearCompraService";

    public void crearCompra(Context context, boolean isProducto, int cantidadProducto, String fechaCompra, String metodoPago,
                            boolean peticion, int idProducto) {

        // Obtener ID del usuario desde el helper
        SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(context);
        int idUsuario = (int) sharedPreferencesHelper.obtenerIdUsuario();

        // Obtener ID del carrito manualmente desde SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        int idCarritoCompra = (int) sharedPreferences.getLong("idCarritoCompra", -1);

        CrearCompraRequest request;

        if (isProducto) {
            // Caso solo un producto
            request = new CrearCompraRequest(peticion, metodoPago, fechaCompra, idProducto, idUsuario,cantidadProducto);
        } else {
            // Caso con carrito
            request = new CrearCompraRequest(fechaCompra, idCarritoCompra, metodoPago, peticion);
        }

        Call<APINodeMySqlRespuestaMensaje> call = NodeApiRetrofitClient
                .getApiService()
                .crearCompra(request);

        call.enqueue(new Callback<APINodeMySqlRespuestaMensaje>() {
            @Override
            public void onResponse(@NonNull Call<APINodeMySqlRespuestaMensaje> call, @NonNull Response<APINodeMySqlRespuestaMensaje> response) {
                if (response.isSuccessful()) {
                    APINodeMySqlRespuestaMensaje resultado = response.body();
                    assert resultado != null;
                    Log.d(TAG, "✅ Compra creada: " + resultado.getMensaje());

                    // Eliminar idCarritoCompra de SharedPreferences después de la compra
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove("idCarritoCompra");
                    editor.apply(); // Guardar los cambios
                    Log.d(TAG, "✅ idCarritoCompra eliminado de SharedPreferences");
                } else {
                    Log.e(TAG, "❌ Error en la respuesta del servidor: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<APINodeMySqlRespuestaMensaje> call, @NonNull Throwable t) {
                Log.e(TAG, "❌ Error al llamar al endpoint crearCompra: " + t.getMessage(), t);
            }
        });
    }
}
