package com.chat;

import java.io.IOException;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ChatWindow extends Stage {

    private int userId;
    private int friendId;
    private TextArea chatArea;
    private TextField messageField;
    private ChatClient chatClient;

    public ChatWindow(int userId, int friendId, String friendName) {
        this.userId = userId;
        this.friendId = friendId;

        setTitle("Conversa com " + friendName);

        // Área de chat
        this.chatArea = new TextArea();
        

        chatArea.setEditable(false); // O chat não pode ser editado pelo usuário

        this.messageField = new TextField();
        Button sendButton = new Button("Send");
        Button emoteButton = new Button("Emote");
        HBox buttonBox = new HBox(10);
       
        buttonBox.getChildren().addAll(sendButton, emoteButton);
            emoteButton.setOnAction(e ->{ UIUtils.showEmojiPopup(emoteButton, messageField);});
            sendButton.setOnAction(e -> {enviarMensagem(messageField);});
            messageField.setOnKeyPressed(e->{
            if(e.getCode() == KeyCode.ENTER) {
                sendButton.fire();
            }});
        
        
        // Layout do chat
        VBox layout = new VBox(10);
 
        layout.getChildren().addAll(chatArea, messageField, buttonBox);
       
        setOnCloseRequest(e -> { //eventos após fechar a janela do chat
            try {
                if (chatClient != null)
                    chatClient.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        
        Scene scene = new Scene(layout, 400, 300);
        setScene(scene);
        try {
            this.chatClient = new ChatClient("localhost", 12345);
            chatClient.listenForMessages(this::mensagemRecebida);
        } catch (IOException e) {
            e.printStackTrace();
        }
        show();
        
        carregarMensagens();
    }

    private void carregarMensagens() {
        try {
            // Use seu método existente de acesso ao banco aqui
            // Exemplo genérico com JDBC:
            String sql = "SELECT sender_id, message, timestamp FROM user_messages WHERE " +
                         "(sender_id = ? AND receiver_id = ?) OR (sender_id = ? AND receiver_id = ?) ORDER BY timestamp";
            var conn = DatabaseConnection.connect();
            var stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, friendId);
            stmt.setInt(3, friendId);
            stmt.setInt(4, userId);
            var rs = stmt.executeQuery();

            while (rs.next()) {
                int sender = rs.getInt("sender_id");
                String message = rs.getString("message");
                String time = rs.getString("timestamp");
                String senderName = (sender == userId) ? "Você" : "Amigo";

                chatArea.appendText(senderName + " (" + time + "): " + message + "\n");
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void enviarMensagem(TextField messageField) {
        String message = this.messageField.getText();
        if (message == null){ 
        System.out.print("Campo de mensagem nulo");
         return;
    }
        try {
            String sql = "INSERT INTO user_messages (sender_id, receiver_id, message) VALUES (?, ?, ?)";
            var conn = DatabaseConnection.connect();
            var stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, friendId);
            stmt.setString(3, message);
            stmt.executeUpdate();
            stmt.close();

            chatArea.appendText("Você: " + message + "\n");
            messageField.clear();
            if (chatClient != null) {
                chatClient.sendMessage(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mensagemRecebida(String mensagem) {
    Platform.runLater(() -> chatArea.appendText("Amigo: " + mensagem + "\n"));
}

}
