package com.example.healthysmile.gui.extraAndroid.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.healthysmile.model.responses.ProductoCompra;
import com.example.healthysmile.utils.ImageUtils;
import com.example.healthysmile.R;

import java.util.List;

public class AdaptadorGestionCarritoCompraRV extends RecyclerView.Adapter<AdaptadorGestionCarritoCompraRV.ViewHolder> {

    private final List<ProductoCompra> listaProductos;
    private final Context context;
    private final ImageUtils imageUtils;

    public AdaptadorGestionCarritoCompraRV(Context context, List<ProductoCompra> listaProductos) {
        this.context = context;
        this.listaProductos = listaProductos;
        this.imageUtils = new ImageUtils();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(context).inflate(R.layout.plantilla_gestion_paciente_compras_compra_producto_rv, parent, false);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductoCompra producto = listaProductos.get(position);

        holder.txtNombre.setText(producto.getNombreProd());
        holder.txtDescripcion.setText(producto.getDescriProd());
        holder.txtCosto.setText("$" + String.format("%.2f", producto.getCostoProd()));
        holder.txtCantidad.setText("x" + producto.getNumProd());
        imageUtils.cargarImagenConGlide(context,producto.getImagen(),holder.imgProducto);
    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProducto;
        TextView txtNombre, txtDescripcion, txtCosto, txtCantidad;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProducto = itemView.findViewById(R.id.plantilla_gestion_paciente_compras_compra_producto_img_imagen);
            txtNombre = itemView.findViewById(R.id.plantilla_gestion_paciente_compras_compra_producto_txt_nombre);
            txtDescripcion = itemView.findViewById(R.id.plantilla_gestion_paciente_compras_compra_producto_txt_descripcion);
            txtCosto = itemView.findViewById(R.id.plantilla_gestion_paciente_compras_compra_producto_txt_costo);
            txtCantidad = itemView.findViewById(R.id.plantilla_gestion_paciente_compras_compra_producto_txt_cantidad);
        }
    }
}
