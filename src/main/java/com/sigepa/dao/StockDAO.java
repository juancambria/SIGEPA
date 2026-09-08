package com.sigepa.dao;

import com.sigepa.model.StockItem;
import com.sigepa.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StockDAO {

    public List<StockItem> listar()
            throws SQLException {

        List<StockItem> items =
                new ArrayList<>();

        String sql =
                "SELECT "
                        + "s.id_stock, "
                        + "s.id_material, "
                        + "m.nombre, "
                        + "s.cantidad, "
                        + "s.ubicacion, "
                        + "m.stock_minimo "
                        + "FROM stock s "
                        + "INNER JOIN materiales m "
                        + "ON m.id_material = s.id_material "
                        + "WHERE m.activo = TRUE "
                        + "ORDER BY m.nombre";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                StockItem item =
                        new StockItem();

                item.setIdStock(
                        resultSet.getInt(
                                "id_stock"
                        )
                );

                item.setIdMaterial(
                        resultSet.getInt(
                                "id_material"
                        )
                );

                item.setMaterial(
                        resultSet.getString(
                                "nombre"
                        )
                );

                item.setCantidad(
                        resultSet.getBigDecimal(
                                "cantidad"
                        )
                );

                item.setUbicacion(
                        resultSet.getString(
                                "ubicacion"
                        )
                );

                item.setStockMinimo(
                        resultSet.getBigDecimal(
                                "stock_minimo"
                        )
                );

                items.add(item);
            }
        }

        return items;
    }

    public void registrarMovimiento(
            int idMaterial,
            int idUsuario,
            String tipo,
            BigDecimal cantidad,
            String motivo
    ) throws SQLException {

        Connection connection =
                DatabaseConnection.getConnection();

        try {

            connection.setAutoCommit(false);

            String consultaStock =
                    "SELECT cantidad "
                            + "FROM stock "
                            + "WHERE id_material = ? "
                            + "FOR UPDATE";

            BigDecimal stockActual;

            try (
                    PreparedStatement statement =
                            connection.prepareStatement(
                                    consultaStock
                            )
            ) {

                statement.setInt(
                        1,
                        idMaterial
                );

                try (
                        ResultSet resultSet =
                                statement.executeQuery()
                ) {

                    if (!resultSet.next()) {

                        throw new SQLException(
                                "Material inexistente."
                        );
                    }

                    stockActual =
                            resultSet.getBigDecimal(
                                    "cantidad"
                            );
                }
            }

            BigDecimal nuevoStock;

            if (
                    tipo.equalsIgnoreCase(
                            "INGRESO"
                    )
            ) {

                nuevoStock =
                        stockActual.add(
                                cantidad
                        );

            } else {

                nuevoStock =
                        stockActual.subtract(
                                cantidad
                        );

                if (
                        nuevoStock.compareTo(
                                BigDecimal.ZERO
                        ) < 0
                ) {

                    throw new IllegalArgumentException(
                            "Stock insuficiente."
                    );
                }
            }

            String actualizar =
                    "UPDATE stock "
                            + "SET cantidad = ? "
                            + "WHERE id_material = ?";

            try (
                    PreparedStatement statement =
                            connection.prepareStatement(
                                    actualizar
                            )
            ) {

                statement.setBigDecimal(
                        1,
                        nuevoStock
                );

                statement.setInt(
                        2,
                        idMaterial
                );

                statement.executeUpdate();
            }

            String movimiento =
                    "INSERT INTO movimientos_stock "
                            + "(id_material, id_usuario, tipo, cantidad, motivo) "
                            + "VALUES (?, ?, ?, ?, ?)";

            try (
                    PreparedStatement statement =
                            connection.prepareStatement(
                                    movimiento
                            )
            ) {

                statement.setInt(
                        1,
                        idMaterial
                );

                statement.setInt(
                        2,
                        idUsuario
                );

                statement.setString(
                        3,
                        tipo.toUpperCase()
                );

                statement.setBigDecimal(
                        4,
                        cantidad
                );

                statement.setString(
                        5,
                        motivo
                );

                statement.executeUpdate();
            }

            connection.commit();

        } catch (Exception exception) {

            connection.rollback();

            throw exception;

        } finally {

            connection.setAutoCommit(true);

            connection.close();
        }
    }
}