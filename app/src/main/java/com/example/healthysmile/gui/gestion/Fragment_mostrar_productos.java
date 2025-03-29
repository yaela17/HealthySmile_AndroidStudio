package com.example.healthysmile.gui.gestion;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.healthysmile.R;
import com.example.healthysmile.gui.extraAndroid.adaptadores.AdaptadorLVProductos;
import com.example.healthysmile.model.entities.Producto;
import com.example.healthysmile.service.ObtenerProductosService;
import com.example.healthysmile.controller.ObtenerProductosResponseListener;
import com.example.healthysmile.utils.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.List;

public class Fragment_mostrar_productos extends Fragment implements AdapterView.OnItemClickListener {

    ListView productosLV, productosNoDisponiblesLV;
    ObtenerProductosService obtenerProductosService;
    SharedPreferencesHelper sharedPreferencesHelper;
    boolean eliminar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mostrar_productos, container, false);

        productosLV = view.findViewById(R.id.mostrar_productos_disponibles_administrador_list_view);
        productosNoDisponiblesLV = view.findViewById(R.id.mostrar_productos_no_disponibles_administrador_list_view);
        obtenerProductosService = new ObtenerProductosService(requireContext());
        sharedPreferencesHelper = new SharedPreferencesHelper(getContext());

        eliminar = sharedPreferencesHelper.obtenerAccionSeleccionada();

        productosLV.setOnItemClickListener(this);
        productosNoDisponiblesLV.setOnItemClickListener(this);

        cargarProductos();

        return view;
    }

    private void cargarProductos() {
        obtenerProductosService.obtenerProductos(new ObtenerProductosResponseListener() {
            @Override
            public void onObtencionExitosa(List<Long> idsProducto, List<String> nombresProd, List<Long> numerosProd,
                                           List<String> descripcionesProd, List<Double> costosProd,
                                           List<Integer> compras, List<String> urlsImagen, List<Boolean> disponibles) {
                // Crear nuevas listas para los productos disponibles
                List<Long> idsProductoDisponibles = new ArrayList<>();
                List<String> nombresProdDisponibles = new ArrayList<>();
                List<Long> numerosProdDisponibles = new ArrayList<>();
                List<String> descripcionesProdDisponibles = new ArrayList<>();
                List<Double> costosProdDisponibles = new ArrayList<>();
                List<String> urlsImagenDisponibles = new ArrayList<>();
                List<Boolean> sonDisponible = new ArrayList<>();

                List<Long> idsProductoNODisponibles = new ArrayList<>();
                List<String> nombresProdNODisponibles = new ArrayList<>();
                List<Long> numerosProdNODisponibles = new ArrayList<>();
                List<String> descripcionesProdNODisponibles = new ArrayList<>();
                List<Double> costosProdNODisponibles = new ArrayList<>();
                List<String> urlsImagenNODisponibles = new ArrayList<>();
                List<Boolean> noSonDisponibles = new ArrayList<>();

                // Filtrar los productos disponibles
                for (int i = 0; i < disponibles.size(); i++) {
                    if (disponibles.get(i)) { // Si el producto está disponible
                        idsProductoDisponibles.add(idsProducto.get(i));
                        nombresProdDisponibles.add(nombresProd.get(i));
                        numerosProdDisponibles.add(numerosProd.get(i));
                        descripcionesProdDisponibles.add(descripcionesProd.get(i));
                        costosProdDisponibles.add(costosProd.get(i));
                        urlsImagenDisponibles.add(urlsImagen.get(i));
                        sonDisponible.add(true);
                    }else {
                        idsProductoNODisponibles.add(idsProducto.get(i));
                        nombresProdNODisponibles.add(nombresProd.get(i));
                        numerosProdNODisponibles.add(numerosProd.get(i));
                        descripcionesProdNODisponibles.add(descripcionesProd.get(i));
                        costosProdNODisponibles.add(costosProd.get(i));
                        urlsImagenNODisponibles.add(urlsImagen.get(i));
                        noSonDisponibles.add(false);
                    }
                }
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        AdaptadorLVProductos adaptador = new AdaptadorLVProductos(
                                requireContext(), idsProductoDisponibles, nombresProdDisponibles, numerosProdDisponibles,
                                descripcionesProdDisponibles, costosProdDisponibles, urlsImagenDisponibles,disponibles, eliminar
                        );
                        AdaptadorLVProductos adaptador2 = new AdaptadorLVProductos(requireContext(),idsProductoNODisponibles, nombresProdNODisponibles,
                                numerosProdNODisponibles,descripcionesProdNODisponibles,costosProdNODisponibles,urlsImagenNODisponibles,disponibles,eliminar);
                        productosLV.setAdapter(adaptador);
                        productosNoDisponiblesLV.setAdapter(adaptador2);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Obtener el producto seleccionado
        AdaptadorLVProductos adaptador = (AdaptadorLVProductos) parent.getAdapter();
        Producto productoSeleccionado = adaptador.getItem(position);
        // Crear el nuevo fragmento y pasar los datos
        Fragment_form_agregar_producto fragmentoDetalle = new Fragment_form_agregar_producto();
        Bundle args = new Bundle();
        args.putLong("idProducto", productoSeleccionado.getIdProd());
        args.putString("nombre", productoSeleccionado.getNombreProd());
        args.putInt("numerosProd",productoSeleccionado.getNumProd());
        args.putString("descripcion", productoSeleccionado.getDescriProd());
        args.putDouble("costo", productoSeleccionado.getCostoProd());
        args.putString("imagenUrl", productoSeleccionado.getImagen());
        args.putBoolean("disponible",productoSeleccionado.isDisponible());
        fragmentoDetalle.setArguments(args);

        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof Fragment_gestion_administrador_productos) {
            ((Fragment_gestion_administrador_productos) parentFragment).cargarFragment(fragmentoDetalle);
        }
    }
}
