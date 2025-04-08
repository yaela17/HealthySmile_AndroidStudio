package com.example.healthysmile.gui.tiendavirtual;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.healthysmile.R;
import com.example.healthysmile.gui.extraAndroid.adaptadores.CarritoAdapter;
import com.example.healthysmile.gui.extraAndroid.dialog.MetodoPagoDialogFragment;
import com.example.healthysmile.model.ItemCarrito;
import com.example.healthysmile.service.tiendaVirtual.ObtenerCarritoCompraService;
import com.example.healthysmile.controller.tiendaVirtual.ObtenerCarritoCompraResponseListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Fragment_tienda_virtual_carrito_compra extends Fragment {

    RecyclerView carritoCompraRV;
    Button btnComprarCarrito;
    List<ItemCarrito> carrito;
    double montoTotal;
    ObtenerCarritoCompraService obtenerCarritoCompraService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tienda_virtual_carrito_compra, container, false);
        carritoCompraRV = view.findViewById(R.id.tienda_virtual_carrito_comprar_RV_mostrar);
        btnComprarCarrito = view.findViewById(R.id.tienda_vitual_carrito_btn_comprar_carrito);

        btnComprarCarrito.setOnClickListener(v -> comprarCarrito());
        carritoCompraRV.setLayoutManager(new LinearLayoutManager(getContext()));
        obtenerCarritoCompraService = new ObtenerCarritoCompraService(getContext());
        obtenerCarritoCompra();
        return view;
    }

    private void obtenerCarritoCompra() {
        carrito = new ArrayList<>();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("AppPrefs", getContext().MODE_PRIVATE);
        long idCarritoCompra = sharedPreferences.getLong("idCarritoCompra", -1);
        long idUsuario = sharedPreferences.getLong("idUsuario", -1);

        obtenerCarritoCompraService.obtenerCarritoCompra(idCarritoCompra, idUsuario, new ObtenerCarritoCompraResponseListener() {
            @Override
            public void onObtencionExitosa(List<Long> idsProducto, List<String> nombresProd, List<Long> numerosProd,
                                           List<String> descripcionesProd, List<Double> costosProd, List<String> urlsImagen,
                                           List<Boolean> disponibles, List<Integer> numerosProdDisponibles,
                                           List<Integer> numProdTot, List<Double> costTot) {

                CarritoAdapter carritoAdapter = new CarritoAdapter(getContext(), idsProducto, nombresProd, numerosProd,
                        descripcionesProd, costosProd, urlsImagen, disponibles,
                        numerosProdDisponibles, numProdTot, costTot);
                // Crear objetos ItemCarrito y agregarlos a la lista carrito
                for (int i = 0; i < nombresProd.size(); i++) {
                    ItemCarrito itemCarrito = new ItemCarrito(
                            numerosProd.get(i).intValue(),   // Convierte el Long a int (si es necesario)
                            nombresProd.get(i),         // Obtén el nombre del producto
                            costosProd.get(i)           // Obtén el costo del producto
                    );
                    carrito.add(itemCarrito);
                }
                montoTotal = costTot.get(0);
                carritoCompraRV.setAdapter(carritoAdapter);
            }
            @Override
            public void onError(String error) {
                // Manejo de error
            }
        });
    }

    private void comprarCarrito(){
        MetodoPagoDialogFragment dialog = new MetodoPagoDialogFragment(getContext(),montoTotal,carrito);
        dialog.show(requireActivity().getSupportFragmentManager(), "MetodoPagoDialog");
    }

}
