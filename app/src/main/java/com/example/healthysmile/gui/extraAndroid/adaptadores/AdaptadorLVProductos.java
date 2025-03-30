package com.example.healthysmile.gui.extraAndroid.adaptadores;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.healthysmile.R;
import com.example.healthysmile.controller.ApiNodeMySqlRespuesta;
import com.example.healthysmile.model.entities.Producto;
import com.example.healthysmile.repository.NodeApiRetrofitClient;
import com.example.healthysmile.service.ApiNodeMySqlService;
import com.example.healthysmile.service.SupabaseFileStorageService;
import com.example.healthysmile.utils.ImageUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdaptadorLVProductos extends BaseAdapter {

    private Context contexto;
    private List<Long> idsProducto;
    private List<String> nombresProd;
    private List<Long> numerosProd;
    private List<String> descripcionesProd;
    private List<Double> costosProd;
    private List<String> urlsImagen;
    private List<Boolean> disponibles;
    private List<Integer> compras;
    private LayoutInflater inflater;
    private SupabaseFileStorageService supabaseService;
    private ImageUtils imageUtils;  // Añadimos ImageUtils como un objeto
    private boolean eliminar;

    public AdaptadorLVProductos(Context contexto, List<Long> idsProducto, List<String> nombresProd,
                                List<Long> numerosProd, List<String> descripcionesProd,
                                List<Double> costosProd, List<String> urlsImagen,List<Boolean>disponibles, boolean eliminar,List<Integer> compras) {
        this.contexto = contexto;
        this.idsProducto = idsProducto;
        this.nombresProd = nombresProd;
        this.numerosProd = numerosProd;
        this.descripcionesProd = descripcionesProd;
        this.costosProd = costosProd;
        this.urlsImagen = urlsImagen;
        this.inflater = LayoutInflater.from(contexto);
        this.supabaseService = new SupabaseFileStorageService();
        this.imageUtils = new ImageUtils();
        this.eliminar = eliminar;
        this.disponibles = disponibles;
        this.compras = compras;
    }

    @Override
    public int getCount() {
        return idsProducto.size();
    }

    @Override
    public Producto getItem(int position) {
        return new Producto(nombresProd.get(position),
                compras.get(position),
                costosProd.get(position),
                descripcionesProd.get(position),
                idsProducto.get(position).intValue(),
                urlsImagen.get(position),
                numerosProd.get(position).intValue(),
                disponibles.get(position));
    }

    @Override
    public long getItemId(int position) {
        return idsProducto.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.plantilla_list_view_producto, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.plantilla_producto_imagenProducto);
        TextView nombreProducto = convertView.findViewById(R.id.plantilla_producto_nombreProducto);
        TextView cantidadProducto = convertView.findViewById(R.id.plantilla_producto_cantidadProducto);
        TextView costoProducto = convertView.findViewById(R.id.plantilla_producto_costoProducto);
        TextView descripcionProducto = convertView.findViewById(R.id.plantilla_producto_descripcionProducto);
        ImageButton eliminarProducto = convertView.findViewById(R.id.plantilla_producto_boton_eliminar_producto);

        nombreProducto.setText(nombresProd.get(position));
        cantidadProducto.setText("Cantidad: " + numerosProd.get(position));
        costoProducto.setText("Costo: $" + costosProd.get(position));
        descripcionProducto.setText(descripcionesProd.get(position));

        String urlImagen = urlsImagen.get(position);
        if (urlImagen != null && !urlImagen.isEmpty()) {
            Log.d("CargarImagen", "URL de la imagen: " + urlImagen); // Log para verificar la URL

            // Usamos ImageUtils para cargar la imagen con Glide
            imageUtils.cargarImagenConGlide(contexto, urlImagen, imageView); // Llamada a Glide
        } else {
            Log.d("CargarImagen", "La URL de la imagen está vacía o nula.");
            imageView.setImageResource(R.drawable.foto_a_seleccionar); // Imagen por defecto
        }

        if(eliminar){
            eliminarProducto.setVisibility(View.VISIBLE);
            eliminarProducto.setOnClickListener(v -> {
                Integer idProducto = idsProducto.get(position).intValue();
                Producto producto = new Producto();
                producto.setIdProd(idProducto);
                deshabilitarProducto(producto, position);
            });
        }

        return convertView;
    }

    private void deshabilitarProducto(Producto producto, int position) {
        ApiNodeMySqlService apiService = NodeApiRetrofitClient.getApiService();

        apiService.deshabilitarProducto(producto).enqueue(new Callback<ApiNodeMySqlRespuesta>() {
            @Override
            public void onResponse(Call<ApiNodeMySqlRespuesta> call, Response<ApiNodeMySqlRespuesta> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("EliminarProducto", "Producto deshabilitado correctamente: " + response.body().getMensaje());
                    idsProducto.remove(position);
                    nombresProd.remove(position);
                    numerosProd.remove(position);
                    descripcionesProd.remove(position);
                    costosProd.remove(position);
                    urlsImagen.remove(position);
                    disponibles.remove(position);
                    notifyDataSetChanged();
                } else {
                    Log.e("EliminarProducto", "Error en la respuesta del servidor");
                }
            }

            @Override
            public void onFailure(Call<ApiNodeMySqlRespuesta> call, Throwable t) {
                Log.e("EliminarProducto", "Error al llamar a la API", t);
            }
        });
    }
}
