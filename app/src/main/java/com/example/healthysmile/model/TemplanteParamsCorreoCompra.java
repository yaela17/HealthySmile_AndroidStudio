package com.example.healthysmile.model;

import java.util.List;

public class TemplanteParamsCorreoCompra {
    private String user_name;
    private String user_email;

    // Solo se usa si is_producto == true
    private List<ItemCarrito> carrito; // Ahora carrito contiene el producto si es producto
    private double total;

    // Getters y Setters
    public String getUser_name() { return user_name; }
    public void setUser_name(String user_name) { this.user_name = user_name; }

    public String getUser_email() { return user_email; }
    public void setUser_email(String user_email) { this.user_email = user_email; }

    public List<ItemCarrito> getCarrito() { return carrito; }
    public void setCarrito(List<ItemCarrito> carrito) { this.carrito = carrito; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
}
