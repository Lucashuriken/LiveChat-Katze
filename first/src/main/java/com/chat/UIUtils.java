package com.chat;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.stage.Popup;
import javafx.scene.control.Label;
public class UIUtils {
    //Classe criada para estilização ou configurações extras de pop ups, ela será instanciada
    public static void applyHoverTransition(Region region, Color fromColor, Color toColor, int durationMs, double radius) {
        region.setBackground(new Background(
            new BackgroundFill(fromColor, new CornerRadii(radius), Insets.EMPTY))
        );

        region.setOnMouseEntered(e -> {
            Timeline enter = new Timeline(
                new KeyFrame(Duration.millis(durationMs),
                    new KeyValue(region.backgroundProperty(),
                        new Background(new BackgroundFill(toColor, new CornerRadii(radius), Insets.EMPTY)))
                )
            );
            enter.play();
        });

        region.setOnMouseExited(e -> {
            Timeline exit = new Timeline(
                new KeyFrame(Duration.millis(durationMs),
                    new KeyValue(region.backgroundProperty(),
                        new Background(new BackgroundFill(fromColor, new CornerRadii(radius), Insets.EMPTY)))
                )
            );
            exit.play();
        });
    }
    public static void showStatusPopup(Label statusLabel, String initialText) {
        Popup popup = new Popup();
        Button saveButton = new Button("Salvar");
        // Criar o TextField para o novo status
        TextField statusField = new TextField(initialText);
        statusField.setPromptText("Digite seu novo status");

        // Criar o Popup
        VBox vbox = new VBox(10, statusField, saveButton);
        vbox.setStyle("-fx-padding: 10; -fx-background-color: white; -fx-border-color: #3366cc; -fx-border-radius: 5;");
        vbox.setAlignment(Pos.CENTER);
        popup.getContent().add(vbox);

        // Criar um botão de "Salvar" para atualizar o status
        saveButton.setOnAction(event -> {
            // Atualiza o status no Label e esconde o popup
            statusLabel.setText(statusField.getText());
            popup.hide(); // Esconde o popup
        });


        // Evento de clique no Label para mostrar o Popup
        statusLabel.setOnMouseClicked(e -> {
            // Posicionar o popup abaixo do statusLabel
            double screenX = statusLabel.localToScreen(statusLabel.getBoundsInLocal().getMinX(), statusLabel.getBoundsInLocal().getMinY()).getX();
            double screenY = statusLabel.localToScreen(statusLabel.getBoundsInLocal().getMinX(), statusLabel.getBoundsInLocal().getMinY()).getY();
            popup.show(statusLabel.getScene().getWindow(), screenX, screenY + statusLabel.getHeight() + 5);
            statusField.requestFocus(); // Focar no TextField
        });
    }
}
