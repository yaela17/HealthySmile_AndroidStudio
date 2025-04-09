package com.example.healthysmile.gui.tiendavirtual;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.healthysmile.R;
import com.example.healthysmile.controller.ApiNodeMySqlRespuesta;
import com.example.healthysmile.gui.extraAndroid.dialog.MetodoPagoDialogFragment;
import com.example.healthysmile.model.ItemCarrito;
import com.example.healthysmile.repository.NodeApiRetrofitClient;
import com.example.healthysmile.service.ApiNodeMySqlService;
import com.example.healthysmile.utils.ImageUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_tienda_virtual_producto extends Fragment {
    private ImageView imagenProducto;
    private TextView nombreProducto, descripcionProducto, precioProducto,cantidadInvetario;
    private EditText cantidadProducto;
    private Button btnDisminuir, btnAumentar, btnAgregarCarrito, btnComprar;
    private long idProducto, numerosProducto;
    private String nombre, descripcion, urlImagen;
    private double costo;
    private boolean disponible;
    private int compras;
    ImageUtils imageUtils;
    long idCarritoCompra;
    long idUsuario;
    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tienda_virtual_producto, container, false);

        // Inicializar vistas
        imagenProducto = view.findViewById(R.id.tienda_vitual_producto_imagen_producto);
        nombreProducto = view.findViewById(R.id.tienda_vitual_producto_nombre_producto);
        cantidadInvetario = view.findViewById(R.id.tienda_vitual_producto_cantidad_producto_inventario);
        descripcionProducto = view.findViewById(R.id.tienda_vitual_producto_descripcion_producto);
        precioProducto = view.findViewById(R.id.tienda_vitual_producto_precio_producto);
        cantidadProducto = view.findViewById(R.id.tienda_vitual_producto_cantidad_producto);
        btnDisminuir = view.findViewById(R.id.tienda_vitual_producto_btn_disminuir);
        btnAumentar = view.findViewById(R.id.tienda_vitual_producto_btn_aumentar);
        btnAgregarCarrito = view.findViewById(R.id.tienda_vitual_producto_btn_agregar_carrito);
        btnComprar = view.findViewById(R.id.tienda_vitual_producto_btn_comprar);

        imageUtils = new ImageUtils();

        // Obtener datos del bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            idProducto = bundle.getLong("idProducto");
            nombre = bundle.getString("nombreProducto");
            numerosProducto = bundle.getLong("numerosProducto");
            descripcion = bundle.getString("descripcionProducto");
            costo = bundle.getDouble("costoProducto");
            urlImagen = bundle.getString("urlImagenProducto");
            disponible = bundle.getBoolean("disponibleProducto");
            compras = bundle.getInt("comprasProducto");

            // Colocar datos en sus respectivas vistas
            nombreProducto.setText(nombre);
            descripcionProducto.setText(descripcion);
            precioProducto.setText("$ " + costo);
            cantidadInvetario.setText("Cantidad actual en inventario : " + numerosProducto);
            cantidadProducto.setText("1");

            if (urlImagen != null && !urlImagen.isEmpty()) {
                imageUtils.cargarImagenConGlide(requireContext(), urlImagen, imagenProducto);
            }
        }

        // Obtener idUsuario y idCarritoCompra desde SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences("AppPrefs", getContext().MODE_PRIVATE);
        idUsuario = sharedPreferences.getLong("idUsuario", -1);
        idCarritoCompra = sharedPreferences.getLong("idCarritoCompra", -1);

        // Log para verificar el valor de idCarritoCompra
        Log.d("CarritoFragment", "ID Carrito Recuperado: " + idCarritoCompra);

        // Configurar filtros y validaciones para cantidadProducto
        cantidadProducto.setInputType(InputType.TYPE_CLASS_NUMBER);
        cantidadProducto.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
        cantidadProducto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                validarCantidad();
            }
        });

        // Botón agregar al carrito
        btnAgregarCarrito.setOnClickListener(v -> {
            int cantidad = obtenerCantidad();
            agregarCarrito(cantidad);
        });

        btnDisminuir.setOnClickListener(v -> {
            int cantidad = obtenerCantidad();
            if (cantidad > 1) {
                cantidad--;
                cantidadProducto.setText(String.valueOf(cantidad));
            }
        });

        btnAumentar.setOnClickListener(v -> {
            int cantidad = obtenerCantidad();
            if (cantidad < numerosProducto) {
                cantidad++;
                cantidadProducto.setText(String.valueOf(cantidad));
            }
        });

        btnComprar.setOnClickListener(v -> comprarProducto());

        return view;
    }

    private void comprarProducto() {
        int cantidad = obtenerCantidad();
        double total = cantidad * costo;
        List<ItemCarrito> carrito = new ArrayList<>();
        ItemCarrito producto = new ItemCarrito();
        producto.setNombre(nombre);
        producto.setCantidad(Integer.parseInt(cantidadProducto.getText().toString().trim()));
        producto.setPrecio(costo);
        carrito.add(producto);
        MetodoPagoDialogFragment dialog = new MetodoPagoDialogFragment(getContext(),total,carrito,(int) idProducto,true);
        dialog.show(requireActivity().getSupportFragmentManager(), "MetodoPagoDialog");
    }

    private void agregarCarrito(int cantidad) {
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("p_idUsuario", idUsuario);
        parametros.put("p_numProd", cantidad);
        parametros.put("p_costoProd", costo);
        parametros.put("p_idProd", idProducto);
        parametros.put("p_idCarritoCompra", idCarritoCompra);

        ApiNodeMySqlService apiService = NodeApiRetrofitClient.getApiService();

        apiService.agregarProductoCarrito(parametros).enqueue(new Callback<ApiNodeMySqlRespuesta>() {
            @Override
            public void onResponse(Call<ApiNodeMySqlRespuesta> call, Response<ApiNodeMySqlRespuesta> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiNodeMySqlRespuesta respuesta = response.body();

                    // Log para verificar la respuesta del servidor
                    Log.d("CarritoResponse", "Respuesta recibida: " + respuesta.toString());

                    long idCarritoCompra = respuesta.getIdCarritoCompra();

                    // Log para verificar el valor de idCarritoCompra
                    Log.d("CarritoResponse", "ID Carrito Compra recibido: " + idCarritoCompra);

                    if (idCarritoCompra != -1 && idCarritoCompra != 0) {
                        // Mostrar mensaje de éxito
                        Toast.makeText(getContext(), "Producto agregado al carrito con éxito. ID del carrito: " + idCarritoCompra, Toast.LENGTH_SHORT).show();

                        // Guardar el ID del carrito en SharedPreferences
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putLong("idCarritoCompra", idCarritoCompra);
                        editor.apply();

                        numerosProducto = numerosProducto - cantidad;
                        cantidadInvetario.setText("Cantidad actual en inventario : " + numerosProducto);

                        // Log para verificar que SharedPreferences fue actualizado correctamente
                        Log.d("CarritoResponse", "ID Carrito guardado en SharedPreferences: " + idCarritoCompra);
                    } else {
                        // Si el ID es inválido, mostrar un mensaje de error
                        Toast.makeText(getContext(), "Ya existia el carrito", Toast.LENGTH_SHORT).show();
                    }if(idCarritoCompra == 0){
                        numerosProducto = numerosProducto - cantidad;
                        cantidadInvetario.setText("Cantidad actual en inventario : " + numerosProducto);
                    }
                } else {
                    Toast.makeText(getContext(), "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                    Log.e("CarritoResponse", "Error en la respuesta: " + (response.body() != null ? response.body().toString() : "Respuesta vacía"));
                }
            }
            @Override
            public void onFailure(Call<ApiNodeMySqlRespuesta> call, Throwable t) {
                Toast.makeText(getContext(), "Error al conectar con la API", Toast.LENGTH_SHORT).show();
                Log.e("CarritoResponse", "Error de conexión: " + t.getMessage(), t);
            }
        });
    }

    private int obtenerCantidad() {
        String texto = cantidadProducto.getText().toString().trim();
        if (texto.isEmpty()) return 1;
        int cantidad = Math.max(1, Math.min(Integer.parseInt(texto), (int) numerosProducto));
        cantidadProducto.setText(String.valueOf(cantidad));
        return cantidad;
    }

    private void validarCantidad() {
        String texto = cantidadProducto.getText().toString().trim();
        if (!texto.isEmpty()) {
            try {
                int cantidad = Integer.parseInt(texto);
                if (cantidad < 1) {
                    cantidadProducto.setText("1");
                } else if (cantidad > numerosProducto) {
                    cantidadProducto.setText(String.valueOf(numerosProducto));
                }
            } catch (NumberFormatException e) {
                cantidadProducto.setText("1");
            }
        }
    }
}