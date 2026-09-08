package com.sigepa.service;

import com.sigepa.dao.UsuarioDAO;
import com.sigepa.model.Usuario;

public class AuthService {

    private final UsuarioDAO usuarioDAO =
            new UsuarioDAO();

    public Usuario login(
            String usuario,
            String password
    ) throws Exception {

        if (
                usuario == null
                        || usuario.isBlank()
                        || password == null
                        || password.isBlank()
        ) {

            throw new IllegalArgumentException(
                    "Usuario y contraseña obligatorios."
            );
        }

        return usuarioDAO.autenticar(
                usuario.trim(),
                password
        );
    }
}