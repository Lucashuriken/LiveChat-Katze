package com.chat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
 private static final String url = "jdbc:postgresql://localhost:5432/LiveChatLulu";
 private static final String user = "postgres";
 private static final String password = "Kickapoo123!";


 public static Connection connect() throws SQLException{
    return DriverManager.getConnection(url,user,password);
 }
}
