package com.example.healthysmile.controller.ayudaYSoporte;

import java.util.List;

public interface ObtenerPreguntasFrecuentesResponse {
    void onResponse(List<Long> idsPreguntasFrecuentes,List<String> preguntas, List<String> respuestas, List<Long> idsUsuarios, List<Long> idsEspecialistas);
    void onError(String error);
}
