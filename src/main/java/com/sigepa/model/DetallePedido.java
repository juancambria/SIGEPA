package com.sigepa.model;

import java.math.BigDecimal;

public class DetallePedido {

    private int idDetalle;
    private int idPedido;
    private int idProducto;
    private String nombreProducto;

    private BigDecimal cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;

    public DetallePedido() {
    }

    public DetallePedido(
            int idProducto,
            BigDecimal cantidad,
            BigDecimal precioUnitario
    ) {

        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;

        calcularSubtotal();
    }

    public void calcularSubtotal() {

        subtotal = cantidad.multiply(precioUnitario);
    }

    public int getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
}