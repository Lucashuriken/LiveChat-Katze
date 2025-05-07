package com.chat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    public static String insertUser(String nome, String password, String email) {
       
        String checkEmailQuery = "SELECT COUNT(*) FROM users WHERE email = ?";
        String insertQuery = "INSERT INTO users (username, password_hash, email) VALUES (?, ?, ?)";
    
        try (Connection conn = DatabaseConnection.connect()) {
    
    
            // Verifica se o email já existe
            try (PreparedStatement stmtEmail = conn.prepareStatement(checkEmailQuery)) {
                stmtEmail.setString(1, email);
                ResultSet rsEmail = stmtEmail.executeQuery();
                if (rsEmail.next() && rsEmail.getInt(1) > 0) {
                    return "Email já está em uso";
                }
            }
    
            // Insere o novo usuário
            try (PreparedStatement stmtInsert = conn.prepareStatement(insertQuery)) {
                stmtInsert.setString(1, nome);
                stmtInsert.setString(2, password);
                stmtInsert.setString(3, email);
                stmtInsert.executeUpdate();
                return "Usuário registrado com sucesso";
            }
    
        } catch (SQLException e) {
            return "Erro ao registrar usuário: " + e.getMessage();
        }
    }
    

    public static User userAutenticator(String email, String password){
        String query = "select * from users where email = ? and password_hash = ?";
        try(Connection conn = DatabaseConnection.connect();
            PreparedStatement stmt = conn.prepareStatement(query)
        ){
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet result= stmt.executeQuery();
            
            if(result.next()){
                int id = result.getInt("id");
                String username = result.getString("username");
                String userEmail = result.getString("email");
                String status = result.getString("status");
                String img = result.getString("status");
                
                return new User(id, username,userEmail, status, img);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    
    public static List<String> getUserGroups(String email){
        List<String> grupos = new ArrayList<>();
        String sql = "SELECT g.name FROM groups g JOIN group_members ug ON g.id = ug.group_id JOIN users u ON ug.user_id = u.id WHERE u.email = ?";
        try(Connection conn = DatabaseConnection.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(0, email);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                grupos.add(rs.getString("name"));
            }
        }
        catch(Exception e){
            
        }
        return grupos;
    }
    
    
    public static List<String> getUserContacts(String email){
    List<String> contacts = new ArrayList<>();
    String sql = "SELECT u2.username FROM contacts c " +
                 "JOIN users u1 ON c.user_id = u1.id " +
                 "JOIN users u2 ON c.contact_id = u2.id " +
                 "WHERE u1.email = ?";

    try (Connection conn = DatabaseConnection.connect();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, email);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            contacts.add(rs.getString("username"));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return contacts;
}
}
