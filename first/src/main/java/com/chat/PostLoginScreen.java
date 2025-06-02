package com.chat;
import java.util.Map;

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

public class PostLoginScreen {
    private User user;
    
    public PostLoginScreen(User user){
        this.user = user;
    }
    
    public void start(Stage primaryStage) {
        User user = Session.getCurrentUser();
        String emailUser = user.getEmail(); //Utilizado para criação de novos amigos;
        
        //int friendId =  DatabaseManager.getFriendId(user.getId());
        // 1. Header com foto, nome e status
        Image profileImage = new Image("https://i.pinimg.com/736x/c6/7c/e0/c67ce0f1ed761a07caf801be53bbb60f.jpg");  //exemplo de foto de perfil
        ImageView profileImageView = applyStyle(new ImageView(profileImage));
       
        Label nomeUsuario = new Label(user.getUsername()) ;
                        //Status setup
        //status style
        Label statusLabel = new Label(user.getStatus() != "[null]" ? user.getStatus() : "Olá mundo"); 
        statusLabel.setStyle(
        "-fx-background-color: #ffffff;" +"-fx-border-color: #cccccc;" +"-fx-border-radius: 5;"+"-fx-background-radius: 5;"+"-fx-padding: 5;" +"-fx-cursor: hand;");
        //style hover
        UIUtils.applyHoverTransition(statusLabel, Color.TRANSPARENT, Color.web("#e6f2ff"), 100, 5);
        
        
        //config to change status
        UIUtils.showStatusPopup(statusLabel, statusLabel.getText());
        
        VBox containerStatus = new VBox(5);
        containerStatus.getChildren().addAll(nomeUsuario,statusLabel);
        
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
        Map<String, Integer> contactMap = DatabaseManager.getUserContactsNames(emailUser);
        contactsListView.getItems().addAll(contactMap.keySet());  // Exemplo de contatos
        contactsArea.getChildren().addAll(contactsLabel, contactsListView);
        contactsListView.setOnMouseClicked(e -> {
            if(e.getClickCount() == 2){
                String selectedContact = contactsListView.getSelectionModel().getSelectedItem();
                if (!contactMap.containsKey(selectedContact)){
                    System.out.println("Não foi feita a associação chave(nome) : Valor (id_contato)");
                }
                else {
                    new ChatWindow(user.getId(), contactMap.get(selectedContact), selectedContact);
                }
                
            }
        });
        // 4. Botões
        Button btnLogout = new Button("Logout");
        Button btnCriarGrupo = new Button("Criar Grupo");
        Button btnCriarContato = new Button("Adicionar Contato");

        HBox buttonContainerBottom = new HBox(10);
        buttonContainerBottom.getChildren().addAll(btnLogout, btnCriarGrupo, btnCriarContato);
        btnLogout.setOnAction(event -> {
            System.out.println("Logout realizado");
            
            primaryStage.close();
            new LoginApp().start(new Stage()); // Abrir a tela de login novamente, se necessário
        });
        btnCriarContato.setOnAction(e -> {
            System.out.println("User: " + user);
            System.out.println("Email: " + (user != null ? user.getEmail() : "null"));
            UIUtils.showFriendPopup(btnCriarContato, emailUser);
        });
        btnCriarGrupo.setOnAction(e -> {
        UIUtils.showCreateGroupPopup(btnCriarGrupo, emailUser);
        });
        // 5. Layout principal
        HBox mainLayout = new HBox(20);  // O HBox vai organizar o aside e a área de contatos horizontalmente
        mainLayout.getChildren().addAll(aside, contactsArea);
        
        VBox root = new VBox(10);
        root.getChildren().addAll(header, mainLayout, buttonContainerBottom);  // Colocando o header no topo, o mainLayout no meio e o botão no final
        
        // Definindo a cena
        Scene scene = new Scene(root, 600, 400);
        
        primaryStage.setTitle("Tela Pós-Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    
        
    }
    private static ImageView applyStyle(ImageView imageView) {
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
        Circle clip = new Circle(25, 25, 25);  // Círculo para fazer a imagem redonda
        imageView.setClip(clip);
        return imageView;
    }

}
