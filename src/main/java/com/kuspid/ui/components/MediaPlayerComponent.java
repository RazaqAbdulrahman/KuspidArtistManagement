package com.kuspid.ui.components;

import atlantafx.base.theme.Styles;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import java.io.File;
import java.util.function.Consumer;

/**
 * Premium media player UI component.
 */
public class MediaPlayerComponent extends VBox {
    private final AudioPlayer audioPlayer;
    private final com.kuspid.db.BeatRepository beatRepository = new com.kuspid.db.BeatRepository();
    private Label fileNameLabel;
    private Label timeLabel;
    private Slider progressSlider;
    private WaveformCanvas waveformCanvas;
    private Slider volumeSlider;
    private Button playPauseBtn;
    private Button stopBtn;
    private boolean isDraggingSlider = false;
    private Consumer<String> onError;

    public MediaPlayerComponent() {
        this.audioPlayer = new AudioPlayer();
        initializeUI();
        setupEventHandlers();
    }

    private void initializeUI() {
        setPadding(new Insets(24));
        setSpacing(20);
        getStyleClass().add("card");
        setStyle(
                "-fx-background-color: -fx-bg-surface; -fx-background-radius: 16; -fx-border-color: -fx-border-default; -fx-border-width: 1;");

        // File name and info
        VBox metaBox = new VBox(5);
        fileNameLabel = new Label("No file loaded");
        fileNameLabel.getStyleClass().addAll(Styles.TITLE_4);
        fileNameLabel.setStyle("-fx-text-fill: -fx-text-primary;");

        Label statusLabel = new Label("NOW PREVIEWING");
        statusLabel.getStyleClass().addAll(Styles.TEXT_SMALL, Styles.TEXT_MUTED);
        statusLabel.setStyle("-fx-font-weight: bold; -fx-letter-spacing: 1.5px; -fx-text-fill: -fx-primary;");
        metaBox.getChildren().addAll(statusLabel, fileNameLabel);

        // Progress section
        VBox progressSection = new VBox(10);

        // Waveform
        waveformCanvas = new WaveformCanvas(600, 80);
        waveformCanvas.widthProperty().bind(progressSection.widthProperty());
        waveformCanvas.setAccentColor(com.kuspid.ui.theme.ThemeService.getInstance().getAccentColor());

        progressSlider = new Slider(0, 100, 0);
        progressSlider.getStyleClass().add("player-slider");

        // Time labels
        HBox timeBox = new HBox();
        timeLabel = new Label("00:00 / 00:00");
        timeLabel.getStyleClass().addAll(Styles.TEXT_SMALL, Styles.TEXT_MUTED);
        timeLabel.setStyle("-fx-font-family: 'monospace';");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        timeBox.getChildren().addAll(spacer, timeLabel);
        progressSection.getChildren().addAll(waveformCanvas, progressSlider, timeBox);

        // Controls
        HBox controls = new HBox(24);
        controls.setAlignment(Pos.CENTER);

        playPauseBtn = new Button("▶");
        playPauseBtn.getStyleClass().addAll(Styles.BUTTON_CIRCLE, Styles.SUCCESS, Styles.LARGE);
        playPauseBtn.setPrefSize(60, 60);
        playPauseBtn.setStyle(
                "-fx-font-size: 26px; -fx-text-fill: white; -fx-cursor: hand; -fx-font-family: 'Segoe UI Symbol', 'system-ui';");
        playPauseBtn.setOnAction(e -> togglePlayPause());

        stopBtn = new Button("■");
        stopBtn.getStyleClass().addAll(Styles.BUTTON_CIRCLE, Styles.DANGER);
        stopBtn.setPrefSize(40, 40);
        stopBtn.setStyle(
                "-fx-font-size: 18px; -fx-text-fill: white; -fx-cursor: hand; -fx-font-family: 'Segoe UI Symbol', 'system-ui';");
        stopBtn.setOnAction(e -> stop());

        // Volume controls
        HBox volumeBox = new HBox(12);
        volumeBox.setAlignment(Pos.CENTER_LEFT);
        Label volumeIcon = new Label("🔊");
        volumeIcon.setStyle("-fx-text-fill: -fx-text-muted; -fx-font-size: 16px;");
        volumeSlider = new Slider(0, 100, 80);
        volumeSlider.setPrefWidth(120);
        volumeSlider.getStyleClass().add("player-slider");
        volumeBox.getChildren().addAll(volumeIcon, volumeSlider);

        Region controlSpacer = new Region();
        HBox.setHgrow(controlSpacer, Priority.ALWAYS);

        controls.getChildren().addAll(volumeBox, playPauseBtn, stopBtn);

        getChildren().addAll(metaBox, progressSection, controls);

        // Slider logic
        progressSlider.setOnMousePressed(e -> isDraggingSlider = true);
        progressSlider.setOnMouseReleased(e -> {
            isDraggingSlider = false;
            double duration = audioPlayer.getDuration();
            if (duration > 0) {
                audioPlayer.seek((progressSlider.getValue() / 100.0) * duration);
            }
        });

        volumeSlider.valueProperty().addListener((obs, old, val) -> audioPlayer.setVolume(val.doubleValue() / 100.0));
    }

    private void setupEventHandlers() {
        audioPlayer.setOnProgressUpdate(currentTime -> {
            if (!isDraggingSlider) {
                double duration = audioPlayer.getDuration();
                if (duration > 0) {
                    double progress = currentTime.toSeconds() / duration;
                    progressSlider.setValue(progress * 100);
                    waveformCanvas.setProgress(progress);
                }
                updateTimeLabel();
            }
        });

        audioPlayer.setOnPlaybackEnded(() -> {
            Platform.runLater(() -> {
                playPauseBtn.setText("▶");
                playPauseBtn.getStyleClass().removeAll(Styles.WARNING);
                playPauseBtn.getStyleClass().add(Styles.SUCCESS);
                progressSlider.setValue(0);
                updateTimeLabel();
            });
        });

        audioPlayer.setOnPlaybackStarted(() -> {
            Platform.runLater(() -> {
                playPauseBtn.setText("⏸");
                playPauseBtn.getStyleClass().removeAll(Styles.SUCCESS);
                playPauseBtn.getStyleClass().add(Styles.WARNING);
            });
        });
    }

    private void togglePlayPause() {
        if (audioPlayer.isPlaying()) {
            audioPlayer.pause();
            playPauseBtn.setText("▶");
            playPauseBtn.getStyleClass().removeAll(Styles.WARNING);
            playPauseBtn.getStyleClass().add(Styles.SUCCESS);
        } else {
            audioPlayer.play();
        }
    }

    public void loadFile(String filePath) {
        try {
            audioPlayer.loadFile(filePath);
            File f = new File(filePath);
            fileNameLabel.setText(f.getName());
            progressSlider.setValue(0);
            waveformCanvas.setProgress(0);

            // Start async analysis
            com.kuspid.util.AudioPeakAnalyzer.extractPeaks(f, 200)
                    .thenAccept(peaks -> Platform.runLater(() -> {
                        waveformCanvas.setPeaks(peaks);

                        // Save fingerprint to DB for Sonic Search if not already present
                        com.kuspid.model.Beat currentBeat = beatRepository.findByFilePath(filePath);
                        if (currentBeat != null && (currentBeat.getSonicFingerprint() == null
                                || currentBeat.getSonicFingerprint().isEmpty())) {
                            currentBeat
                                    .setSonicFingerprint(com.kuspid.util.AudioPeakAnalyzer.summarizeFingerprint(peaks));
                            beatRepository.update(currentBeat);
                            System.out.println("AI Profile indexed for: " + currentBeat.getTitle());
                        }
                    }));

            updateTimeLabel();

        } catch (Exception e) {
            if (onError != null)
                onError.accept(e.getMessage());
        }
    }

    public void stop() {
        audioPlayer.stop();
        playPauseBtn.setText("▶");
        playPauseBtn.getStyleClass().removeAll(Styles.WARNING);
        playPauseBtn.getStyleClass().add(Styles.SUCCESS);
        progressSlider.setValue(0);
        updateTimeLabel();
    }

    private void updateTimeLabel() {
        timeLabel.setText(formatTime(audioPlayer.getCurrentTime()) + " / " + formatTime(audioPlayer.getDuration()));
    }

    private String formatTime(double seconds) {
        int s = (int) seconds;
        return String.format("%02d:%02d", s / 60, s % 60);
    }

    public void setOnError(Consumer<String> callback) {
        this.onError = callback;
    }

    public void dispose() {
        audioPlayer.dispose();
    }
}
