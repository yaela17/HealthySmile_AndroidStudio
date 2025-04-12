package com.example.healthysmile.model.responses;

public class ProductoCompra {
    private String nombreProd;
    private String descriProd;
    private Double costoProd;
    private String imagen;
    private Integer numProd;

    public ProductoCompra(Double costoProd, String descriProd, String imagen, String nombreProd, Integer numProd) {
        this.costoProd = costoProd;
        this.descriProd = descriProd;
        this.imagen = imagen;
        this.nombreProd = nombreProd;
        this.numProd = numProd;
    }

    public Double getCostoProd() {
        return costoProd;
    }

    public void setCostoProd(Double costoProd) {
        this.costoProd = costoProd;
    }

    public String getDescriProd() {
        return descriProd;
    }

    public void setDescriProd(String descriProd) {
        this.descriProd = descriProd;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getNombreProd() {
        return nombreProd;
    }

    public void setNombreProd(String nombreProd) {
        this.nombreProd = nombreProd;
    }

    public Integer getNumProd() {
        return numProd;
    }

    public void setNumProd(Integer numProd) {
        this.numProd = numProd;
    }
}
