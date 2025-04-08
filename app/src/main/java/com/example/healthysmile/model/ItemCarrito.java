package com.example.healthysmile.model;

public  class ItemCarrito {
    private String nombre;
    private int cantidad;
    private double precio;

    public ItemCarrito() {
    }

    public ItemCarrito(int cantidad, String nombre, double precio) {
        this.cantidad = cantidad;
        this.nombre = nombre;
        this.precio = precio;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
}