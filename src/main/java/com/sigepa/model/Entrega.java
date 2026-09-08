package com.sigepa.model;

import java.time.LocalDateTime;

public class Entrega {

    private int idEntrega;
    private int idPedido;

    private LocalDateTime fecha;

    private String responsable;
    private String estado;
    private String observaciones;

    public Entrega() {
    }

    public int getIdEntrega() {
        return idEntrega;
    }

    public void setIdEntrega(int idEntrega) {
        this.idEntrega = idEntrega;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
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

    @Override
    public String toString() {

        return "Entrega #"
                + idEntrega
                + " | Pedido #"
                + idPedido
                + " | "
                + estado;
    }
}