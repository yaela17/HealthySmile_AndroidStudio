package com.example.healthysmile.controller.tiendaVirtual;

import java.util.List;

public interface ObtenerCarritoCompraResponseListener {
    void onObtencionExitosa(List<Long> idsProducto,
                            List<String> nombresProd,
                            List<Long> numerosProd,
                            List<String> descripcionesProd,
                            List<Double> costosProd,
                            List<String> urlsImagen,
                            List<Boolean> disponibles,
                            List<Integer> numerosProdDisponibles,
                            List<Integer> numProdTot,
                            List<Double> costTot);
    void onError(String error);
}
