package com.example.healthysmile.controller.consulta;

import java.util.List;

public interface EspecialistaResponseListenerChat {
    void onResponse(List<String> nombres, List<String> especialidades, List<String> descripciones, List<Long> idsEspecialista);
    void onError(String error);
}
