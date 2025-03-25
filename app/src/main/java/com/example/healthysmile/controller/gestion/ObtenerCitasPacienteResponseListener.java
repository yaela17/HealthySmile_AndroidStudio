package com.example.healthysmile.controller.gestion;

import java.util.List;

public interface ObtenerCitasPacienteResponseListener {
    void onResponse(List<Long> idsCitas, List<String> motivosCita, List<String> fechasCita,List<Long> idsEspecialistas, List<String> nombresEspecialistas);
    void onError(String error);
}