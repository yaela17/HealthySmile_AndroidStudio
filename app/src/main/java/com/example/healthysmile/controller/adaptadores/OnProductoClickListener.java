package com.example.healthysmile.controller.adaptadores;

public interface OnProductoClickListener {
    void onProductoClick(int position, long idProducto,String nombreProducto, long numerosProducto, String descripcionProducto, double costoProducto, String urlImagenProducto, boolean disponibleProducto,Integer comprasProducto);
}
