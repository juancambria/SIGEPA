package com.sigepa.dao;

import com.sigepa.model.Cliente;
import com.sigepa.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public void guardar(
            Cliente cliente
    ) throws SQLException {

        String sql =
                "INSERT INTO clientes "
                        + "(nombre, documento, telefono, email, direccion, activo) "
                        + "VALUES (?, ?, ?, ?, ?, TRUE)";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    cliente.getNombre()
            );

            statement.setString(
                    2,
                    cliente.getDocumento()
            );

            statement.setString(
                    3,
                    cliente.getTelefono()
            );

            statement.setString(
                    4,
                    cliente.getEmail()
            );

            statement.setString(
                    5,
                    cliente.getDireccion()
            );

            statement.executeUpdate();
        }
    }

    public List<Cliente> listar()
            throws SQLException {

        List<Cliente> clientes =
                new ArrayList<>();

        String sql =
                "SELECT * FROM clientes "
                        + "WHERE activo = TRUE "
                        + "ORDER BY nombre";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                Cliente cliente =
                        new Cliente();

                cliente.setIdCliente(
                        resultSet.getInt(
                                "id_cliente"
                        )
                );

                cliente.setNombre(
                        resultSet.getString(
                                "nombre"
                        )
                );

                cliente.setDocumento(
                        resultSet.getString(
                                "documento"
                        )
                );

                cliente.setTelefono(
                        resultSet.getString(
                                "telefono"
                        )
                );

                cliente.setEmail(
                        resultSet.getString(
                                "email"
                        )
                );

                cliente.setDireccion(
                        resultSet.getString(
                                "direccion"
                        )
                );

                cliente.setActivo(
                        resultSet.getBoolean(
                                "activo"
                        )
                );

                clientes.add(cliente);
            }
        }

        return clientes;
    }
}