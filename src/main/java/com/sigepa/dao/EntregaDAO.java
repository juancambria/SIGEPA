package com.sigepa.dao;

import com.sigepa.model.Entrega;
import com.sigepa.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EntregaDAO {

    public void registrar(
            int idPedido,
            String responsable,
            String observaciones
    ) throws SQLException {

        String sql =
                "INSERT INTO entregas "
                        + "(id_pedido, fecha, responsable, estado, observaciones) "
                        + "VALUES (?, NOW(), ?, 'ENTREGADO', ?)";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    idPedido
            );

            statement.setString(
                    2,
                    responsable
            );

            statement.setString(
                    3,
                    observaciones
            );

            statement.executeUpdate();
        }
    }

    public List<Entrega> listar()
            throws SQLException {

        List<Entrega> entregas =
                new ArrayList<>();

        String sql =
                "SELECT * FROM entregas "
                        + "ORDER BY id_entrega DESC";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                Entrega entrega =
                        new Entrega();

                entrega.setIdEntrega(
                        resultSet.getInt(
                                "id_entrega"
                        )
                );

                entrega.setIdPedido(
                        resultSet.getInt(
                                "id_pedido"
                        )
                );

                Timestamp fecha =
                        resultSet.getTimestamp(
                                "fecha"
                        );

                if (fecha != null) {

                    entrega.setFecha(
                            fecha.toLocalDateTime()
                    );
                }

                entrega.setResponsable(
                        resultSet.getString(
                                "responsable"
                        )
                );

                entrega.setEstado(
                        resultSet.getString(
                                "estado"
                        )
                );

                entrega.setObservaciones(
                        resultSet.getString(
                                "observaciones"
                        )
                );

                entregas.add(entrega);
            }
        }

        return entregas;
    }
}