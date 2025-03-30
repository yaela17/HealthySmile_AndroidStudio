package com.example.healthysmile.model.entities;

public class Producto {
    private Integer idProd;
    private String nombreProd;
    private Integer numProd;
    private String descriProd;
    private Double costoProd;
    private Integer compras;
    private String imagen;
    private boolean disponible;

    public Producto() {
    }

    public Producto(String nombreProd,Integer compras, Double costoProd, String descriProd, Integer idProd, String imagen, Integer numProd,boolean disponible) {
        this.nombreProd = nombreProd;
        this.compras = compras;
        this.costoProd = costoProd;
        this.descriProd = descriProd;
        this.idProd = idProd;
        this.imagen = imagen;
        this.numProd = numProd;
        this.disponible = disponible;
    }

    public String getNombreProd() {
        return nombreProd;
    }

    public void setNombreProd(String nombreProd) {
        this.nombreProd = nombreProd;
    }

    public Integer getCompras() {
        return compras;
    }

    public void setCompras(Integer compras) {
        this.compras = compras;
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

    public Integer getIdProd() {
        return idProd;
    }

    public void setIdProd(Integer idProd) {
        this.idProd = idProd;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Integer getNumProd() {
        return numProd;
    }

    public void setNumProd(Integer numProd) {
        this.numProd = numProd;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "compras=" + compras +
                ", idProd=" + idProd +
                ", nombreProd='" + nombreProd + '\'' +
                ", numProd=" + numProd +
                ", descriProd='" + descriProd + '\'' +
                ", costoProd=" + costoProd +
                ", imagen='" + imagen + '\'' +
                ", disponible=" + disponible +
                '}';
    }
}
