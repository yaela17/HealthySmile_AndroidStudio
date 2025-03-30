package com.example.healthysmile.gui.tiendavirtual;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthysmile.R;
import com.example.healthysmile.controller.ObtenerProductosResponseListener;
import com.example.healthysmile.gui.extraAndroid.adaptadores.AdaptadorRVProductosVertical;
import com.example.healthysmile.service.tiendaVirtual.ObtenerProductoPorNombreService;

import java.util.List;

public class Fragment_tienda_virtual_buscar_producto_nombre extends Fragment {

    private RecyclerView recyclerView;
    private EditText inputSearch;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable buscarRunnable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tienda_virtual_buscar_producto_nombre, container, false);

        // Inicializar RecyclerView
        recyclerView = view.findViewById(R.id.tienda_buscar_producto_por_nombre_RV);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        androidx.appcompat.widget.Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        inputSearch = toolbar.findViewById(R.id.top_bar_input_search);

        // Verificar si hay un query pasado por Bundle
        Bundle args = getArguments();
        if (args != null && args.containsKey("query")) {
            String query = args.getString("query");
            llenarRecyclerView(query);
        }

        inputSearch.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable editable) {
                handler.removeCallbacks(buscarRunnable);
                if (editable.length() > 0) {
                    buscarRunnable = new Runnable() {
                        @Override
                        public void run() {
                            buscarProducto();
                        }
                    };
                    handler.postDelayed(buscarRunnable, 500);
                }else {
                    loadFragment(new Fragment_tienda_virtual_default());
                }
            }
        });
        return view;
    }

    private void buscarProducto() {
        String query = inputSearch.getText().toString().trim();
        if (!query.isEmpty()) {
            llenarRecyclerView(query);
        }
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
                            getActivity(), idsProducto, nombresProd, numerosProd, descripcionesProd, costosProd, urlsImagen, disponibles, compras
                    );
                    recyclerView.setAdapter(adaptadorRVProductosVertical); // Setea el adaptador al RecyclerView
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
