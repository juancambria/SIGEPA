package com.sigepa.model;

import java.math.BigDecimal;

/** Representa un ingreso y aumenta las existencias del material. */
public class IngresoStock extends MovimientoStock {

    public IngresoStock(int idMaterial, int idUsuario,
                        BigDecimal cantidad, String motivo) {
        super(idMaterial, idUsuario, cantidad, motivo);
    }

    @Override
    public BigDecimal calcularNuevoStock(BigDecimal stockActual) {
        validarStockActual(stockActual);
        return stockActual.add(getCantidad());
    }

    @Override
    public String getTipo() {
        return "INGRESO";
    }

    private void validarStockActual(BigDecimal stockActual) {
        if (stockActual == null || stockActual.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El stock actual no puede ser nulo ni negativo.");
        }
    }
}
