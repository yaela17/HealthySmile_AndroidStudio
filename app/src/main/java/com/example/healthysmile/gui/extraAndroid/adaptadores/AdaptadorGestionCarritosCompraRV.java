package com.example.healthysmile.gui.extraAndroid.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.healthysmile.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthysmile.model.responses.ObtenerCarritosCompraResponse;

import java.util.List;

public class AdaptadorGestionCarritosCompraRV extends RecyclerView.Adapter<AdaptadorGestionCarritosCompraRV.ViewHolder> {

    private List<ObtenerCarritosCompraResponse> listaCompras;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ObtenerCarritosCompraResponse compra);
    }

    public AdaptadorGestionCarritosCompraRV(List<ObtenerCarritosCompraResponse> listaCompras, OnItemClickListener listener) {
        this.listaCompras = listaCompras;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.plantilla_gestion_paciente_compras_rv, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ObtenerCarritosCompraResponse compra = listaCompras.get(position);

        holder.idCompra.setText("Compra #" + compra.getIdCompra());
        holder.fechaCompra.setText("Fecha: " + compra.getFechaCompra());
        holder.metodoPago.setText("Método de pago: " + compra.getMetodoPago());
        holder.estadoCompra.setText("Estado: " + (compra.isEstadoCompra() ? "Pendiente" : "Completada"));
        holder.numProdTot.setText("Total de productos: " + compra.getNumProdTot());
        holder.costTot.setText("Costo total: $" + compra.getCostTot());

        // Establecer el icono dependiendo del estado
        if (compra.isEstadoCompra()) {
            holder.iconoEstado.setImageResource(R.drawable.icon_check_circle);
        } else {
            holder.iconoEstado.setImageResource(R.drawable.icon_cancel_circle); // Cambiar por el icono correspondiente
        }

        // Establecer el onClickListener
        holder.itemView.setOnClickListener(v -> listener.onItemClick(compra));
    }

    @Override
    public int getItemCount() {
        return listaCompras.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView idCompra, fechaCompra, metodoPago, estadoCompra, numProdTot, costTot;
        ImageView iconoEstado;

        public ViewHolder(View itemView) {
            super(itemView);
            idCompra = itemView.findViewById(R.id.plantilla_gestion_paciente_compras_rv_idCompra);
            fechaCompra = itemView.findViewById(R.id.plantilla_gestion_paciente_compras_rv_fechaCompra);
            metodoPago = itemView.findViewById(R.id.plantilla_gestion_paciente_compras_rv_metodoPago);
            estadoCompra = itemView.findViewById(R.id.plantilla_gestion_paciente_compras_rv_estadoCompra);
            numProdTot = itemView.findViewById(R.id.plantilla_gestion_paciente_compras_rv_numProdTot);
            costTot = itemView.findViewById(R.id.plantilla_gestion_paciente_compras_rv_costTot);
            iconoEstado = itemView.findViewById(R.id.plantilla_gestion_paciente_compras_rv_iconoEstado);
        }
    }
}
