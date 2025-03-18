package com.example.healthysmile.controller;

import java.util.List;

public interface EspecialistaResponseListenerInicio {
    void onResponseInicio(List<String> nombres, List<String> especialidades, List<String> descripciones, List<Long> idsEspecialista, List<String> cedulasProfesional,List<String>fotos);
    void onErrorInicio(String error);
}
