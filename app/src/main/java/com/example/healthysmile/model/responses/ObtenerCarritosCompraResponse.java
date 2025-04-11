package com.example.healthysmile.model.responses;

public class ObtenerCarritosCompraResponse {
    private Integer idCompra;
    private Integer idCarritoCompra;
    private String fechaCompra;
    private String metodoPago;
    private boolean estadoCompra;
    private boolean peticion;
    private Integer numProdTot;
    private Double costTot;

    public ObtenerCarritosCompraResponse(Double costTot, boolean estadoCompra, String fechaCompra, Integer idCarritoCompra, Integer idCompra, String metodoPago, Integer numProdTot, boolean peticion) {
        this.costTot = costTot;
        this.estadoCompra = estadoCompra;
        this.fechaCompra = fechaCompra;
        this.idCarritoCompra = idCarritoCompra;
        this.idCompra = idCompra;
        this.metodoPago = metodoPago;
        this.numProdTot = numProdTot;
        this.peticion = peticion;
    }

    public Double getCostTot() {
        return costTot;
    }

    public void setCostTot(Double costTot) {
        this.costTot = costTot;
    }

    public boolean isEstadoCompra() {
        return estadoCompra;
    }

    public void setEstadoCompra(boolean estadoCompra) {
        this.estadoCompra = estadoCompra;
    }

    public String getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(String fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public Integer getIdCarritoCompra() {
        return idCarritoCompra;
    }

    public void setIdCarritoCompra(Integer idCarritoCompra) {
        this.idCarritoCompra = idCarritoCompra;
    }

    public Integer getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(Integer idCompra) {
        this.idCompra = idCompra;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public Integer getNumProdTot() {
        return numProdTot;
    }

    public void setNumProdTot(Integer numProdTot) {
        this.numProdTot = numProdTot;
    }

    public boolean isPeticion() {
        return peticion;
    }

    public void setPeticion(boolean peticion) {
        this.peticion = peticion;
    }
}
