package com.example.healthysmile.service;
import com.example.healthysmile.BuildConfig;

public class URLSApisNode {
    public static final String BASE_URL_APIs_Node = BuildConfig.API_BASE_URL;

    //Modulo Iniciar Sesion
    public static final String URL_LOG_IN = BASE_URL_APIs_Node + "LogInUsuario";

    public static final String URL_OBTENER_CITA_POR_FECHA = BASE_URL_APIs_Node + "obtenerCitaPorFecha";
    public static final String URL_ELIMINAR_CITA = BASE_URL_APIs_Node + "eliminarCita";
    public static final String URL_ACTUALIZAR_CITA = BASE_URL_APIs_Node + "modificarCita";
    public static final String URL_OBTENER_CITAS_DIA = BASE_URL_APIs_Node + "obtenerCitasPorDia";
    public static final String URL_OBTENER_CITAS_PACIENTE = BASE_URL_APIs_Node + "obtenerCitasPaciente";
    public static final String URL_OBTENER_CITAS_ESPECIALISTA = BASE_URL_APIs_Node + "obtenerCitasEspecialista";

    public static final String URL_BUSCAR_PREGUNTA_FRECUENTE = BASE_URL_APIs_Node + "buscarPregunta";
    public static final String URL_OBTENER_PREGUNTAS_FRECUENTES = BASE_URL_APIs_Node + "obtenerPreguntas";
    public static final String URL_OBTENER_ESPECIALISTAS = BASE_URL_APIs_Node + "obtenerEspecialistasChatAndroid";
    public static final String URL_OBTENER_PACIENTES = BASE_URL_APIs_Node + "obtenerPacientesChatAndroid";
    public static final String URL_RESPONDER_PREGUNTA_FRECUENTE = BASE_URL_APIs_Node + "responderPregunta";
    public static final String URL_AUMENTAR_BUSQUEDAS_PREGUNTA = BASE_URL_APIs_Node + "aumentarBusquedas";

    //Productos
    public static final String URL_OBTENER_PRODUCTOS = BASE_URL_APIs_Node + "obtenerProductos";
    public static final String URL_BUSCAR_PRODUCTO_POR_NOMBRE = BASE_URL_APIs_Node + "buscarProductoPorNombre";
    public static final String URL_OBTENER_CARRITO_COMPRA = BASE_URL_APIs_Node + "obtenerCarritoCompra";

}
