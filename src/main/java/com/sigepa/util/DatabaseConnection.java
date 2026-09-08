package com.sigepa.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/sigepa_db"
                    + "?useSSL=false"
                    + "&serverTimezone=America/Argentina/Cordoba";

    private static final String USER = "root";

    // COLOCÁ ACÁ TU CONTRASEÑA DE MYSQL
    private static final String PASSWORD = "";

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                URL,
                USER,
                PASSWORD
        );
    }
}