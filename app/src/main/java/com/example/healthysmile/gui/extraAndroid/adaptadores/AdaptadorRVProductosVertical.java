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
import com.example.healthysmile.service.SupabaseFileStorageService;
import com.example.healthysmile.utils.ImageUtils;

import java.util.List;

public class AdaptadorRVProductosVertical extends RecyclerView.Adapter<AdaptadorRVProductosVertical.ProductoViewHolder> {

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

    public AdaptadorRVProductosVertical(Context contexto, List<Long> idsProducto, List<String> nombresProd,
                                List<Long> numerosProd, List<String> descripcionesProd,
                                List<Double> costosProd, List<String> urlsImagen, List<Boolean> disponibles,
                                List<Integer> compras) {
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
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(contexto).inflate(R.layout.plantilla_vertical_producto, parent, false);
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

        // Muestra o oculta el texto "A G O T A D O"
        if (disponibles.get(position)) {
            holder.agotadoLabel.setVisibility(View.GONE);
        } else {
            holder.agotadoLabel.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return idsProducto.size();
    }

    public static class ProductoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nombreProducto, cantidadProducto, costoProducto, descripcionProducto, agotadoLabel;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.plantilla_producto_imagenProducto);
            nombreProducto = itemView.findViewById(R.id.plantilla_producto_nombreProducto);
            cantidadProducto = itemView.findViewById(R.id.plantilla_producto_cantidadProducto);
            costoProducto = itemView.findViewById(R.id.plantilla_producto_costoProducto);
            descripcionProducto = itemView.findViewById(R.id.plantilla_producto_descripcionProducto);
            agotadoLabel = itemView.findViewById(R.id.plantilla_producto_agotado_label);
        }
    }
}
