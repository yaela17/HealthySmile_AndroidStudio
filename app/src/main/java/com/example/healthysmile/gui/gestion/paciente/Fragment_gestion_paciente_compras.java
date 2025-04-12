package com.example.healthysmile.gui.gestion.paciente;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import com.example.healthysmile.R;
import com.example.healthysmile.gui.gestion.Fragment_gestion_init;
import com.example.healthysmile.gui.gestion.administrador.Fragment_gestion_administrador_productos;
import com.example.healthysmile.model.responses.ObtenerCarritosCompraResponse;
import com.example.healthysmile.repository.NodeApiRetrofitClient;
import com.example.healthysmile.service.ApiNodeMySqlService;
import com.example.healthysmile.gui.extraAndroid.adaptadores.AdaptadorGestionCarritosCompraRV;
import com.example.healthysmile.utils.SharedPreferencesHelper;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_gestion_paciente_compras extends Fragment {

    RecyclerView pacienteComprasRV;
    AdaptadorGestionCarritosCompraRV adaptador;
    List<ObtenerCarritosCompraResponse> listaCompras;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el layout para este fragmento
        View view = inflater.inflate(R.layout.fragment_gestion_paciente_compras, container, false);
        pacienteComprasRV = view.findViewById(R.id.gestion_paciente_compras_rv);

        // Llamamos al método para llenar el RecyclerView
        llenarRecyclerView(getContext());

        return view;
    }

    private void llenarRecyclerView(Context context) {
        // Supongamos que el idUsuario es el que tienes actualmente autenticado o es fijo
        SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(context);
        Integer idUsuario = (int) sharedPreferencesHelper.obtenerIdUsuario();

        // Llamada a la API para obtener los carritos de compra
        ApiNodeMySqlService apiService = NodeApiRetrofitClient.getApiService();
        Call<List<ObtenerCarritosCompraResponse>> call = apiService.obtenerCarritosCompra(idUsuario);

        call.enqueue(new Callback<List<ObtenerCarritosCompraResponse>>() {
            @Override
            public void onResponse(Call<List<ObtenerCarritosCompraResponse>> call, Response<List<ObtenerCarritosCompraResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Log de la respuesta completa
                    Log.d("Respuesta API", "Código de respuesta: " + response.code());
                    Log.d("Respuesta API", "Cuerpo de la respuesta: " + response.body().toString());

                    // Se obtuvo la respuesta con éxito
                    listaCompras = response.body();

                    // Log para verificar los datos obtenidos
                    Log.d("Datos obtenidos", "Lista de compras: " + listaCompras.toString());

                    // Configurar el adaptador con los datos obtenidos
                    adaptador = new AdaptadorGestionCarritosCompraRV(listaCompras, compra -> {
                        int idCompra = compra.getIdCompra();
                        Log.d("Compra seleccionada", "ID de compra: " + idCompra);

                        Fragment_gestion_paciente_compras_compra fragment = new Fragment_gestion_paciente_compras_compra();
                        Bundle bundle = new Bundle();
                        bundle.putInt("idCompra", idCompra);
                        fragment.setArguments(bundle);

                        Fragment parentFragment = getParentFragment();
                        if (parentFragment instanceof Fragment_gestion_compras_init) {
                            ((Fragment_gestion_compras_init) parentFragment).cargarFragment(fragment);
                        }
                    });

                    // Asegúrate de configurar un LayoutManager en el RecyclerView
                    if (pacienteComprasRV.getLayoutManager() == null) {
                        pacienteComprasRV.setLayoutManager(new LinearLayoutManager(getContext()));
                    }

                    // Configurar el RecyclerView con el adaptador
                    pacienteComprasRV.setAdapter(adaptador);
                } else {
                    // Log del error en la respuesta
                    Log.e("Error", "No se pudo obtener los carritos de compra");
                    Log.e("Error", "Código de respuesta: " + response.code());
                    if (response.errorBody() != null) {
                        try {
                            Log.e("Error", "Cuerpo del error: " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<List<ObtenerCarritosCompraResponse>> call, Throwable t) {
                // Manejar el error de la llamada
                Log.e("Error", "Error en la llamada a la API: " + t.getMessage());
            }
        });
    }
}
