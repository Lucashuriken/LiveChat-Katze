package com.chat;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class PostLoginScreen extends Application {
    private User user;
    
    public PostLoginScreen(User user){
        this.user = user;
    }

    @Override
    public void start(Stage primaryStage) {
        // 1. Header com foto e status
        Image profileImage = new Image("https://i.pinimg.com/736x/c6/7c/e0/c67ce0f1ed761a07caf801be53bbb60f.jpg");  // Coloque a URL da foto do usuário aqui
        ImageView profileImageView = new ImageView(profileImage);
        profileImageView.setFitWidth(50);
        profileImageView.setFitHeight(50);
        Circle clip = new Circle(25, 25, 25);
        profileImageView.setClip(clip);
       
                        //Status setup
        //status style
        Label statusLabel = new Label(user.getStatus() != null ? user.getStatus() : "Olá mundo"); 
        statusLabel.setStyle(
        "-fx-background-color: #ffffff;" +  
        "-fx-border-color: #cccccc;" +     
        "-fx-border-radius: 5;" +           
        "-fx-background-radius: 5;" +       
        "-fx-padding: 5;"  +                 
        "-fx-cursor: hand;");
        //style hover
        UIUtils.applyHoverTransition(statusLabel, Color.TRANSPARENT, Color.web("#e6f2ff"), 100, 5);
        
        
        //config to change status
        UIUtils.showStatusPopup(statusLabel, statusLabel.getText());
        
        HBox containerStatus = new HBox(5);
        containerStatus.getChildren().addAll(statusLabel);
        
        HBox header = new HBox(10);
        header.getChildren().addAll(profileImageView, containerStatus);
        
        // 2. Aside com grupos criados
        VBox aside = new VBox(10);
        Label groupsLabel = new Label("Grupos Criados:");
        ListView<String> groupListView = new ListView<>();
        
        groupListView.getItems().addAll(DatabaseManager.getUserGroups(user.getEmail())); 
        aside.getChildren().addAll(groupsLabel, groupListView);
        
        // 3. Área de contatos à direita do aside
        VBox contactsArea = new VBox(10);
        Label contactsLabel = new Label("Contatos:");
        ListView<String> contactsListView = new ListView<>();
        contactsListView.getItems().addAll(DatabaseManager.getUserContacts(user.getEmail()));  // Exemplo de contatos
        contactsArea.getChildren().addAll(contactsLabel, contactsListView);
        
        // 4. Botão de Logout
        Button btnLogout = new Button("Logout");
        btnLogout.setOnAction(event -> {
            System.out.println("Logout realizado");
            primaryStage.close();
            new LoginApp().start(new Stage()); // Abrir a tela de login novamente, se necessário
        });
        
        // 5. Layout principal
        HBox mainLayout = new HBox(20);  // O HBox vai organizar o aside e a área de contatos horizontalmente
        mainLayout.getChildren().addAll(aside, contactsArea);
        
        VBox root = new VBox(10);
        root.getChildren().addAll(header, mainLayout, btnLogout);  // Colocando o header no topo, o mainLayout no meio e o botão no final
        
        // Definindo a cena
        Scene scene = new Scene(root, 600, 400);
        
        primaryStage.setTitle("Tela Pós-Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    
        
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
