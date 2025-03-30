package com.example.healthysmile.gui.tiendavirtual;

import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Handler;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;

import com.example.healthysmile.R;

public class Fragment_tienda_virtual_init extends Fragment {

    EditText inputSearch;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable buscarRunnable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Infla el layout para este fragmento
        View view = inflater.inflate(R.layout.fragment_tienda_virtual_init, container, false);

        // Obtener la barra de herramientas y el input de búsqueda
        androidx.appcompat.widget.Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        inputSearch = toolbar.findViewById(R.id.top_bar_input_search);
        setHasOptionsMenu(true);

        // Llamar al método para cargar el fragmento por defecto
        loadFragment(new Fragment_tienda_virtual_default());

        inputSearch.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable editable) {
                // Cancelar cualquier búsqueda previa pendiente
                handler.removeCallbacks(buscarRunnable);

                // Solo ejecutar la búsqueda si el texto no está vacío
                if (editable.length() > 0) {
                    // Crear un nuevo Runnable para ejecutar la búsqueda después de 1 segundo
                    buscarRunnable = new Runnable() {
                        @Override
                        public void run() {
                            buscarProducto(); // Ejecutar la búsqueda
                        }
                    };
                    // Esperar 1 segundo (1000 ms) antes de ejecutar la búsqueda
                    handler.postDelayed(buscarRunnable, 500);
                }else {
                    loadFragment(new Fragment_tienda_virtual_default());
                }
            }
        });

        return view;
    }

    public void loadFragment(Fragment fragment) {
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.tienda_virtual_init_fragments_container, fragment)
                    .commit();
        }
    }

    private void buscarProducto() {
        String query = inputSearch.getText().toString().trim();
        if (!query.isEmpty()) {
            Fragment_tienda_virtual_buscar_producto_nombre fragment = new Fragment_tienda_virtual_buscar_producto_nombre();

            Bundle args = new Bundle();
            args.putString("query", query);
            fragment.setArguments(args);

            loadFragment(fragment);
        } else {
            Toast.makeText(requireContext(), "Por favor ingrese un texto para buscar", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.tool_bar_tienda_virtual, menu); // Inflar el menú del fragmento
        super.onCreateOptionsMenu(menu, inflater);
    }
}
