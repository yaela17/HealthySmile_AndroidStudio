package com.example.healthysmile.gui.extraAndroid.adaptadores;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthysmile.R;
import com.example.healthysmile.controller.adaptadores.OnProductoClickListener;
import com.example.healthysmile.service.SupabaseFileStorageService;
import com.example.healthysmile.utils.ImageUtils;

import java.util.List;

public class AdaptadorRVProductos extends RecyclerView.Adapter<AdaptadorRVProductos.ProductoViewHolder> {

    private Context contexto;
    private List<Long> idsProducto;
    private List<String> nombresProd;
    private List<Long> numerosProd;
    private List<String> descripcionesProd;
    private List<Double> costosProd;
    private List<String> urlsImagen;
    private List<Boolean> disponibles;
    private List<Integer> compras;
    private SupabaseFileStorageService supabaseService;
    private ImageUtils imageUtils;
    private OnProductoClickListener listener;

    public AdaptadorRVProductos(Context contexto, List<Long> idsProducto, List<String> nombresProd,
                                List<Long> numerosProd, List<String> descripcionesProd,
                                List<Double> costosProd, List<String> urlsImagen, List<Boolean> disponibles,
                                List<Integer> compras,OnProductoClickListener listener) {
        this.contexto = contexto;
        this.idsProducto = idsProducto;
        this.nombresProd = nombresProd;
        this.numerosProd = numerosProd;
        this.descripcionesProd = descripcionesProd;
        this.costosProd = costosProd;
        this.urlsImagen = urlsImagen;
        this.disponibles = disponibles;
        this.compras = compras;
        this.supabaseService = new SupabaseFileStorageService();
        this.imageUtils = new ImageUtils();
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(contexto).inflate(R.layout.plantilla_producto_tienda, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        holder.nombreProducto.setText(nombresProd.get(position));
        holder.cantidadProducto.setText("Cantidad: " + numerosProd.get(position));
        holder.costoProducto.setText("$" + costosProd.get(position));
        holder.descripcionProducto.setText(descripcionesProd.get(position));

        String urlImagen = urlsImagen.get(position);
        if (urlImagen != null && !urlImagen.isEmpty()) {
            Log.d("CargarImagen", "URL de la imagen: " + urlImagen);
            imageUtils.cargarImagenConGlide(contexto, urlImagen, holder.imageView);
        } else {
            Log.d("CargarImagen", "La URL de la imagen está vacía o nula.");
            holder.imageView.setImageResource(R.drawable.foto_a_seleccionar);
        }

        // Muestra o oculta el texto "No Disponible"
        if (disponibles.get(position)) {
            holder.noDisponible.setVisibility(View.GONE);
        } else {
            holder.noDisponible.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onProductoClick(position, idsProducto.get(position),nombresProd.get(position),numerosProd.get(position),
                        descripcionesProd.get(position),costosProd.get(position),urlsImagen.get(position),disponibles.get(position),compras.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return idsProducto.size();
    }

    public static class ProductoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nombreProducto, cantidadProducto, costoProducto, descripcionProducto, noDisponible;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.plantilla_producto_tienda_imagenProducto);
            nombreProducto = itemView.findViewById(R.id.plantilla_producto_tienda_nombreProducto);
            cantidadProducto = itemView.findViewById(R.id.plantilla_producto_tienda_cantidadProducto);
            costoProducto = itemView.findViewById(R.id.plantilla_producto_tienda_costoProducto);
            descripcionProducto = itemView.findViewById(R.id.plantilla_producto_tienda_descripcionProducto);
            noDisponible = itemView.findViewById(R.id.plantilla_producto_tienda_disponible);
        }
    }
}
