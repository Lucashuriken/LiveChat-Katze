package com.chat;
import javafx.scene.input.KeyCode;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LoginApp extends Application {

    @Override
    public void start(Stage stage) {
        // Criação dos componentes da tela
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();

        Button loginButton = new Button("Login");
        Button buttonEsqueciSenha = new Button("Esqueci a senha");
        Button criarConta = new Button ("Criar conta");
        Button guestButton = new Button ("Entrar sem conta");

        // Layout
        HBox buttonContainerBox = new HBox(10);
        buttonContainerBox.getChildren().addAll(loginButton, buttonEsqueciSenha, guestButton, criarConta);

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(usernameLabel, usernameField, passwordLabel, passwordField, buttonContainerBox);

        // Ação do botão de login
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            // Aqui você pode validar o login (por enquanto, só imprime)
            System.out.println("Username: " + username);
            System.out.println("Password: " + password);
        });
        
        //Eventos de clique dos botões
        
        //Evento de apertar Enter
        passwordField.setOnKeyPressed(e -> {
        if(e.getCode() == KeyCode.ENTER){
            loginButton.fire();
        
        }
        });

        //Icone
        Image icone = new Image("file:C:\\Users\\anrri\\OneDrive\\Área de Trabalho\\Projeto Fenix\\Bem vindo my lord\\Pick up some goods\\Art\\katze\\wazar.jpg");
        
        // Cena e stage
        Scene scene = new Scene(layout, 400, 200);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
        stage.getIcons().add(icone);
        stage.setResizable(false);
        
        //Trocar de tela no criar conta
        criarConta.setOnAction((e->{
            CreateaccountApp createaccountApp = new CreateaccountApp();
            createaccountApp.start(new Stage());
            stage.close();
        }));
    
    }
        
    public static void validarConta(){

    }
    public static void main(String[] args) {
        launch(args);
    }
}
