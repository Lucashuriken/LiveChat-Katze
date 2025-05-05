package com.chat;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class ChatApp extends Application {

    @Override
    public void start(Stage stage) {
        // Criação dos componentes do chat
        TextArea chatArea = new TextArea();
        
        chatArea.setEditable(false); // O chat não pode ser editado pelo usuário

        TextField messageField = new TextField();
        Button sendButton = new Button("Send");
        Button emoteButton = new Button("Emote");
        HBox buttonBox = new HBox(10);
       
        buttonBox.getChildren().addAll(sendButton, emoteButton);
        
        // Layout do chat
        VBox layout = new VBox(10);
 
        layout.getChildren().addAll(chatArea, messageField, buttonBox);
       
        //Configurando emojis
        
        GridPane emojiGrid = new GridPane();
        
        //lista emoji
        String[] emojisList = {
            "😀", "😁", "😂", "🤣", "😅", 
            "😊", "😍", "😎", "😭", "😡", 
            "👍", "👎", "👀", "💪", "🔥",
            "❤", "💔", "🎉", "💀", "🚀"
        };
        
        int col = 0, row = 0;
        
        for(String emoji : emojisList){
            Button emojiPress = new Button(emoji);
            
            emojiPress.setOnAction(e ->{
                messageField.appendText(emoji);
            });
            emojiGrid.add(emojiPress, col, row);
            col ++;
            if(col > 4){
                col = 0;
                row ++;
            };
        }
        emojiGrid.setVgap(5);
        emojiGrid.setHgap(5);
        emojiGrid.setStyle("-fx-padding: 10; -fx-background-color: white; -fx-border-color: gray;");

        //criando a pop up dos emojis
        Popup emojiPop = new Popup();
        
        emojiPop.setAutoHide(true);
        emojiPop.getContent().add(emojiGrid);
        
        emoteButton.setOnAction(e ->{
            emojiPop.show(emoteButton.getScene().getWindow());
        });
        // Ação do botão de enviar
        sendButton.setOnAction(e -> {
            String message = messageField.getText();
            if (!message.isEmpty()) {
                chatArea.appendText("You: " + message + "\n");
                messageField.clear();
            }
        });
        
        messageField.setOnKeyPressed(e->{
            if(e.getCode() == KeyCode.ENTER) {
                sendButton.fire();
            }});
            
        //Icone 
        Image icone = new Image("file:C:\\Users\\anrri\\OneDrive\\Área de Trabalho\\Projeto Fenix\\Bem vindo my lord\\Pick up some goods\\Art\\katze\\wazar.jpg");
        
        // Cena e stage
         Scene scene = new Scene(layout, 400, 300);
         stage.setTitle("Chat do lululu");
         stage.setScene(scene);
         stage.show();
         stage.setHeight(300);
         stage.getIcons().add(icone);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
