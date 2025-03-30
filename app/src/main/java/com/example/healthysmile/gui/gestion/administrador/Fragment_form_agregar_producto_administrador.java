package com.example.healthysmile.gui.gestion.administrador;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthysmile.R;
import com.example.healthysmile.controller.ApiNodeMySqlRespuesta;
import com.example.healthysmile.model.entities.Producto;
import com.example.healthysmile.repository.NodeApiRetrofitClient;
import com.example.healthysmile.service.gestion.ActualizarFotoProductoService;
import com.example.healthysmile.service.ApiNodeMySqlService;
import com.example.healthysmile.utils.ImageUtils;

import java.io.File;
import java.io.IOException;

import javax.annotation.Nullable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_form_agregar_producto_administrador extends Fragment {

    ImageView fotoProducto;
    Button btnCambiarFoto, btnAgregarProducto,btnEditarProducto;
    EditText inputNombreProd, inputNumProd, inputDescProd, inputCostProd;
    private static final int REQUEST_GALLERY_IMAGE = 101;

    //Caso en que sea modificar
    long idProductoModificar;
    String nombreProductoModificar, descripcionProductoModificar, imagenUrlProductoModificar;
    Integer numerosProductoModificar,comprasProductoModificar;
    double costoProductoModificar;
    boolean disponibleProductoModificar;
    LinearLayout layoutDisponibilidad;
    TextView tituloDisponibilidad;
    SwitchCompat switchDisponibilidad;

    //Controlar si es modificar o agregar
    boolean agregar = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_form_agregar_producto_administrador, container, false);

        fotoProducto = view.findViewById(R.id.form_agregar_producto_foto_producto);
        btnCambiarFoto = view.findViewById(R.id.form_agregar_producto_btn_cambiar_foto);
        btnAgregarProducto = view.findViewById(R.id.form_agregar_producto_btn_agregar_producto);
        inputNombreProd = view.findViewById(R.id.form_agregar_producto_input_nombre_producto);
        inputNumProd = view.findViewById(R.id.form_agregar_producto_input_cantidad_producto);
        inputDescProd = view.findViewById(R.id.form_agregar_producto_input_descripcion_producto);
        inputCostProd = view.findViewById(R.id.form_agregar_producto_input_costo_producto);

        btnCambiarFoto.setOnClickListener(v -> cambiarFotoProducto());

        Bundle args = getArguments();
        if (args != null) {
            layoutDisponibilidad = view.findViewById(R.id.form_agregar_producto_layout_disponibilidad);
            tituloDisponibilidad = view.findViewById(R.id.titulo_form_agregar_producto_switch_disponible);
            switchDisponibilidad = view.findViewById(R.id.form_agregar_producto_switch_disponible);
            btnEditarProducto = view.findViewById(R.id.form_agregar_producto_btn_editar);
            btnEditarProducto.setVisibility(View.VISIBLE);
            layoutDisponibilidad.setVisibility(View.VISIBLE);
            btnCambiarFoto.setVisibility(View.GONE);

            btnEditarProducto.setOnClickListener(v -> habilitarInputs(true));

            agregar = false;

            idProductoModificar = args.getLong("idProducto");
            nombreProductoModificar = args.getString("nombre");
            numerosProductoModificar = args.getInt("numerosProd");
            descripcionProductoModificar = args.getString("descripcion");
            costoProductoModificar = args.getDouble("costo");
            imagenUrlProductoModificar = args.getString("imagenUrl");
            disponibleProductoModificar = args.getBoolean("disponible");
            comprasProductoModificar = args.getInt("compras");

            inputNombreProd.setText(nombreProductoModificar);
            inputNumProd.setText(String.valueOf(numerosProductoModificar));
            inputDescProd.setText(descripcionProductoModificar);
            inputCostProd.setText(String.valueOf(costoProductoModificar));
            ImageUtils imageUtils = new ImageUtils();
            imageUtils.cargarImagenConGlide(getContext(),imagenUrlProductoModificar,fotoProducto);
            switchDisponibilidad.setChecked(disponibleProductoModificar);
            habilitarInputs(false);
        }

        if (agregar){
            btnAgregarProducto.setText("Agregar");
            btnAgregarProducto.setOnClickListener(v -> agregarProducto());
        }else {
            btnAgregarProducto.setText("Actualizar");
            btnAgregarProducto.setOnClickListener(v -> actualizarProducto());
        }

        return view;
    }


    // Método para cambiar la foto del producto
    public void cambiarFotoProducto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_GALLERY_IMAGE);
    }

    // Método para manejar la respuesta de la galería
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GALLERY_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                // Convertir la URI a un archivo temporal
                ImageUtils imageUtils = new ImageUtils();
                File file = imageUtils.convertUriToFile(getContext(),selectedImageUri);
                if (file != null && file.exists()) {
                    Log.d("FotoProducto", "El archivo existe");
                    Log.d("FotoProducto", "Nombre del archivo: " + file.getName());
                    ActualizarFotoProductoService actualizarFotoProductoService = new ActualizarFotoProductoService(getContext());
                    actualizarFotoProductoService.actualizarFotoProducto(file);

                    fotoProducto.setImageURI(selectedImageUri);
                } else {
                    Log.e("FotoProducto", "El archivo no existe o es nulo");
                    Toast.makeText(requireContext(), "No se encontró el archivo de imagen", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    // Método para agregar el producto
    public void agregarProducto() {
        // Verificar si hay campos vacíos
        if (verificarCamposVacios()) return;

        // Verificar si los datos numéricos son válidos
        if (!verificarDatosNumericos()) return;

        // Verificar si la URL existe en SharedPreferences
        if (!verificarUrl()) return;

        // Obtener los valores de los campos EditText
        String nombreProducto = inputNombreProd.getText().toString().trim();
        String cantidadProducto = inputNumProd.getText().toString().trim();
        String descripcionProducto = inputDescProd.getText().toString().trim();
        String costoProducto = inputCostProd.getText().toString().trim();

        // Obtener la URL de la foto del producto desde SharedPreferences
        SharedPreferences prefs = getContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        String urlFotoProducto = prefs.getString("urlFotoProducto", null);

        if (urlFotoProducto == null) {
            Toast.makeText(getContext(), "No se ha guardado una URL para la foto del producto", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear el objeto Producto
        Producto producto = new Producto();
        producto.setNombreProd(nombreProducto);
        producto.setNumProd(Integer.parseInt(cantidadProducto)); // Asumimos que la cantidad es un número entero
        producto.setDescriProd(descripcionProducto);
        producto.setCostoProd(Double.parseDouble(costoProducto)); // Asumimos que el costo es un número decimal
        producto.setImagen(urlFotoProducto); // URL de la foto del producto

        // Obtener el servicio Retrofit
        ApiNodeMySqlService apiService = NodeApiRetrofitClient.getApiService();

        // Llamar al servicio Retrofit para agregar el producto
        Call<ApiNodeMySqlRespuesta> call = apiService.agregarProducto(producto);

        // Hacer la llamada a Retrofit en un hilo en segundo plano (asíncrono)
        call.enqueue(new Callback<ApiNodeMySqlRespuesta>() {
            @Override
            public void onResponse(Call<ApiNodeMySqlRespuesta> call, Response<ApiNodeMySqlRespuesta> response) {
                if (response.isSuccessful()) {
                    // Si la respuesta es exitosa, mostramos un mensaje y vaciamos los campos
                    ApiNodeMySqlRespuesta respuesta = response.body();
                    if (respuesta != null && respuesta.getMensaje() != null) {
                        Toast.makeText(getContext(), respuesta.getMensaje(), Toast.LENGTH_SHORT).show();
                        vaciarCampos();
                    } else {
                        Toast.makeText(getContext(), "Producto agregado correctamente", Toast.LENGTH_SHORT).show();
                        vaciarCampos();
                    }
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.remove("urlFotoProducto");
                    editor.apply();
                } else {
                    // Si la respuesta no es exitosa, mostramos un mensaje de error
                    Toast.makeText(getContext(), "Error al agregar el producto", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ApiNodeMySqlRespuesta> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void actualizarProducto() {
        if (verificarCamposVacios()) return;
        if (!verificarDatosNumericos()) return;

        int idProductoModificarFormated = (int) idProductoModificar;
        String nombreProducto = inputNombreProd.getText().toString().trim();
        String cantidadProducto = inputNumProd.getText().toString().trim();
        String descripcionProducto = inputDescProd.getText().toString().trim();
        String costoProducto = inputCostProd.getText().toString().trim();
        boolean disponibilidadProducto = switchDisponibilidad.isChecked();

        // Obtener la URL de la foto del producto desde SharedPreferences
        SharedPreferences prefs = getContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        String urlFotoProducto = prefs.getString("urlFotoProducto", null);

        if (urlFotoProducto == null) {
            urlFotoProducto = imagenUrlProductoModificar;
        }

        // Crear el objeto Producto con los datos actualizados
        Producto producto = new Producto();
        producto.setIdProd(idProductoModificarFormated); // ID del producto que se va a actualizar
        producto.setNombreProd(nombreProducto);
        producto.setNumProd(Integer.parseInt(cantidadProducto)); // Asumimos que la cantidad es un número entero
        producto.setDescriProd(descripcionProducto);
        producto.setCostoProd(Double.parseDouble(costoProducto)); // Asumimos que el costo es un número decimal
        producto.setImagen(urlFotoProducto); // URL de la foto del producto
        producto.setCompras(comprasProductoModificar);
        producto.setDisponible(disponibilidadProducto); // Disponibilidad del producto

        // Obtener el servicio Retrofit
        ApiNodeMySqlService apiService = NodeApiRetrofitClient.getApiService();
        Log.d("API_REQUEST", "Producto: " + producto.toString());

        // Llamar al servicio Retrofit para actualizar el producto
        Call<ApiNodeMySqlRespuesta> call = apiService.actualizarProducto(producto);

        call.enqueue(new Callback<ApiNodeMySqlRespuesta>() {
            @Override
            public void onResponse(Call<ApiNodeMySqlRespuesta> call, Response<ApiNodeMySqlRespuesta> response) {
                // Log para el código de respuesta HTTP
                Log.d("API_RESPONSE", "Código de respuesta: " + response.code());  // Mostrar el código de respuesta HTTP

                // Log del cuerpo de la respuesta de error (si está disponible)
                try {
                    if (response.errorBody() != null) {
                        String errorBody = response.errorBody().string();  // Leer el cuerpo de error si existe
                        Log.d("API_RESPONSE", "Cuerpo de la respuesta de error: " + errorBody);
                    }
                } catch (IOException e) {
                    Log.e("API_RESPONSE", "Error al leer el cuerpo de la respuesta de error: " + e.getMessage());
                }

                // Revisar si la respuesta fue exitosa
                if (response.isSuccessful()) {
                    // Si la respuesta es exitosa, mostramos un mensaje
                    ApiNodeMySqlRespuesta respuesta = response.body();
                    if (respuesta != null && respuesta.getMensaje() != null) {
                        Log.d("API_RESPONSE", "Mensaje del servidor: " + respuesta.getMensaje());
                        Toast.makeText(getContext(), respuesta.getMensaje(), Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("API_RESPONSE", "Producto actualizado correctamente");
                        Toast.makeText(getContext(), "Producto actualizado correctamente", Toast.LENGTH_SHORT).show();
                        habilitarInputs(false);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.remove("urlFotoProducto");
                        editor.apply();
                    }
                } else {
                    // Si la respuesta no es exitosa, mostramos un mensaje de error
                    Log.d("API_RESPONSE", "Error al actualizar el producto, código: " + response.code());

                    // Verificar si hay detalles adicionales en la respuesta de error
                    try {
                        String errorResponseBody = response.errorBody() != null ? response.errorBody().string() : "No hay cuerpo de error";
                        Log.d("API_RESPONSE", "Cuerpo de la respuesta de error detallado: " + errorResponseBody);
                    } catch (IOException e) {
                        Log.e("API_RESPONSE", "Error al leer el cuerpo de la respuesta de error: " + e.getMessage());
                    }

                    Toast.makeText(getContext(), "Error al actualizar el producto", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiNodeMySqlRespuesta> call, Throwable t) {
                Log.e("API_ERROR", "Error de conexión: " + t.getMessage());  // Log de error si ocurre una falla

                // Log adicional para detalles de la excepción
                Log.e("API_ERROR", "Excepción completa: ", t);

                Toast.makeText(getContext(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
    // Método para verificar campos vacíos
    public boolean verificarCamposVacios() {
        if (inputNombreProd.getText().toString().isEmpty() ||
                inputNumProd.getText().toString().isEmpty() ||
                inputDescProd.getText().toString().isEmpty() ||
                inputCostProd.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    // Método para verificar si los datos numéricos son válidos
    public boolean verificarDatosNumericos() {
        try {
            Integer.parseInt(inputNumProd.getText().toString());  // Verificar si numProd es un Integer
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "La cantidad del producto debe ser un número válido", Toast.LENGTH_SHORT).show();
            return false;
        }

        try {
            Double.parseDouble(inputCostProd.getText().toString());  // Verificar si costProd es un Double
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "El costo del producto debe ser un número válido", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    // Método para vaciar los campos después de agregar un producto
    public void vaciarCampos() {
        inputNombreProd.setText("");
        inputNumProd.setText("");
        inputDescProd.setText("");
        inputCostProd.setText("");
        fotoProducto.setImageDrawable(getResources().getDrawable(R.drawable.foto_a_seleccionar));
    }

    public boolean verificarUrl() {
        SharedPreferences prefs = getContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        String urlFotoProducto = prefs.getString("urlFotoProducto", null);
        if (urlFotoProducto == null) {
            Toast.makeText(getContext(), "No se ha guardado una URL para la foto del producto", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void habilitarInputs(boolean estado){
        inputNombreProd.setEnabled(estado);
        inputNumProd.setEnabled(estado);
        inputDescProd.setEnabled(estado);
        inputCostProd.setEnabled(estado);
        switchDisponibilidad.setEnabled(estado);
        btnAgregarProducto.setEnabled(estado);
    }

}
