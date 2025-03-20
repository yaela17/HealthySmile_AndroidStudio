package com.example.healthysmile.controller.consulta;

import java.util.List;

public interface EspecialistaResponseListenerSpinnerCitas {
    void onResponseSpinnerCitas(List<String> nombres, List<String> especialidades, List<Long> idsEspecialistas);
    void onErrorSpinnerCitas(String error);
}
