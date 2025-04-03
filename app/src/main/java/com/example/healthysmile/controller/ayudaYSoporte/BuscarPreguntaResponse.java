package com.example.healthysmile.controller.ayudaYSoporte;

import java.util.List;

public interface BuscarPreguntaResponse {
    void onResponse(List<Long> idPreguntasFrecuentes, List<String> preguntas, List<String> respuestas, List<Long> idsUsuarios, List<Long> idsEspecialistas);
    void onPreguntasNoEncontradas(String mensaje);
    void onError(String error);
}
