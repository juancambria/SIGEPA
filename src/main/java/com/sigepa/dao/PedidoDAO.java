package com.sigepa.dao;

import com.sigepa.model.DetallePedido;
import com.sigepa.model.Pedido;
import com.sigepa.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    public int guardar(
            Pedido pedido
    ) throws SQLException {

        Connection connection =
                DatabaseConnection.getConnection();

        try {

            connection.setAutoCommit(false);

            String sqlPedido =
                    "INSERT INTO pedidos "
                            + "(id_cliente, estado, observaciones, total) "
                            + "VALUES (?, ?, ?, ?)";

            int idPedido;

            try (
                    PreparedStatement statement =
                            connection.prepareStatement(
                                    sqlPedido,
                                    Statement.RETURN_GENERATED_KEYS
                            )
            ) {

                statement.setInt(
                        1,
                        pedido.getIdCliente()
                );

                statement.setString(
                        2,
                        pedido.getEstado()
                );

                statement.setString(
                        3,
                        pedido.getObservaciones()
                );

                statement.setBigDecimal(
                        4,
                        pedido.getTotal()
                );

                statement.executeUpdate();

                try (
                        ResultSet keys =
                                statement.getGeneratedKeys()
                ) {

                    if (!keys.next()) {

                        throw new SQLException(
                                "No se pudo obtener el ID del pedido."
                        );
                    }

                    idPedido =
                            keys.getInt(1);
                }
            }

            String sqlDetalle =
                    "INSERT INTO detalle_pedidos "
                            + "(id_pedido, id_producto, cantidad, precio_unitario, subtotal) "
                            + "VALUES (?, ?, ?, ?, ?)";

            for (
                    DetallePedido detalle :
                    pedido.getDetalles()
            ) {

                try (
                        PreparedStatement statement =
                                connection.prepareStatement(
                                        sqlDetalle
                                )
                ) {

                    statement.setInt(
                            1,
                            idPedido
                    );

                    statement.setInt(
                            2,
                            detalle.getIdProducto()
                    );

                    statement.setBigDecimal(
                            3,
                            detalle.getCantidad()
                    );

                    statement.setBigDecimal(
                            4,
                            detalle.getPrecioUnitario()
                    );

                    statement.setBigDecimal(
                            5,
                            detalle.getSubtotal()
                    );

                    statement.executeUpdate();
                }
            }

            connection.commit();

            return idPedido;

        } catch (Exception exception) {

            connection.rollback();

            throw exception;

        } finally {

            connection.setAutoCommit(true);

            connection.close();
        }
    }

    public List<Pedido> listar()
            throws SQLException {

        List<Pedido> pedidos =
                new ArrayList<>();

        String sql =
                "SELECT p.*, c.nombre AS cliente "
                        + "FROM pedidos p "
                        + "INNER JOIN clientes c "
                        + "ON c.id_cliente = p.id_cliente "
                        + "ORDER BY p.id_pedido DESC";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                Pedido pedido =
                        new Pedido();

                pedido.setIdPedido(
                        resultSet.getInt(
                                "id_pedido"
                        )
                );

                pedido.setIdCliente(
                        resultSet.getInt(
                                "id_cliente"
                        )
                );

                pedido.setNombreCliente(
                        resultSet.getString(
                                "cliente"
                        )
                );

                pedido.setEstado(
                        resultSet.getString(
                                "estado"
                        )
                );

                pedido.setObservaciones(
                        resultSet.getString(
                                "observaciones"
                        )
                );

                pedido.setTotal(
                        resultSet.getBigDecimal(
                                "total"
                        )
                );

                pedidos.add(pedido);
            }
        }

        return pedidos;
    }

    public String obtenerEstado(
            int idPedido
    ) throws SQLException {

        String sql =
                "SELECT estado FROM pedidos "
                        + "WHERE id_pedido = ?";

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

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (resultSet.next()) {

                    return resultSet.getString(
                            "estado"
                    );
                }
            }
        }

        return null;
    }

    public void actualizarEstado(
            int idPedido,
            String estado
    ) throws SQLException {

        String sql =
                "UPDATE pedidos "
                        + "SET estado = ? "
                        + "WHERE id_pedido = ?";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    estado
            );

            statement.setInt(
                    2,
                    idPedido
            );

            statement.executeUpdate();
        }
    }
}