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

    public void registrarEntregaTransaccional(
            int idPedido,
            String responsable,
            String observaciones
    ) throws SQLException {

        try (Connection connection =
                     DatabaseConnection.getConnection()) {

            connection.setAutoCommit(false);

            try {

                // Bloquear y consultar el pedido
                String sqlEstado =
                        "SELECT estado FROM pedidos "
                                + "WHERE id_pedido = ? FOR UPDATE";

                String estado;

                try (PreparedStatement statement =
                             connection.prepareStatement(sqlEstado)) {

                    statement.setInt(1, idPedido);

                    try (ResultSet resultado =
                                 statement.executeQuery()) {

                        if (!resultado.next()) {
                            throw new IllegalArgumentException(
                                    "El pedido no existe."
                            );
                        }

                        estado = resultado.getString("estado");
                    }
                }

                // Validar el estado del pedido
                if (!"TERMINADO".equals(estado)) {
                    throw new IllegalArgumentException(
                            "Solo pueden entregarse pedidos TERMINADOS."
                    );
                }

                // Comprobar que no exista otra entrega
                String sqlExiste =
                        "SELECT COUNT(*) FROM entregas "
                                + "WHERE id_pedido = ?";

                try (PreparedStatement statement =
                             connection.prepareStatement(sqlExiste)) {

                    statement.setInt(1, idPedido);

                    try (ResultSet resultado =
                                 statement.executeQuery()) {

                        resultado.next();

                        if (resultado.getInt(1) > 0) {
                            throw new IllegalArgumentException(
                                    "El pedido ya tiene una entrega registrada."
                            );
                        }
                    }
                }

                // Registrar la entrega
                String sqlEntrega =
                        "INSERT INTO entregas "
                                + "(id_pedido, fecha, responsable, estado, observaciones) "
                                + "VALUES (?, NOW(), ?, 'ENTREGADO', ?)";

                try (PreparedStatement statement =
                             connection.prepareStatement(sqlEntrega)) {

                    statement.setInt(1, idPedido);
                    statement.setString(2, responsable);
                    statement.setString(3, observaciones);

                    if (statement.executeUpdate() != 1) {
                        throw new SQLException(
                                "No se pudo registrar la entrega."
                        );
                    }
                }

                // Actualizar el pedido
                String sqlPedido =
                        "UPDATE pedidos SET estado = 'ENTREGADO' "
                                + "WHERE id_pedido = ? AND estado = 'TERMINADO'";

                try (PreparedStatement statement =
                             connection.prepareStatement(sqlPedido)) {

                    statement.setInt(1, idPedido);

                    if (statement.executeUpdate() != 1) {
                        throw new SQLException(
                                "No se pudo actualizar el pedido."
                        );
                    }
                }

                // Confirmar ambas operaciones
                connection.commit();

            } catch (Exception ex) {
                connection.rollback();
                throw ex;
            }
        }
    }

}