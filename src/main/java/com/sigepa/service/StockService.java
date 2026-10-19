package com.sigepa.service;

import com.sigepa.dao.StockDAO;
import com.sigepa.model.StockItem;
import com.sigepa.model.MovimientoStock;
import com.sigepa.model.IngresoStock;
import com.sigepa.model.EgresoStock;

import java.math.BigDecimal;
import java.util.List;

public class StockService {

    private final StockDAO stockDAO =
            new StockDAO();

    public List<StockItem> listar()
            throws Exception {

        return stockDAO.listar();
    }

    public void registrarMovimiento(
            int idMaterial,
            int idUsuario,
            String tipo,
            BigDecimal cantidad,
            String motivo
    ) throws Exception {

        if (tipo == null) {
            throw new IllegalArgumentException(
                    "El tipo debe ser INGRESO o EGRESO."
            );
        }

        MovimientoStock movimiento;

        if (tipo.equalsIgnoreCase("INGRESO")) {
            movimiento = new IngresoStock(idMaterial, idUsuario, cantidad, motivo);
        } else if (tipo.equalsIgnoreCase("EGRESO")) {
            movimiento = new EgresoStock(idMaterial, idUsuario, cantidad, motivo);
        } else {
            throw new IllegalArgumentException(
                    "El tipo debe ser INGRESO o EGRESO."
            );
        }

        stockDAO.registrarMovimiento(movimiento);
    }
}
