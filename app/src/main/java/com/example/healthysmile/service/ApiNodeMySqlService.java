package com.example.healthysmile.service;

import com.example.healthysmile.controller.APINodeMySqlRespuestaMensaje;
import com.example.healthysmile.controller.ApiNodeMySqlRespuesta;
import com.example.healthysmile.controller.CedulaResultado;
import com.example.healthysmile.model.TemplanteParamsCorreoCompra;
import com.example.healthysmile.model.TemplateParams;
import com.example.healthysmile.model.entities.Especialista;
import com.example.healthysmile.model.entities.Producto;
import com.example.healthysmile.model.entities.Usuario;
import com.example.healthysmile.model.relationships.RelProdCarritoUser;
import com.example.healthysmile.model.requests.ActualizarDescripcionRequest;
import com.example.healthysmile.model.requests.ActualizarFotoPerfilRequest;
import com.example.healthysmile.model.requests.ActualizarNombreRequest;
import com.example.healthysmile.model.requests.AgregarProductoCarritoRequest;
import com.example.healthysmile.model.requests.CrearCargoRequest;
import com.example.healthysmile.model.requests.CrearCitaRequest;
import com.example.healthysmile.model.requests.CrearCompraRequest;
import com.example.healthysmile.model.requests.CrearPreguntaFrecuenteRequest;
import com.example.healthysmile.model.responses.ObtenerCarritosCompraResponse;
import com.example.healthysmile.model.responses.ProductoCompra;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ApiNodeMySqlService {
    @POST("crearPaciente")
    Call<ApiNodeMySqlRespuesta> crearPaciente(@Body Usuario paciente);

    @POST("crearEspecialista")
    Call<ApiNodeMySqlRespuesta> crearEspecialista(@Body Especialista especialista);

    @POST("crearPregunta")
    Call<ApiNodeMySqlRespuesta> crearPreguntaFrecuente(@Body CrearPreguntaFrecuenteRequest crearPreguntaFrecuenteRequest);

    @POST("crearCita")
    Call<ApiNodeMySqlRespuesta> crearCita(@Body CrearCitaRequest crearCitaRequest);

    @POST("verificarCorreo")
    Call<ApiNodeMySqlRespuesta> verificarCorreo(@Query("correoUser") String correo);

    @POST("enviarCorreoVerificacion")
    Call<ApiNodeMySqlRespuesta> enviarCorreoVerificacion(@Body TemplateParams templateParams);

    @POST("actualizarFotoPerfil")
    Call<ApiNodeMySqlRespuesta> actualizarFotoPerfil(@Body ActualizarFotoPerfilRequest actualizarFotoPerfilRequest);

    @POST("agregarProducto")
    Call<ApiNodeMySqlRespuesta> agregarProducto(@Body Producto producto);

    @POST("deshabilitarProducto")
    Call<ApiNodeMySqlRespuesta> deshabilitarProducto(@Body Producto producto);

    @POST("actualizarProducto")
    Call<ApiNodeMySqlRespuesta> actualizarProducto(@Body Producto producto);

    @POST("agregarProductoCarrito")
    Call<ApiNodeMySqlRespuesta> agregarProductoCarrito(@Body AgregarProductoCarritoRequest agregarProductoCarritoRequest);

    @PUT("actualizarProductoCarrito")
    Call<ApiNodeMySqlRespuesta> actualizarProductoCarrito(@Body RelProdCarritoUser relProdCarritoUser);

    @DELETE("eliminarProductoCarrito")
    Call<ApiNodeMySqlRespuesta> eliminarProductoCarrito(
            @Query("idUsuario") String idUsuario,
            @Query("idProd") String idProd,
            @Query("idCarritoCompra") String idCarritoCompra
    );

    @GET("buscarCedulaProfesional")
    Call<List<CedulaResultado>> verificarCedula(
            @Query("nombre")String nombre,
            @Query("primerApellido") String primerApellido,
            @Query("segundoApellido") String segundoApellido
    );

    @POST("crearCargo")
    Call<ApiNodeMySqlRespuesta> crearCargo(@Body CrearCargoRequest crearCargoRequest);

    @POST("enviarCorreoCompra")
    Call<ApiNodeMySqlRespuesta> enviarCorreoCompra(@Body TemplanteParamsCorreoCompra templateParams);

    @POST("crearCompra")
    Call<APINodeMySqlRespuestaMensaje> crearCompra(@Body CrearCompraRequest crearCompraRequest);

    @GET("obtenerCarritosCompra")
    Call<List<ObtenerCarritosCompraResponse>> obtenerCarritosCompra(@Query("idUsuario") Integer idUsuario);

    @GET("obtenerCompra")
    Call<List<ProductoCompra>> obtenerCompra(@Query("idCompra") Integer idCompra);

    @POST("actualizarNombre")
    Call<ApiNodeMySqlRespuesta> actualizarNombre(@Body ActualizarNombreRequest usuario);

    @POST("actualizarDescripcion")
    Call<ApiNodeMySqlRespuesta> actualizarDescripcion(@Body ActualizarDescripcionRequest usuario);

}
