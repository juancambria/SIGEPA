package com.sigepa.service;

import com.sigepa.dao.PedidoDAO;
import com.sigepa.model.Pedido;

import java.util.List;

public class PedidoService {

    private final PedidoDAO pedidoDAO =
            new PedidoDAO();

    public int registrar(
            Pedido pedido
    ) throws Exception {

        if (
                pedido.getIdCliente() <= 0
        ) {

            throw new IllegalArgumentException(
                    "Debe seleccionar un cliente."
            );
        }

        if (
                pedido.getDetalles() == null
                        || pedido.getDetalles().isEmpty()
        ) {

            throw new IllegalArgumentException(
                    "El pedido debe tener al menos un producto."
            );
        }

        pedido.calcularTotal();

        pedido.setEstado(
                "PENDIENTE"
        );

        return pedidoDAO.guardar(
                pedido
        );
    }

    public List<Pedido> listar()
            throws Exception {

        return pedidoDAO.listar();
    }

    public void confirmarPedido(
            int idPedido
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

        if (!estado.equals("PENDIENTE")) {

            throw new IllegalArgumentException(
                    "Solo pueden confirmarse pedidos pendientes."
            );
        }

        pedidoDAO.actualizarEstado(
                idPedido,
                "CONFIRMADO"
        );
    }
}