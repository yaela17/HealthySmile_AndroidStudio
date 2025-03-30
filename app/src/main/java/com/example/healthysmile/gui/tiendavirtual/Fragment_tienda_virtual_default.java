package com.example.healthysmile.gui.tiendavirtual;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthysmile.R;
import com.example.healthysmile.controller.ObtenerProductosResponseListener;
import com.example.healthysmile.gui.extraAndroid.adaptadores.AdaptadorRVProductos;
import com.example.healthysmile.service.ObtenerProductosService;

import java.util.List;

public class Fragment_tienda_virtual_default extends Fragment {

    RecyclerView tiendaProductosRV;
    ObtenerProductosService obtenerProductosService;
    AdaptadorRVProductos adaptador;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tienda_virtual_default, container, false);

        // Inicializar el RecyclerView
        tiendaProductosRV = view.findViewById(R.id.tienda_productos_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        tiendaProductosRV.setLayoutManager(layoutManager);

        obtenerProductosService = new ObtenerProductosService(requireContext());

        // Llamada al método que carga los productos
        cargarProductos();

        return view;
    }

    // Método que obtiene los productos
    private void cargarProductos() {
        obtenerProductosService.obtenerProductos(new ObtenerProductosResponseListener() {
            @Override
            public void onObtencionExitosa(List<Long> idsProducto, List<String> nombresProd, List<Long> numerosProd,
                                           List<String> descripcionesProd, List<Double> costosProd,
                                           List<Integer> compras, List<String> urlsImagen, List<Boolean> disponibles) {

                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        // Adaptador para el RecyclerView
                        adaptador = new AdaptadorRVProductos(requireContext(), idsProducto, nombresProd, numerosProd,
                                descripcionesProd, costosProd, urlsImagen, disponibles, compras);
                        tiendaProductosRV.setAdapter(adaptador);
                    });
                }
            }

            @Override
            public void onError(String mensaje) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() ->
                            Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show()
                    );
                }
            }
        });
    }
}
