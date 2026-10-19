package com.sigepa.model;

import java.math.BigDecimal;

/**
 * Abstracción de un movimiento de stock.
 * Define los datos comunes y delega el cálculo del nuevo stock a las subclases.
 */
public abstract class MovimientoStock {

    private final int idMaterial;
    private final int idUsuario;
    private final BigDecimal cantidad;
    private final String motivo;

    protected MovimientoStock(int idMaterial, int idUsuario,
                              BigDecimal cantidad, String motivo) {
        if (idMaterial <= 0) {
            throw new IllegalArgumentException("El ID del material debe ser mayor a cero.");
        }
        if (idUsuario <= 0) {
            throw new IllegalArgumentException("El ID del usuario debe ser mayor a cero.");
        }
        if (cantidad == null || cantidad.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
        }
        if (motivo == null || motivo.trim().isEmpty()) {
            throw new IllegalArgumentException("El motivo es obligatorio.");
        }
        this.idMaterial = idMaterial;
        this.idUsuario = idUsuario;
        this.cantidad = cantidad;
        this.motivo = motivo.trim();
    }

    public abstract BigDecimal calcularNuevoStock(BigDecimal stockActual);

    public abstract String getTipo();

    public int getIdMaterial() {
        return idMaterial;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public String getMotivo() {
        return motivo;
    }
}
