package com.sigepa.service;

import com.sigepa.dao.PedidoDAO;
import com.sigepa.dao.ProduccionDAO;
import com.sigepa.model.OrdenProduccion;

import java.util.List;

public class ProduccionService {

    private final ProduccionDAO produccionDAO =
            new ProduccionDAO();

    private final PedidoDAO pedidoDAO =
            new PedidoDAO();

    public void generarOrden(
            int idPedido,
            String observaciones
    ) throws Exception {

        String estado =
                pedidoDAO.obtenerEstado(
                        idPedido
                );

        if (estado == null) {

            throw new IllegalArgumentException(
                    "Pedido inexistente."
            );
        }

        if (!estado.equals("CONFIRMADO")) {

            throw new IllegalArgumentException(
                    "El pedido debe estar CONFIRMADO."
            );
        }

        produccionDAO.crearOrden(
                idPedido,
                observaciones
        );

        pedidoDAO.actualizarEstado(
                idPedido,
                "EN PRODUCCION"
        );
    }

    public List<OrdenProduccion> listar()
            throws Exception {

        return produccionDAO.listar();
    }

    public void finalizarOrden(
            int idOrden,
            int idPedido
    ) throws Exception {

        produccionDAO.finalizarOrden(
                idOrden
        );

        pedidoDAO.actualizarEstado(
                idPedido,
                "TERMINADO"
        );
    }
}