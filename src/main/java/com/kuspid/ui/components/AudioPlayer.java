package com.kuspid.ui.components;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.application.Platform;
import javafx.util.Duration;

import java.io.File;

/**
 * Robust audio player wrapper using JavaFX Media API.
 */
public class AudioPlayer {
    private MediaPlayer mediaPlayer;
    private String currentFile;
    private Runnable onPlaybackEnded;
    private Runnable onPlaybackStarted;
    private java.util.function.Consumer<Duration> onProgressUpdate;

    public AudioPlayer() {
        this.currentFile = null;
        this.mediaPlayer = null;
    }

    /**
     * Load an audio file for playback.
     */
    public void loadFile(String filePath) throws Exception {
        dispose();

        File file = new File(filePath);
        if (!file.exists()) {
            throw new IllegalArgumentException("File not found: " + filePath);
        }

        this.currentFile = filePath;
        Media media = new Media(file.toURI().toString());
        mediaPlayer = new MediaPlayer(media);

        // Setup listeners
        mediaPlayer.setOnEndOfMedia(() -> {
            if (onPlaybackEnded != null) {
                Platform.runLater(onPlaybackEnded);
            }
        });

        mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
            if (onProgressUpdate != null) {
                Platform.runLater(() -> onProgressUpdate.accept(newTime));
            }
        });
    }

    /**
     * Start playback from current position.
     */
    public void play() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
            if (onPlaybackStarted != null) {
                onPlaybackStarted.run();
            }
        }
    }

    /**
     * Pause playback.
     */
    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    /**
     * Stop playback and reset to beginning.
     */
    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    /**
     * Seek to a specific time (in seconds).
     */
    public void seek(double seconds) {
        if (mediaPlayer != null) {
            mediaPlayer.seek(Duration.seconds(seconds));
        }
    }

    /**
     * Set volume (0.0 to 1.0).
     */
    public void setVolume(double volume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
        }
    }

    /**
     * Get current volume.
     */
    public double getVolume() {
        return mediaPlayer != null ? mediaPlayer.getVolume() : 1.0;
    }

    /**
     * Get total duration of current media (in seconds).
     */
    public double getDuration() {
        if (mediaPlayer != null && mediaPlayer.getMedia() != null) {
            return mediaPlayer.getMedia().getDuration().toSeconds();
        }
        return 0.0;
    }

    /**
     * Get current playback position (in seconds).
     */
    public double getCurrentTime() {
        return mediaPlayer != null ? mediaPlayer.getCurrentTime().toSeconds() : 0.0;
    }

    /**
     * Check if currently playing.
     */
    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }

    /**
     * Check if media is paused.
     */
    public boolean isPaused() {
        return mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED;
    }

    /**
     * Set callback for when playback ends.
     */
    public void setOnPlaybackEnded(Runnable callback) {
        this.onPlaybackEnded = callback;
    }

    /**
     * Set callback for when playback starts.
     */
    public void setOnPlaybackStarted(Runnable callback) {
        this.onPlaybackStarted = callback;
    }

    /**
     * Set callback for progress updates.
     */
    public void setOnProgressUpdate(java.util.function.Consumer<Duration> callback) {
        this.onProgressUpdate = callback;
    }

    /**
     * Get currently loaded file path.
     */
    public String getCurrentFile() {
        return currentFile;
    }

    /**
     * Clean up resources.
     */
    public void dispose() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
        }
        currentFile = null;
    }
}
