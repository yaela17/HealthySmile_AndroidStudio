package com.example.healthysmile.controller.consulta;

import java.util.List;

public interface ObtenerCitasPorDiaResponseListener {
    void onResponse(List<Long> idsCita, List<String> motivosCita, List<Long> idsEspecialista, List<String> fechasCita);
    void onError(String error);
}

