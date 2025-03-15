package com.example.healthysmile.model.relationships;

public class RelProdCarrito {
    private Integer idRelProdCarrito;
    private Integer idProd;
    private Integer idCarritoCompra;

    public RelProdCarrito(Integer idCarritoCompra, Integer idProd, Integer idRelProdCarrito) {
        this.idCarritoCompra = idCarritoCompra;
        this.idProd = idProd;
        this.idRelProdCarrito = idRelProdCarrito;
    }
}
