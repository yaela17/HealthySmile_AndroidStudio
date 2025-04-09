package com.example.healthysmile.model.requests;

public class CrearCargoRequest {
    private String token_id;
    private Double amount;
    private String descripcion;
    private String device_session_id;
    private Customer customer;

    public CrearCargoRequest() {}

    public CrearCargoRequest(String token_id, Double amount, String descripcion, String device_session_id, Customer customer) {
        this.token_id = token_id;
        this.amount = amount;
        this.descripcion = descripcion;
        this.device_session_id = device_session_id;
        this.customer = customer;
    }

    public String getToken_id() {
        return token_id;
    }

    public void setToken_id(String token_id) {
        this.token_id = token_id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDevice_session_id() {
        return device_session_id;
    }

    public void setDevice_session_id(String device_session_id) {
        this.device_session_id = device_session_id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
