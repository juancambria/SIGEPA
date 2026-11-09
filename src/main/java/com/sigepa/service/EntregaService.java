
package com.sigepa.service;

import com.sigepa.dao.EntregaDAO;
import com.sigepa.model.Entrega;

import java.util.List;

public class EntregaService {

    private final EntregaDAO entregaDAO =
            new EntregaDAO();

    // Registrar una entrega
    public void registrarEntrega(
            int idPedido,
            String responsable,
            String observaciones
    ) throws Exception {

        if (idPedido <= 0) {
            throw new IllegalArgumentException(
                    "El ID del pedido debe ser válido."
            );
        }

        if (responsable == null
                || responsable.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Debe ingresar el responsable de la entrega."
            );
        }

        entregaDAO.registrarEntregaTransaccional(
                idPedido,
                responsable.trim(),
                observaciones
        );
    }

    // Consultar entregas registradas
    public List<Entrega> listar()
            throws Exception {

        return entregaDAO.listar();
    }
}
