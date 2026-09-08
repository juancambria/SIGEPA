package com.sigepa.model;

import java.math.BigDecimal;

public class StockItem {

    private int idStock;
    private int idMaterial;

    private String material;

    private BigDecimal cantidad;

    private String ubicacion;

    private BigDecimal stockMinimo;

    public StockItem() {
    }

    public boolean necesitaReposicion() {

        return cantidad.compareTo(stockMinimo) <= 0;
    }

    public int getIdStock() {
        return idStock;
    }

    public void setIdStock(int idStock) {
        this.idStock = idStock;
    }

    public int getIdMaterial() {
        return idMaterial;
    }

    public void setIdMaterial(int idMaterial) {
        this.idMaterial = idMaterial;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public BigDecimal getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(BigDecimal stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    @Override
    public String toString() {

        return idMaterial
                + " | "
                + material
                + " | Stock: "
                + cantidad
                + " | Mínimo: "
                + stockMinimo
                + " | "
                + ubicacion;
    }
}