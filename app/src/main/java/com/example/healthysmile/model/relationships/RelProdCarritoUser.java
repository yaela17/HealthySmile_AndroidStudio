package com.example.healthysmile.model.relationships;

public class RelProdCarritoUser {
    private Integer idRelProdCarrito;
    private Integer idProd;
    private Integer idCarritoCompra;
    private Integer idUsuario;
    private Integer numProd;

    public RelProdCarritoUser(Integer idCarritoCompra, Integer idProd, Integer idRelProdCarrito, Integer idUsuario, Integer numProd) {
        this.idCarritoCompra = idCarritoCompra;
        this.idProd = idProd;
        this.idRelProdCarrito = idRelProdCarrito;
        this.idUsuario = idUsuario;
        this.numProd = numProd;
    }

    public RelProdCarritoUser() {
    }

    public Integer getIdCarritoCompra() {
        return idCarritoCompra;
    }

    public void setIdCarritoCompra(Integer idCarritoCompra) {
        this.idCarritoCompra = idCarritoCompra;
    }

    public Integer getIdProd() {
        return idProd;
    }

    public void setIdProd(Integer idProd) {
        this.idProd = idProd;
    }

    public Integer getIdRelProdCarrito() {
        return idRelProdCarrito;
    }

    public void setIdRelProdCarrito(Integer idRelProdCarrito) {
        this.idRelProdCarrito = idRelProdCarrito;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getNumProd() {
        return numProd;
    }

    public void setNumProd(Integer numProd) {
        this.numProd = numProd;
    }
}
