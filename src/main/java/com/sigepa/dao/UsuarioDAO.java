package com.sigepa.dao;

import com.sigepa.model.Usuario;
import com.sigepa.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    public Usuario autenticar(
            String usuario,
            String password
    ) throws SQLException {

        String sql =
                "SELECT * FROM usuarios "
                        + "WHERE usuario = ? "
                        + "AND password = ? "
                        + "AND activo = TRUE";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    usuario
            );

            statement.setString(
                    2,
                    password
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (resultSet.next()) {

                    Usuario usuarioEncontrado =
                            new Usuario();

                    usuarioEncontrado.setIdUsuario(
                            resultSet.getInt(
                                    "id_usuario"
                            )
                    );

                    usuarioEncontrado.setIdRol(
                            resultSet.getInt(
                                    "id_rol"
                            )
                    );

                    usuarioEncontrado.setNombre(
                            resultSet.getString(
                                    "nombre"
                            )
                    );

                    usuarioEncontrado.setApellido(
                            resultSet.getString(
                                    "apellido"
                            )
                    );

                    usuarioEncontrado.setUsuario(
                            resultSet.getString(
                                    "usuario"
                            )
                    );

                    usuarioEncontrado.setActivo(
                            resultSet.getBoolean(
                                    "activo"
                            )
                    );

                    return usuarioEncontrado;
                }
            }
        }

        return null;
    }
}