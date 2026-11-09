
package com.sigepa.service;

import com.sigepa.dao.ProduccionDAO;
import com.sigepa.model.OrdenProduccion;

import java.util.List;

public class ProduccionService {

    private final ProduccionDAO produccionDAO =
            new ProduccionDAO();

    // Generar una orden de producción
    public void generarOrden(
            int idPedido,
            String observaciones
    ) throws Exception {

        if (idPedido <= 0) {
            throw new IllegalArgumentException(
                    "El ID del pedido debe ser válido."
            );
        }

        produccionDAO.generarOrdenTransaccional(
                idPedido,
                observaciones
        );
    }

    // Consultar las órdenes de producción
    public List<OrdenProduccion> listar()
            throws Exception {

        return produccionDAO.listar();
    }

    // Finalizar una orden de producción
    public void finalizarOrden(
            int idOrden,
            int idPedido
    ) throws Exception {

        if (idOrden <= 0 || idPedido <= 0) {
            throw new IllegalArgumentException(
                    "Los identificadores deben ser válidos."
            );
        }

        produccionDAO.finalizarOrdenTransaccional(
                idOrden,
                idPedido
        );
    }
}
