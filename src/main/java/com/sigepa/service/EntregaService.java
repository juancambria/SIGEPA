package com.sigepa.service;

import com.sigepa.dao.EntregaDAO;
import com.sigepa.dao.PedidoDAO;
import com.sigepa.model.Entrega;

import java.util.List;

public class EntregaService {

    private final EntregaDAO entregaDAO =
            new EntregaDAO();

    private final PedidoDAO pedidoDAO =
            new PedidoDAO();

    public void registrarEntrega(
            int idPedido,
            String responsable,
            String observaciones
    ) throws Exception {

        String estado =
                pedidoDAO.obtenerEstado(
                        idPedido
                );

        if (estado == null) {

            throw new IllegalArgumentException(
                    "El pedido no existe."
            );
        }

        if (!estado.equals("TERMINADO")) {

            throw new IllegalArgumentException(
                    "Solo pueden entregarse pedidos TERMINADOS."
            );
        }

        entregaDAO.registrar(
                idPedido,
                responsable,
                observaciones
        );

        pedidoDAO.actualizarEstado(
                idPedido,
                "ENTREGADO"
        );
    }

    public List<Entrega> listar()
            throws Exception {

        return entregaDAO.listar();
    }
}