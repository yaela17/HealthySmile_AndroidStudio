package com.example.healthysmile.gui.tiendavirtual;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthysmile.R;
import com.example.healthysmile.controller.tiendaVirtual.ObtenerProductosResponseListener;
import com.example.healthysmile.controller.adaptadores.OnProductoClickListener;
import com.example.healthysmile.gui.extraAndroid.adaptadores.AdaptadorRVProductosVertical;
import com.example.healthysmile.service.tiendaVirtual.ObtenerProductoPorNombreService;

import java.util.List;

public class Fragment_tienda_virtual_buscar_producto_nombre extends Fragment {

    private RecyclerView recyclerView;
    String tipoUsuario;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tienda_virtual_buscar_producto_nombre, container, false);

        // Inicializar RecyclerView
        recyclerView = view.findViewById(R.id.tienda_buscar_producto_por_nombre_RV);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        tipoUsuario = sharedPreferences.getString("tipoUsuario", "Paciente");


        // Verificar si hay un query pasado por Bundle
        Bundle args = getArguments();
        if (args != null && args.containsKey("query")) {
            String query = args.getString("query");
            llenarRecyclerView(query);
        }

        return view;
    }

    private void llenarRecyclerView(String nombreProducto) {
        if (getActivity() == null || !isAdded()) {
            return;
        }

        ObtenerProductoPorNombreService service = new ObtenerProductoPorNombreService(getActivity());
        service.obtenerProductoPorNombre(nombreProducto, new ObtenerProductosResponseListener() {
            @Override
            public void onObtencionExitosa(List<Long> idsProducto, List<String> nombresProd, List<Long> numerosProd,
                                           List<String> descripcionesProd, List<Double> costosProd,
                                           List<Integer> compras, List<String> urlsImagen, List<Boolean> disponibles) {
                if (getActivity() != null && isAdded()) {
                    AdaptadorRVProductosVertical adaptadorRVProductosVertical = new AdaptadorRVProductosVertical(
                            getActivity(), idsProducto, nombresProd, numerosProd, descripcionesProd, costosProd, urlsImagen, disponibles, compras, new OnProductoClickListener() {
                        @Override
                        public void onProductoClick(int position, long idProducto, String nombreProducto, long numerosProducto,
                                                    String descripcionProducto, double costoProducto, String urlImagenProducto,
                                                    boolean disponibleProducto, Integer comprasProducto) {
                            // Crear el bundle con los datos del producto
                            Bundle bundle = new Bundle();
                            bundle.putLong("idProducto", idProducto);
                            bundle.putString("nombreProducto", nombreProducto);
                            bundle.putLong("numerosProducto", numerosProducto);
                            bundle.putString("descripcionProducto", descripcionProducto);
                            bundle.putDouble("costoProducto", costoProducto);
                            bundle.putString("urlImagenProducto", urlImagenProducto);
                            bundle.putBoolean("disponibleProducto", disponibleProducto);
                            bundle.putInt("comprasProducto", comprasProducto);

                            Fragment fragment = switch (tipoUsuario) {
                                case "Paciente" -> new Fragment_tienda_virtual_producto();
                                case "Especialista" -> new Fragment_tienda_virtual_producto();
                                case "Administrador" -> new Fragment_tienda_virtual_producto();
                                default -> new Fragment_tienda_virtual_producto();
                            };
                            fragment.setArguments(bundle);
                            loadFragment(fragment);
                        }
                    }
                    );
                    recyclerView.setAdapter(adaptadorRVProductosVertical);
                }
            }

            @Override
            public void onError(String error) {
                if (getActivity() != null && isAdded()) {
                    Toast.makeText(getActivity(), "Error: " + error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.tienda_virtual_init_fragments_container, fragment)
                    .commit();
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.tool_bar_tienda_virtual, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
