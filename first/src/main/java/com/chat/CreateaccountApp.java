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
import java.util.regex.Pattern;

public class CreateaccountApp extends Application {
 

    @Override
    public void start (Stage stage){
        //Elementos
        Label nomeLabel = new Label("Seu nome ?");
        TextField nomeField = new TextField();

/*         Label usuarioLabel = new Label("Username");
        usuarioField = new TextField (); */

        Label emailLabel = new Label("Email");
        TextField emailField = new TextField();

        Label passwordLabel = new Label("Password");
        PasswordField passwordField = new PasswordField();

        Label passwordLabel2 = new Label("Repita o Password");
        PasswordField passwordField2 = new PasswordField();
        
        Button buttonCadastrar = new Button("Cadastrar");
        Button buttonVoltar = new Button("Voltar");
        
        HBox containerButton = new HBox(10);  // Adicionando espaçamento entre os botões
        containerButton.getChildren().addAll(buttonCadastrar, buttonVoltar);
        
        //Layout
        VBox layout = new VBox(10);  // Adicionando espaçamento entre os elementos
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(nomeLabel,nomeField,emailLabel,emailField, passwordLabel, passwordField, passwordLabel2, passwordField2, containerButton);
        
        
        
        // Evento de Cadastrar
        buttonCadastrar.setOnAction(e -> {
        String nome = nomeField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = passwordField2.getText();

        // Validação de campos

        if (nome.isEmpty()) {
        showAlert("Erro", "O campo 'Seu nome' não pode ser vazio.");
        return;
    }
        if (email.isEmpty()) {
        showAlert("Erro", "O campo 'Email' não pode ser vazio.");
            if(validarEmail(emailField.getText())){
                showAlert("Erro", "Informe um email válido"); 
            }
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
    // Cadastro de usuário com retorno de status detalhado
    try {
        String resultado = DatabaseManager.insertUser(nome, password, email);

        if (resultado.equals("Usuário registrado com sucesso")) {
            showAlertDone("Criação de conta", resultado);
            Stage postLoginStage = new Stage();
            new LoginApp().start(postLoginStage);
            stage.close();
        } else {
            showAlert("Erro", resultado);
        }
    } catch (Exception ex) {
        showAlert("Erro ao criar conta", ex.getMessage());
    }
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
        Scene scene = new Scene(layout, 300, 300);
        stage.setTitle("Cadastro do lululu :3 ");
        stage.setScene(scene);
        stage.show();
        stage.getIcons().add(icone);
        
    }
    public static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

    }
    public static void showAlertDone(String title, String message) {
    Alert alert = new Alert(AlertType.CONFIRMATION);  // Definindo o tipo de alerta para "informação"
    alert.setTitle(title);
        alert.setHeaderText(null);  // Para remover o texto no topo do alerta, se necessário
        alert.setContentText(message);
        alert.showAndWait();
        }   
  
    public static boolean validarEmail(String email){
        String EMAIL_REGEX = 
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
         Pattern emailPattern = Pattern.compile(EMAIL_REGEX);
        if(email == null || email.isEmpty()){
            return false;
        }
         return emailPattern.matcher(email).matches();
        
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
