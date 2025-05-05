package com.chat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseManager {
    public static boolean insertUser(String username, String password) {
        String checkQuery = "Select Count (*) from users where username = ?";
        String sql = "INSERT INTO users (username, password_hash) VALUES (?,?)";

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement stmtCheck = conn.prepareStatement(checkQuery)) {
            stmtCheck.setString(1, username);
            ResultSet rs = stmtCheck.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {

                return false;
            }
                else{
                try (

                        PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, username);
                    stmt.setString(2, password);
                    stmt.executeUpdate();
                    System.out.println("Usuário registrado com sucesso");

                    return true;

            }
        }
        } catch (SQLException e) {
            String msg = e.getMessage().toLowerCase();
            System.out.println(msg);
            return false;
        }
    }

}
