package com.sigepa.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Pedido {

    private int idPedido;
    private int idCliente;
    private String nombreCliente;

    private LocalDateTime fecha;
    private String estado;
    private String observaciones;

    private BigDecimal total;

    private List<DetallePedido> detalles;

    public Pedido() {

        detalles = new ArrayList<>();

        estado = "PENDIENTE";

        total = BigDecimal.ZERO;
    }

    public void agregarDetalle(DetallePedido detalle) {

        detalles.add(detalle);

        calcularTotal();
    }

    public void calcularTotal() {

        total = BigDecimal.ZERO;

        for (DetallePedido detalle : detalles) {

            total = total.add(
                    detalle.getSubtotal()
            );
        }
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<DetallePedido> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallePedido> detalles) {
        this.detalles = detalles;
    }

    @Override
    public String toString() {

        return "Pedido #"
                + idPedido
                + " | Cliente: "
                + nombreCliente
                + " | Estado: "
                + estado
                + " | Total: $"
                + total;
    }
}