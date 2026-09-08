package com.sigepa.service;

import com.sigepa.dao.StockDAO;
import com.sigepa.model.StockItem;

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

        if (
                cantidad.compareTo(
                        BigDecimal.ZERO
                ) <= 0
        ) {

            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor a cero."
            );
        }

        if (
                !tipo.equalsIgnoreCase("INGRESO")
                        && !tipo.equalsIgnoreCase("EGRESO")
        ) {

            throw new IllegalArgumentException(
                    "El tipo debe ser INGRESO o EGRESO."
            );
        }

        stockDAO.registrarMovimiento(
                idMaterial,
                idUsuario,
                tipo,
                cantidad,
                motivo
        );
    }
}