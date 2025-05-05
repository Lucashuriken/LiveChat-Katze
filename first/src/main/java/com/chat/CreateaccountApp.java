package com.chat;
import javafx.scene.input.KeyCode;
import javafx.scene.control.Alert.AlertType;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class CreateaccountApp extends Application {
    private TextField usuarioField;
    private PasswordField passwordField2;
    @Override
    public void start (Stage stage){
        //Elementos
        Label usuarioLabel = new Label("Username");
        usuarioField = new TextField ();
        Label passwordLabel = new Label("Password");
        PasswordField passwordField = new PasswordField();
        Label passwordLabel2 = new Label("Repita o Password");
        passwordField2 = new PasswordField();
        
        Button buttonCadastrar = new Button("Cadastrar");
        Button buttonVoltar = new Button("Voltar");
        
        HBox containerButton = new HBox(10);  // Adicionando espaçamento entre os botões
        containerButton.getChildren().addAll(buttonCadastrar, buttonVoltar);
        
        //Layout
        VBox layout = new VBox(10);  // Adicionando espaçamento entre os elementos
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(usuarioLabel, usuarioField, passwordLabel, passwordField, passwordLabel2, passwordField2, containerButton);
        
        
        
        // Evento de Cadastrar
        buttonCadastrar.setOnAction(e -> {
            String username = usuarioField.getText();
            String password = passwordField.getText();
            String confirmPassword = passwordField2.getText();
            
            // Validação de campos
            if (username.isEmpty()) {
                showAlert("Erro", "O campo 'Username' não pode ser vazio.");
                return;
            }
            if (password.isEmpty()) {
                showAlert("Erro", "O campo 'Password' não pode ser vazio.");
                return;
            }
            if (confirmPassword.isEmpty()) {
                showAlert("Erro", "O campo 'Repita o Password' não pode ser vazio.");
                return;
            }
            if (!password.equals(confirmPassword)) {
                showAlert("Erro", "As senhas não coincidem.");
                return;
            }
            
            // Cadastro de usuário e tratamento de conta duplicada
            
            try {
                boolean check = DatabaseManager.insertUser(username, confirmPassword);
                
                if(check == true){
                    showAlertDone("Criação de conta", "Conta criada com sucesso");
                }
                else {
                    showAlert("Erro", "Usuário já existente");
                }
            }
            catch(Exception a){
                String ex = a.getMessage();
                showAlert("Erro ao criar conta", "Usuário ou Senha já existente" + ex);
            }
            // Você pode adicionar aqui um redirecionamento para outra tela após o cadastro (se necessário)
        });
        
        // Evento de apertar enter e dar submit
        passwordField2.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                buttonCadastrar.fire();
            }
        });
        
        // Evento de voltar à tela de login
        buttonVoltar.setOnAction(e -> {
            LoginApp loginApp = new LoginApp();
            try {
                loginApp.start(new Stage());
                stage.close();  // Fecha a tela de cadastro
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        // Evento de cadastrar usuário
        
        // Icone
        Image icone = new Image("file:C:\\Users\\anrri\\OneDrive\\Área de Trabalho\\Projeto Fenix\\Bem vindo my lord\\Pick up some goods\\Art\\katze\\wazar.jpg");
        
        // Scene
        Scene scene = new Scene(layout, 300, 200);
        stage.setTitle("Cadastro do lululu :3 ");
        stage.setScene(scene);
        stage.show();
        stage.getIcons().add(icone);
        stage.setHeight(270);
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

    }
    public void showAlertDone(String title, String message) {
    Alert alert = new Alert(AlertType.CONFIRMATION);  // Definindo o tipo de alerta para "informação"
    alert.setTitle(title);
        alert.setHeaderText(null);  // Para remover o texto no topo do alerta, se necessário
        alert.setContentText(message);
        alert.showAndWait();
        }   
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
