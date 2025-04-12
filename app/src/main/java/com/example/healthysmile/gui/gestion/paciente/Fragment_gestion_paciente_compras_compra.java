package com.example.healthysmile.gui.gestion.paciente;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthysmile.R;
import com.example.healthysmile.gui.extraAndroid.adaptadores.AdaptadorGestionCarritoCompraRV;
import com.example.healthysmile.model.responses.ProductoCompra;
import com.example.healthysmile.repository.NodeApiRetrofitClient;
import com.example.healthysmile.service.ApiNodeMySqlService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_gestion_paciente_compras_compra extends Fragment {

    private RecyclerView recyclerView;
    private AdaptadorGestionCarritoCompraRV adaptador;
    private List<ProductoCompra> listaProductos;

    private int idCompraRecibida = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gestion_paciente_compras_compra, container, false);

        recyclerView = view.findViewById(R.id.gestion_paciente_compras_compra_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        listaProductos = new ArrayList<>();
        adaptador = new AdaptadorGestionCarritoCompraRV(getContext(), listaProductos);
        recyclerView.setAdapter(adaptador);

        // Obtener idCompra del Bundle
        if (getArguments() != null) {
            idCompraRecibida = getArguments().getInt("idCompra", -1);
            Log.d("Fragment", "ID de compra recibido: " + idCompraRecibida);
            if (idCompraRecibida != -1) {
                llenarCompra(idCompraRecibida);
            }
        }

        return view;
    }

    private void llenarCompra(int idCompra) {
        ApiNodeMySqlService apiService = NodeApiRetrofitClient.getApiService();
        Call<List<ProductoCompra>> call = apiService.obtenerCompra(idCompra);

        call.enqueue(new Callback<List<ProductoCompra>>() {
            @Override
            public void onResponse(@NonNull Call<List<ProductoCompra>> call, @NonNull Response<List<ProductoCompra>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaProductos.clear();
                    listaProductos.addAll(response.body());
                    adaptador.notifyDataSetChanged();
                } else {
                    Log.e("Fragment", "Error al obtener productos: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ProductoCompra>> call, @NonNull Throwable t) {
                Log.e("Fragment", "Fallo en la conexión: " + t.getMessage());
            }
        });
    }
}
