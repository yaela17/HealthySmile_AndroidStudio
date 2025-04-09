package com.example.healthysmile.model.requests;

public class AgregarProductoCarritoRequest {
    private Integer idUsuario;
    private Integer numProd;
    private Double costoProd;
    private Integer idProd;
    private Integer idCarritoCompra;


    public AgregarProductoCarritoRequest(Double costoProd, Integer idCarritoCompra, Integer idProd, Integer idUsuario, Integer numProd) {
        this.costoProd = costoProd;
        this.idCarritoCompra = idCarritoCompra;
        this.idProd = idProd;
        this.idUsuario = idUsuario;
        this.numProd = numProd;
    }

    public Double getCostoProd() {
        return costoProd;
    }

    public void setCostoProd(Double costoProd) {
        this.costoProd = costoProd;
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
