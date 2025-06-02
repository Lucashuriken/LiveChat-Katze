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
        Label emailLabel = new Label("Email");
        TextField emailField = new TextField();
        
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
        layout.getChildren().addAll(emailLabel, emailField, passwordLabel, passwordField, buttonContainerBox);

        // Ação do botão de login
        loginButton.setOnAction(e -> {
            try{
            String email = emailField.getText();
            String password = passwordField.getText();
            // Aqui vamos validar o login 
            User user = DatabaseManager.userAutenticator(email, password);
            System.out.println("Usuário autenticado: " + (user != null ? user.getUsername() : "null"));
            if(user != null){
                Session.setCurrentUser(user); // Armazena o usuário globalmente na sessão
                ChatClient client = new ChatClient("localhost", 12345);
                Session.setChatClient(client);
                CreateaccountApp.showAlertDone("Login","Login realizado");
                Stage postLoginStage = new Stage();
                new PostLoginScreen(user).start(postLoginStage);
                stage.close();
            }
            else{
                CreateaccountApp.showAlert("Login","Conta não encontrada");
            }
        }catch(Exception ex){
            ex.printStackTrace();
            CreateaccountApp.showAlert("Erro", "Ocorreu um erro ao tentar realizar login. Tente novamente." + ex);
        }
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
