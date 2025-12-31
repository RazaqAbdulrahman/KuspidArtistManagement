package com.kuspid.ui.components;

import atlantafx.base.theme.Styles;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;

/**
 * Premium modal dialog for the media player.
 */
public class AudioPlayerDialog {
    private Stage stage;
    private MediaPlayerComponent mediaPlayer;

    public AudioPlayerDialog(String filePath, String fileName) {
        createDialog(filePath, fileName);
    }

    private void createDialog(String filePath, String fileName) {
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Preview: " + fileName);
        stage.setWidth(550);
        stage.setHeight(400);
        stage.setResizable(true);
        stage.setMinWidth(500);
        stage.setMinHeight(350);
        stage.setOnCloseRequest(e -> dispose());

        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: -fx-bg-base;"); // Dark theme base

        Label header = new Label("Audio Preview");
        header.getStyleClass().add(Styles.TITLE_3);
        header.setStyle("-fx-text-fill: -fx-text-primary;");

        // Media player component
        mediaPlayer = new MediaPlayerComponent();

        Button closeBtn = new Button("Close Player");
        closeBtn.getStyleClass().addAll(Styles.BUTTON_OUTLINED);
        closeBtn.setPrefWidth(200);
        closeBtn.setOnAction(e -> stage.close());

        root.getChildren().addAll(header, mediaPlayer, closeBtn);

        Scene scene = new Scene(root);
        stage.setScene(scene);

        // Load the audio file
        mediaPlayer.loadFile(filePath);
        mediaPlayer.setOnError(error -> header.setText("Error: " + error));
    }

    public void show() {
        stage.show();
    }

    public void dispose() {
        if (mediaPlayer != null)
            mediaPlayer.dispose();
        if (stage != null)
            stage.close();
    }

    public static void showForFile(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists())
                return;
            AudioPlayerDialog dialog = new AudioPlayerDialog(filePath, file.getName());
            dialog.show();
        } catch (Exception ignored) {
        }
    }
}
