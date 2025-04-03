package com.example.healthysmile.gui.extraAndroid.adaptadores;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
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
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.healthysmile.R;
import com.example.healthysmile.controller.ApiNodeMySqlRespuesta;
import com.example.healthysmile.model.relationships.RelProdCarritoUser;
import com.example.healthysmile.repository.NodeApiRetrofitClient;
import com.example.healthysmile.service.ApiNodeMySqlService;
import com.example.healthysmile.utils.ImageUtils;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder> {

    private Context context;
    private List<Long> idsProducto;
    private List<String> nombresProd;
    private List<Long> numerosProd;
    private List<String> descripcionesProd;
    private List<Double> costosProd;
    private List<String> urlsImagen;
    private List<Boolean> disponibles;
    private List<Integer> numerosProdDisponibles;
    private List<Integer> numProdTot;
    private List<Double> costTot;
    ImageUtils imageUtils;
    boolean borrar;

    public CarritoAdapter(Context context, List<Long> idsProducto, List<String> nombresProd, List<Long> numerosProd,
                          List<String> descripcionesProd, List<Double> costosProd, List<String> urlsImagen,
                          List<Boolean> disponibles, List<Integer> numerosProdDisponibles, List<Integer> numProdTot,
                          List<Double> costTot) {
        this.context = context;
        this.idsProducto = idsProducto;
        this.nombresProd = nombresProd;
        this.numerosProd = numerosProd;
        this.descripcionesProd = descripcionesProd;
        this.costosProd = costosProd;
        this.urlsImagen = urlsImagen;
        this.disponibles = disponibles;
        this.numerosProdDisponibles = numerosProdDisponibles;
        this.numProdTot = numProdTot;
        this.costTot = costTot;
        this.imageUtils = new ImageUtils();
        this.borrar = true;
    }

    @NonNull
    @Override
    public CarritoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.plantilla_vertical_carrito_compra_producto, parent, false);
        return new CarritoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarritoViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.nombreProd.setText(nombresProd.get(position));
        holder.descripcionProd.setText(descripcionesProd.get(position));
        holder.costoProd.setText("$" + costosProd.get(position));
        holder.numProd.setText(String.valueOf(numerosProd.get(position)));
        holder.disponibilidad.setText(disponibles.get(position) ? "Disponible" : "No disponible");
        imageUtils.cargarImagenConGlide(context,urlsImagen.get(position),holder.imagenProd);

        if (!disponibles.get(position)) {
            holder.etiquetaAgotado.setVisibility(View.VISIBLE);
        } else {
            holder.etiquetaAgotado.setVisibility(View.GONE);
        }

        holder.botonAumentar.setOnClickListener(v -> {
            int cantidadActual = Integer.parseInt(holder.numProd.getText().toString());
            if (cantidadActual < numerosProdDisponibles.get(position)) {
                cantidadActual++;
                holder.numProd.setText(String.valueOf(cantidadActual));
                numerosProd.set(position, (long) cantidadActual);

                actualizarProductoCarrito(idsProducto.get(position), cantidadActual,context);
            }
        });

        holder.botonDisminuir.setOnClickListener(v -> {
            int cantidadActual = Integer.parseInt(holder.numProd.getText().toString());
            if (cantidadActual > 1) {
                cantidadActual--;
                holder.numProd.setText(String.valueOf(cantidadActual));
                numerosProd.set(position, (long) cantidadActual);

                actualizarProductoCarrito(idsProducto.get(position), cantidadActual,context);
            }
        });



        holder.textoEliminar.setOnClickListener(v -> {
            final long idProducto = idsProducto.get(position);
            SharedPreferences sharedPreferences = context.getSharedPreferences("CarritoPreferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString("idProducto_" + position, String.valueOf(idProducto));
            editor.putString("nombreProducto_" + position, nombresProd.get(position));
            editor.putString("numeroProducto_" + position, String.valueOf(numerosProd.get(position)));  // Convertimos Long a String
            editor.putString("descripcionProducto_" + position, descripcionesProd.get(position));
            editor.putString("costoProducto_" + position, String.valueOf(costosProd.get(position)));  // Convertimos Double a String
            editor.putString("urlImagen_" + position, urlsImagen.get(position));
            editor.putString("disponibleProducto_" + position, String.valueOf(disponibles.get(position)));  // Convertimos Boolean a String
            editor.putString("numeroDisponible_" + position, String.valueOf(numerosProdDisponibles.get(position)));  // Convertimos Integer a String
            editor.putString("numProdTotal_" + position, String.valueOf(numProdTot.get(position)));  // Convertimos Integer a String
            editor.putString("costTot_" + position, String.valueOf(costTot.get(position)));  // Convertimos Double a String
            editor.putInt("position_" + position, position);  // Guardar la posición
            editor.apply();

            // 2. Eliminar producto de las listas de datos
            idsProducto.remove(position);
            nombresProd.remove(position);
            numerosProd.remove(position);
            descripcionesProd.remove(position);
            costosProd.remove(position);
            urlsImagen.remove(position);
            disponibles.remove(position);
            numerosProdDisponibles.remove(position);
            numProdTot.remove(position);
            costTot.remove(position);

            // Notificar que el item fue eliminado
            notifyItemRemoved(position);

            // 3. Mostrar el Snackbar con texto "Restablecer"
            Snackbar.make(v, "Producto eliminado", Snackbar.LENGTH_LONG)
                    .setAction("Restablecer", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            restablecerProducto(position, context, holder);
                            borrar = false;
                        }
                    })
                    .setDuration(3000)  // El Snackbar se mostrará por 3 segundos
                    .show();

            // Eliminar el producto después de 3 segundos si no se ha restablecido
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (borrar) {
                        Toast.makeText(context, "Producto eliminado", Toast.LENGTH_SHORT).show();
                        eliminarProducto(idProducto,context);
                    }
                    borrar = true;
                }
            }, 3000);  // 3 segundos de retraso
        });
    }

    @Override
    public int getItemCount() {
        return idsProducto.size();
    }

    public static class CarritoViewHolder extends RecyclerView.ViewHolder {
        TextView nombreProd, descripcionProd, costoProd, disponibilidad, etiquetaAgotado,textoEliminar;
        EditText numProd;
        ImageView imagenProd;
        Button botonAumentar, botonDisminuir;

        public CarritoViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreProd = itemView.findViewById(R.id.plantilla_vertical_carrito_compra_producto_nombreProducto);
            descripcionProd = itemView.findViewById(R.id.plantilla_vertical_carrito_compra_producto_descripcionProducto);
            costoProd = itemView.findViewById(R.id.plantilla_vertical_carrito_compra_producto_costoProducto);
            numProd = itemView.findViewById(R.id.plantilla_vertical_carrito_compra_producto_editText_cantidad);
            disponibilidad = itemView.findViewById(R.id.plantilla_vertical_carrito_compra_producto_disponibilidadProducto);
            etiquetaAgotado = itemView.findViewById(R.id.plantilla_vertical_carrito_compra_producto_agotado_label);
            imagenProd = itemView.findViewById(R.id.plantilla_vertical_carrito_compra_producto_imagenProducto);
            botonAumentar = itemView.findViewById(R.id.plantilla_vertical_carrito_compra_producto_boton_aumentar);
            botonDisminuir = itemView.findViewById(R.id.plantilla_vertical_carrito_compra_producto_boton_disminuir);
            textoEliminar = itemView.findViewById(R.id.plantilla_vertical_carrito_compra_producto_textoEliminar);
        }
    }

    private void actualizarProductoCarrito(Long idProducto, int nuevaCantidad, Context context) {
        ApiNodeMySqlService apiService = NodeApiRetrofitClient.getApiService();

        SharedPreferences sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        long idUsuario = sharedPreferences.getLong("idUsuario", -1);
        long idCarritoCompra = sharedPreferences.getLong("idCarritoCompra", -1);

        if (idUsuario == -1 || idCarritoCompra == -1) {
            Log.e("CarritoAdapter", "Error: No se encontraron los valores en SharedPreferences");
            return;
        }

        RelProdCarritoUser request = new RelProdCarritoUser();
        request.setIdUsuario((int) idUsuario);
        request.setIdCarritoCompra((int) idCarritoCompra);
        request.setIdProd(idProducto.intValue());
        request.setNumProd(nuevaCantidad);

        Call<ApiNodeMySqlRespuesta> call = apiService.actualizarProductoCarrito(request);
        call.enqueue(new Callback<ApiNodeMySqlRespuesta>() {
            @Override
            public void onResponse(Call<ApiNodeMySqlRespuesta> call, Response<ApiNodeMySqlRespuesta> response) {
                if (response.isSuccessful()) {
                    Log.d("CarritoAdapter", "Carrito actualizado correctamente");
                } else {
                    Log.e("CarritoAdapter", "Error en la actualización: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ApiNodeMySqlRespuesta> call, Throwable t) {
                Log.e("CarritoAdapter", "Fallo la conexión: " + t.getMessage());
            }
        });
    }

    private void eliminarProducto(Long idProducto, Context context) {
        ApiNodeMySqlService apiService = NodeApiRetrofitClient.getApiService();

        SharedPreferences sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        long idUsuario = sharedPreferences.getLong("idUsuario", -1);
        long idCarritoCompra = sharedPreferences.getLong("idCarritoCompra", -1);

        if (idUsuario == -1 || idCarritoCompra == -1) {
            Log.e("CarritoAdapter", "Error: No se encontraron los valores en SharedPreferences");
            return;
        }
        Call<ApiNodeMySqlRespuesta> call = apiService.eliminarProductoCarrito(String.valueOf(idUsuario),
                String.valueOf(idProducto),String.valueOf(idCarritoCompra));
        call.enqueue(new Callback<ApiNodeMySqlRespuesta>() {
            @Override
            public void onResponse(Call<ApiNodeMySqlRespuesta> call, Response<ApiNodeMySqlRespuesta> response) {
                if (response.isSuccessful()) {
                    Log.d("CarritoAdapter", "Producto eliminado correctamente de la base de datos");
                } else {
                    Log.e("CarritoAdapter", "Error al eliminar el producto: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ApiNodeMySqlRespuesta> call, Throwable t) {
                Log.e("CarritoAdapter", "Fallo la conexión: " + t.getMessage());
            }
        });
    }

    // Método para restablecer el producto, que insertará en la posición original
    private void restablecerProducto(int position, Context context,@NonNull CarritoViewHolder holder) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("CarritoPreferences", Context.MODE_PRIVATE);

        // Recuperar los datos almacenados de SharedPreferences
        String idProductoStr = sharedPreferences.getString("idProducto_" + position, null);
        String nombreProducto = sharedPreferences.getString("nombreProducto_" + position, null);
        String numeroProductoStr = sharedPreferences.getString("numeroProducto_" + position, null);
        String descripcionProducto = sharedPreferences.getString("descripcionProducto_" + position, null);
        String costoProductoStr = sharedPreferences.getString("costoProducto_" + position, null);
        String urlImagen = sharedPreferences.getString("urlImagen_" + position, null);
        String disponibleProductoStr = sharedPreferences.getString("disponibleProducto_" + position, null);
        String numeroDisponibleStr = sharedPreferences.getString("numeroDisponible_" + position, null);
        String numProdTotalStr = sharedPreferences.getString("numProdTotal_" + position, null);
        String costTotProductoStr = sharedPreferences.getString("costTot_" + position, null);
        int originalPosition = sharedPreferences.getInt("position_" + position, -1);  // Recuperar la posición original

        // Aquí comprobamos si los datos son válidos
        if (idProductoStr != null) {
            // Convertir los valores de String a sus tipos originales
            long idProducto = Long.parseLong(idProductoStr);
            long numeroProducto = Long.parseLong(numeroProductoStr);
            double costoProducto = Double.parseDouble(costoProductoStr);
            boolean disponibleProducto = Boolean.parseBoolean(disponibleProductoStr);
            int numeroDisponible = Integer.parseInt(numeroDisponibleStr);
            int numProdTotal = Integer.parseInt(numProdTotalStr);
            double costTotProducto = Double.parseDouble(costTotProductoStr);

            // Añadir el producto de nuevo a la lista en la posición original
            idsProducto.add(originalPosition, idProducto);
            nombresProd.add(originalPosition, nombreProducto);
            numerosProd.add(originalPosition, numeroProducto);
            descripcionesProd.add(originalPosition, descripcionProducto);
            costosProd.add(originalPosition, costoProducto);
            urlsImagen.add(originalPosition, urlImagen);
            disponibles.add(originalPosition, disponibleProducto);
            numerosProdDisponibles.add(originalPosition, numeroDisponible);
            numProdTot.add(originalPosition, numProdTotal);
            costTot.add(originalPosition, costTotProducto);

            // Cargar la imagen usando Glide a través de imageUtils
            imageUtils.cargarImagenConGlide(context, urlImagen, holder.imagenProd);

            // Notificar que el producto ha sido restablecido en la misma posición
            notifyItemInserted(originalPosition);
        }
    }
}