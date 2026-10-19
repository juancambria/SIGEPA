package com.sigepa.model;

import java.math.BigDecimal;

/** Representa un egreso y valida que haya stock suficiente. */
public class EgresoStock extends MovimientoStock {

    public EgresoStock(int idMaterial, int idUsuario,
                       BigDecimal cantidad, String motivo) {
        super(idMaterial, idUsuario, cantidad, motivo);
    }

    @Override
    public BigDecimal calcularNuevoStock(BigDecimal stockActual) {
        if (stockActual == null || stockActual.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El stock actual no puede ser nulo ni negativo.");
        }
        BigDecimal nuevoStock = stockActual.subtract(getCantidad());
        if (nuevoStock.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Stock insuficiente.");
        }
        return nuevoStock;
    }

    @Override
    public String getTipo() {
        return "EGRESO";
    }
}
