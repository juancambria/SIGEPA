package com.sigepa.dao;

import com.sigepa.model.OrdenProduccion;
import com.sigepa.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProduccionDAO {

    public void crearOrden(
            int idPedido,
            String observaciones
    ) throws SQLException {

        String sql =
                "INSERT INTO ordenes_produccion "
                        + "(id_pedido, fecha_inicio, estado, observaciones) "
                        + "VALUES (?, NOW(), 'EN PRODUCCION', ?)";

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
                    observaciones
            );

            statement.executeUpdate();
        }
    }

    public List<OrdenProduccion> listar()
            throws SQLException {

        List<OrdenProduccion> ordenes =
                new ArrayList<>();

        String sql =
                "SELECT * FROM ordenes_produccion "
                        + "ORDER BY id_orden DESC";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                OrdenProduccion orden =
                        new OrdenProduccion();

                orden.setIdOrden(
                        resultSet.getInt(
                                "id_orden"
                        )
                );

                orden.setIdPedido(
                        resultSet.getInt(
                                "id_pedido"
                        )
                );

                Timestamp inicio =
                        resultSet.getTimestamp(
                                "fecha_inicio"
                        );

                if (inicio != null) {

                    orden.setFechaInicio(
                            inicio.toLocalDateTime()
                    );
                }

                Timestamp fin =
                        resultSet.getTimestamp(
                                "fecha_fin"
                        );

                if (fin != null) {

                    orden.setFechaFin(
                            fin.toLocalDateTime()
                    );
                }

                orden.setEstado(
                        resultSet.getString(
                                "estado"
                        )
                );

                orden.setObservaciones(
                        resultSet.getString(
                                "observaciones"
                        )
                );

                ordenes.add(orden);
            }
        }

        return ordenes;
    }


    public boolean finalizarOrden(
            int idOrden,
            int idPedido
    ) throws SQLException {

        String sql =
                "UPDATE ordenes_produccion "
                        + "SET estado = 'TERMINADO', "
                        + "fecha_fin = NOW() "
                        + "WHERE id_orden = ? "
                        + "AND id_pedido = ? "
                        + "AND estado = 'EN PRODUCCION'";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, idOrden);
            statement.setInt(2, idPedido);

            return statement.executeUpdate() == 1;
        }
    }

    public void generarOrdenTransaccional(
            int idPedido,
            String observaciones
    ) throws SQLException {

        try (Connection connection =
                     DatabaseConnection.getConnection()) {

            connection.setAutoCommit(false);

            try {
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
                                    "Pedido inexistente."
                            );
                        }

                        estado = resultado.getString("estado");
                    }
                }

                if (!"CONFIRMADO".equals(estado)) {
                    throw new IllegalArgumentException(
                            "El pedido debe estar CONFIRMADO."
                    );
                }

                String sqlOrden =
                        "INSERT INTO ordenes_produccion "
                                + "(id_pedido, fecha_inicio, estado, observaciones) "
                                + "VALUES (?, NOW(), 'EN PRODUCCION', ?)";

                try (PreparedStatement statement =
                             connection.prepareStatement(sqlOrden)) {

                    statement.setInt(1, idPedido);
                    statement.setString(2, observaciones);
                    statement.executeUpdate();
                }

                String sqlPedido =
                        "UPDATE pedidos SET estado = 'EN PRODUCCION' "
                                + "WHERE id_pedido = ? AND estado = 'CONFIRMADO'";

                try (PreparedStatement statement =
                             connection.prepareStatement(sqlPedido)) {

                    statement.setInt(1, idPedido);

                    if (statement.executeUpdate() != 1) {
                        throw new SQLException(
                                "No se pudo actualizar el pedido."
                        );
                    }
                }

                connection.commit();

            } catch (Exception ex) {
                connection.rollback();
                throw ex;
            }
        }
    }

    public void finalizarOrdenTransaccional(
            int idOrden,
            int idPedido
    ) throws SQLException {

        try (Connection connection =
                     DatabaseConnection.getConnection()) {

            connection.setAutoCommit(false);

            try {
                String sqlEstado =
                        "SELECT estado FROM pedidos "
                                + "WHERE id_pedido = ? FOR UPDATE";

                try (PreparedStatement statement =
                             connection.prepareStatement(sqlEstado)) {

                    statement.setInt(1, idPedido);

                    try (ResultSet resultado =
                                 statement.executeQuery()) {

                        if (!resultado.next()
                                || !"EN PRODUCCION".equals(
                                resultado.getString("estado"))) {
                            throw new IllegalArgumentException(
                                    "El pedido no está EN PRODUCCION."
                            );
                        }
                    }
                }

                String sqlOrden =
                        "UPDATE ordenes_produccion "
                                + "SET estado = 'TERMINADO', fecha_fin = NOW() "
                                + "WHERE id_orden = ? AND id_pedido = ? "
                                + "AND estado = 'EN PRODUCCION'";

                try (PreparedStatement statement =
                             connection.prepareStatement(sqlOrden)) {

                    statement.setInt(1, idOrden);
                    statement.setInt(2, idPedido);

                    if (statement.executeUpdate() != 1) {
                        throw new IllegalArgumentException(
                                "La orden no existe, no corresponde "
                                        + "al pedido o ya está finalizada."
                        );
                    }
                }

                String sqlPedido =
                        "UPDATE pedidos SET estado = 'TERMINADO' "
                                + "WHERE id_pedido = ? AND estado = 'EN PRODUCCION'";

                try (PreparedStatement statement =
                             connection.prepareStatement(sqlPedido)) {

                    statement.setInt(1, idPedido);

                    if (statement.executeUpdate() != 1) {
                        throw new SQLException(
                                "No se pudo finalizar el pedido."
                        );
                    }
                }

                connection.commit();

            } catch (Exception ex) {
                connection.rollback();
                throw ex;
            }
        }
    }

}