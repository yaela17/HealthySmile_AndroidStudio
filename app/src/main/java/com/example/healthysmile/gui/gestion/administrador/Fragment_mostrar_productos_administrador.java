package com.example.healthysmile.gui.gestion.administrador;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
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
import com.example.healthysmile.controller.tiendaVirtual.ObtenerProductosResponseListener;
import com.example.healthysmile.utils.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.List;

public class Fragment_mostrar_productos_administrador extends Fragment implements AdapterView.OnItemClickListener {

    ListView productosLV, productosNoDisponiblesLV;
    ObtenerProductosService obtenerProductosService;
    SharedPreferencesHelper sharedPreferencesHelper;
    boolean eliminar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mostrar_productos_administrador, container, false);

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
                List<Integer> comprasDisponibles = new ArrayList<>();

                List<Long> idsProductoNODisponibles = new ArrayList<>();
                List<String> nombresProdNODisponibles = new ArrayList<>();
                List<Long> numerosProdNODisponibles = new ArrayList<>();
                List<String> descripcionesProdNODisponibles = new ArrayList<>();
                List<Double> costosProdNODisponibles = new ArrayList<>();
                List<String> urlsImagenNODisponibles = new ArrayList<>();
                List<Boolean> noSonDisponibles = new ArrayList<>();
                List<Integer> comprasNoDisponibles = new ArrayList<>();

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
                        comprasDisponibles.add(compras.get(i));
                    }else {
                        idsProductoNODisponibles.add(idsProducto.get(i));
                        nombresProdNODisponibles.add(nombresProd.get(i));
                        numerosProdNODisponibles.add(numerosProd.get(i));
                        descripcionesProdNODisponibles.add(descripcionesProd.get(i));
                        costosProdNODisponibles.add(costosProd.get(i));
                        urlsImagenNODisponibles.add(urlsImagen.get(i));
                        noSonDisponibles.add(false);
                        comprasNoDisponibles.add(compras.get(i));
                    }
                }
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        AdaptadorLVProductos adaptador = new AdaptadorLVProductos(
                                requireContext(), idsProductoDisponibles, nombresProdDisponibles, numerosProdDisponibles,
                                descripcionesProdDisponibles, costosProdDisponibles, urlsImagenDisponibles,disponibles, eliminar,comprasDisponibles
                        );
                        AdaptadorLVProductos adaptador2 = new AdaptadorLVProductos(requireContext(),idsProductoNODisponibles, nombresProdNODisponibles,
                                numerosProdNODisponibles,descripcionesProdNODisponibles,costosProdNODisponibles,urlsImagenNODisponibles,disponibles,eliminar, comprasNoDisponibles);
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
        Fragment_form_agregar_producto_administrador fragmentoDetalle = new Fragment_form_agregar_producto_administrador();
        Bundle args = new Bundle();
        args.putLong("idProducto", productoSeleccionado.getIdProd());
        args.putString("nombre", productoSeleccionado.getNombreProd());
        args.putInt("numerosProd",productoSeleccionado.getNumProd());
        args.putString("descripcion", productoSeleccionado.getDescriProd());
        args.putDouble("costo", productoSeleccionado.getCostoProd());
        args.putString("imagenUrl", productoSeleccionado.getImagen());
        args.putBoolean("disponible",productoSeleccionado.isDisponible());
        args.putInt("compras",productoSeleccionado.getCompras());
        fragmentoDetalle.setArguments(args);

        Log.d("Compras LV : ",String.valueOf(productoSeleccionado.getCompras()));

        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof Fragment_gestion_administrador_productos) {
            ((Fragment_gestion_administrador_productos) parentFragment).cargarFragment(fragmentoDetalle);
        }
    }

    /**
     * A simple {@link Fragment} subclass.
     * Use the {@link Fragment_mostrar_especialistas_administrador#newInstance} factory method to
     * create an instance of this fragment.
     */
    public static class Fragment_mostrar_especialistas_administrador extends Fragment {

        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private static final String ARG_PARAM1 = "param1";
        private static final String ARG_PARAM2 = "param2";

        // TODO: Rename and change types of parameters
        private String mParam1;
        private String mParam2;

        public Fragment_mostrar_especialistas_administrador() {
            // Required empty public constructor
        }

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Fragment_mostrar_especialistas_administrador.
         */
        // TODO: Rename and change types and number of parameters
        public static Fragment_mostrar_especialistas_administrador newInstance(String param1, String param2) {
            Fragment_mostrar_especialistas_administrador fragment = new Fragment_mostrar_especialistas_administrador();
            Bundle args = new Bundle();
            args.putString(ARG_PARAM1, param1);
            args.putString(ARG_PARAM2, param2);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                mParam1 = getArguments().getString(ARG_PARAM1);
                mParam2 = getArguments().getString(ARG_PARAM2);
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_mostrar_productos_administrador, container, false);
        }
    }

    /**
     * A simple {@link Fragment} subclass.
     * Use the {@link Fragment_gestion_administrador_citas#newInstance} factory method to
     * create an instance of this fragment.
     */
    public static class Fragment_gestion_administrador_citas extends Fragment {

        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private static final String ARG_PARAM1 = "param1";
        private static final String ARG_PARAM2 = "param2";

        // TODO: Rename and change types of parameters
        private String mParam1;
        private String mParam2;

        public Fragment_gestion_administrador_citas() {
            // Required empty public constructor
        }

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Fragment_gestion_administrador_citas.
         */
        // TODO: Rename and change types and number of parameters
        public static Fragment_gestion_administrador_citas newInstance(String param1, String param2) {
            Fragment_gestion_administrador_citas fragment = new Fragment_gestion_administrador_citas();
            Bundle args = new Bundle();
            args.putString(ARG_PARAM1, param1);
            args.putString(ARG_PARAM2, param2);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                mParam1 = getArguments().getString(ARG_PARAM1);
                mParam2 = getArguments().getString(ARG_PARAM2);
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_gestion_administrador_citas, container, false);
        }
    }
}
