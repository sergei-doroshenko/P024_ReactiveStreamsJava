package org.sdoroshenko;

import java.sql.*;

public class Repository {

    public void getData() throws SQLException {
        try (
                Connection conn = DriverManager.getConnection("jdbc:h2:mem:");
                Statement stat = conn.createStatement();
                ResultSet rs = stat.executeQuery("SELECT 2 + 2 AS total")
        ) {
            if (rs.next()) {
                System.out.println(rs.getInt("total"));
                assert rs.getInt("total") == 4;
            }
        }
    }
}
