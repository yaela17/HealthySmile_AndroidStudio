package com.example.healthysmile.controller.tiendaVirtual;

import java.util.List;

public interface ObtenerProductosResponseListener {
    void onObtencionExitosa(List<Long> idsProducto, List<String> nombresProd, List<Long> numerosProd, List<String> descripcionesProd, List<Double> costosProd, List<Integer> compras, List<String> urlsImagen,List<Boolean> disponibles);
    void onError(String error);
}
