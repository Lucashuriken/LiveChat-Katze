package com.chat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseManager {
    public static String insertUser(String nome, String password, String email) {

        String checkEmailQuery = "SELECT COUNT(*) FROM users WHERE email = ?";
        String insertQuery = "INSERT INTO users (username, password_hash, email, status) VALUES (?, ?, ?, ?)";

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
                stmtInsert.setString(4, "Olá mundo");
                stmtInsert.executeUpdate();
                return "Usuário registrado com sucesso";
            }

        } catch (SQLException e) {
            return "Erro ao registrar usuário: " + e.getMessage();
        }
    }

    public static User userAutenticator(String email, String password) {
        String query = "select * from users where email = ? and password_hash = ?";
        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet result = stmt.executeQuery();

            if (result.next()) {
                int id = result.getInt("id");
                String username = result.getString("username");
                String userEmail = result.getString("email");
                String status = result.getString("status");
                String img = result.getString("img");

                return new User(id, username, userEmail, status, img);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // falta implementar
    public static List<String> getUserGroups(String email) {
        List<String> grupos = new ArrayList<>();
        String sql = "SELECT g.name FROM groups g JOIN group_members gm ON g.id = gm.group_id JOIN users u ON gm.user_id = u.id WHERE u.email = ?";
        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                grupos.add(rs.getString("name"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return grupos;
    }

    // falta implementar
    public static Map<String, Integer> getUserContactsNames(String email) {
        Map<String, Integer> contacts = new HashMap<>();
        String sql = 
            "SELECT u2.username, u2.id AS contact_id FROM contacts c " +
            "JOIN users u1 ON c.user_id = u1.id " +
            "JOIN users u2 ON c.contact_id = u2.id " +
            "WHERE u1.email = ?";

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                contacts.put(rs.getString("username"), rs.getInt("contact_id"));
            
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return contacts;
    }


    public void createFriend(String emailUser, String emailFriend) {
        if (emailUser.equalsIgnoreCase(emailFriend)) {
            System.out.println("Tentou adicionar a si mesmo");
            return;
        }

        try (Connection conn = DatabaseConnection.connect()) {
            int userId = getUserIdByEmail(conn, emailUser);
            int contactId = getUserIdByEmail(conn, emailFriend);

            if (contactId == -1) {
                System.out.println("Usuário(s) não encontrado(s).");
                return; // mais pra frente vai ter que mudar essa lógica pra imprimir a mensagem se o
                        // contato já existe/contato não encontrado etc..
            }

            String query = "SELECT * FROM Contacts WHERE user_id = ? AND contact_id = ?";

            try (
                    PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, userId);
                stmt.setInt(2, contactId);
                ResultSet rs = stmt.executeQuery();
                if (!rs.next()) {
                    String qry = "insert into contacts(user_id, contact_id) values (?,?)";
                    try (PreparedStatement insertStmt = conn.prepareStatement(qry)) {
                        insertStmt.setInt(1, userId);
                        insertStmt.setInt(2, contactId);
                        insertStmt.executeUpdate();
                        System.out.println("Usuário adicionado");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static GroupCreationResult createGroup(String nomeGrupo, int userId) {
        if (nomeGrupo == null || nomeGrupo.trim().isEmpty()) {
            System.out.println("Nome do grupo inválido.");
            return GroupCreationResult.ERROR;
        }

        String checkSql = "SELECT id FROM groups WHERE name = ? AND created_by = ?";
        String insertSql = "INSERT INTO groups (name, created_by) VALUES (?, ?)";
        String addMemberSql = "INSERT INTO group_members (group_id, user_id) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setString(1, nomeGrupo);
            checkStmt.setInt(2, userId);

            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                System.out.println("Grupo já existe para este usuário.");
                return GroupCreationResult.ALREADY_EXISTS;
            }

            // Criar grupo
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                insertStmt.setString(1, nomeGrupo);
                insertStmt.setInt(2, userId);
                insertStmt.executeUpdate();

                ResultSet generatedKeys = insertStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int groupId = generatedKeys.getInt(1);

                    // Adicionar o criador como membro
                    try (PreparedStatement addMemberStmt = conn.prepareStatement(addMemberSql)) {
                        addMemberStmt.setInt(1, groupId);
                        addMemberStmt.setInt(2, userId);
                        addMemberStmt.executeUpdate();
                    }

                    System.out.println("Grupo criado e usuário adicionado como membro.");
                    return GroupCreationResult.SUCCESS;
                } else {
                    System.out.println("Erro ao obter o ID do grupo recém-criado.");
                    return GroupCreationResult.ERROR;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return GroupCreationResult.ERROR;
        }
    }
    // Método para encontrar o id tendo apenas o email, vamos utilizar em
    // createFriend//

    public static int getUserIdByEmail(String email) {
        // Este método é chamado em UiUtilis.showCreatedGroupPopup
        String sql = "SELECT id FROM Users WHERE email = ?";
        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private int getUserIdByEmail(Connection conn, String email) {
        // Este método será chamado aqui no DatabaseManager.createFriend
        String sql = "SELECT id FROM Users WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int getFriendId(int userId){ 
        String sql = "select contact_id from contacts where user_id = ? ";
        try (Connection conn = DatabaseConnection.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            return rs.getInt("contact_id");

            }
            catch(SQLException ex) {
                ex.printStackTrace();
            }
            System.out.println("Coluna não encontrada");
            return 0;   
        }
}