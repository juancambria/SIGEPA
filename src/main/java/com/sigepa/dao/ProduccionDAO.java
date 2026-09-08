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

    public void finalizarOrden(
            int idOrden
    ) throws SQLException {

        String sql =
                "UPDATE ordenes_produccion "
                        + "SET estado = 'TERMINADO', "
                        + "fecha_fin = NOW() "
                        + "WHERE id_orden = ?";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    idOrden
            );

            statement.executeUpdate();
        }
    }
}