package com.example.healthysmile.Modelo.relaciones;

public class RelCompraUser {
    private Integer idRelCompraUser;
    private Integer idCompra;
    private Integer idUsuario;

    public RelCompraUser(Integer idCompra, Integer idRelCompraUser, Integer idUsuario) {
        this.idCompra = idCompra;
        this.idRelCompraUser = idRelCompraUser;
        this.idUsuario = idUsuario;
    }
}
