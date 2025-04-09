package com.example.healthysmile.model.requests;

public class CrearCompraRequest {
    private final int idCarritoCompra;
    private final String fechaCompra;
    private final String metodoPago;
    private final boolean peticion;
    private final int idProducto;
    private final int cantidadProducto;
    private final int idUsuario;


    //Caso con Carrito
    public CrearCompraRequest(String fechaCompra, int idCarritoCompra, String metodoPago, boolean peticion) {
        this.cantidadProducto = 0;
        this.fechaCompra = fechaCompra;
        this.idCarritoCompra = idCarritoCompra;
        this.idProducto = -1;
        this.idUsuario = -1;
        this.metodoPago = metodoPago;
        this.peticion = peticion;
    }

    //Caso solo un producto
    public CrearCompraRequest(boolean peticion, String metodoPago, String fechaCompra, int idProducto, int idUsuario, int cantidadProducto) {
        this.peticion = peticion;
        this.metodoPago = metodoPago;
        this.idUsuario = idUsuario;
        this.idProducto = idProducto;
        this.idCarritoCompra = -1;
        this.fechaCompra = fechaCompra;
        this.cantidadProducto = cantidadProducto;
    }
}
