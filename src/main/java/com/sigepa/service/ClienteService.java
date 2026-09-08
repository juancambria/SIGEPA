package com.sigepa.service;

import com.sigepa.dao.ClienteDAO;
import com.sigepa.model.Cliente;

import java.util.List;

public class ClienteService {

    private final ClienteDAO clienteDAO =
            new ClienteDAO();

    public void registrar(
            Cliente cliente
    ) throws Exception {

        if (
                cliente.getNombre() == null
                        || cliente.getNombre().isBlank()
        ) {

            throw new IllegalArgumentException(
                    "El nombre es obligatorio."
            );
        }

        clienteDAO.guardar(
                cliente
        );
    }

    public List<Cliente> listar()
            throws Exception {

        return clienteDAO.listar();
    }
}