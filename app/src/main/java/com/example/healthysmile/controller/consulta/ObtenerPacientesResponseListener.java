package com.example.healthysmile.controller.consulta;

import java.util.List;

public interface ObtenerPacientesResponseListener {
    void onResponse(List<String> nombres, List<String> correos, List<Long> idsPacientes);
    void onError(String error);
}
