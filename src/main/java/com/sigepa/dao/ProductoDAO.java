package com.sigepa.dao;

import com.sigepa.model.Producto;
import com.sigepa.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    public List<Producto> listar()
            throws SQLException {

        List<Producto> productos =
                new ArrayList<>();

        String sql =
                "SELECT * FROM productos "
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

                Producto producto =
                        new Producto();

                producto.setIdProducto(
                        resultSet.getInt(
                                "id_producto"
                        )
                );

                producto.setNombre(
                        resultSet.getString(
                                "nombre"
                        )
                );

                producto.setDescripcion(
                        resultSet.getString(
                                "descripcion"
                        )
                );

                producto.setPrecio(
                        resultSet.getBigDecimal(
                                "precio"
                        )
                );

                producto.setActivo(
                        resultSet.getBoolean(
                                "activo"
                        )
                );

                productos.add(producto);
            }
        }

        return productos;
    }

    public Producto buscarPorId(
            int idProducto
    ) throws SQLException {

        String sql =
                "SELECT * FROM productos "
                        + "WHERE id_producto = ? "
                        + "AND activo = TRUE";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    idProducto
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (resultSet.next()) {

                    Producto producto =
                            new Producto();

                    producto.setIdProducto(
                            resultSet.getInt(
                                    "id_producto"
                            )
                    );

                    producto.setNombre(
                            resultSet.getString(
                                    "nombre"
                            )
                    );

                    producto.setDescripcion(
                            resultSet.getString(
                                    "descripcion"
                            )
                    );

                    producto.setPrecio(
                            resultSet.getBigDecimal(
                                    "precio"
                            )
                    );

                    producto.setActivo(true);

                    return producto;
                }
            }
        }

        return null;
    }
}