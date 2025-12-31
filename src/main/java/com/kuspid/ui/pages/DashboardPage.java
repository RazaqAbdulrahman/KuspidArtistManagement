package com.kuspid.ui.pages;

import com.kuspid.service.ArtistService;
import com.kuspid.service.BeatService;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.FlowPane;

/**
 * Dashboard Page - Main landing page for the application.
 * Shows quick statistics and overview of artists and beats.
 */
public class DashboardPage extends ScrollPane {
    private final BeatService beatService = BeatService.getInstance();
    private final ArtistService artistService = new ArtistService();
    private FlowPane statsGrid;

    public DashboardPage() {
        setFitToWidth(true);
        getStyleClass().add("page-container");
        setStyle("-fx-background-color: transparent; -fx-padding: 0;");

        VBox content = new VBox(40);
        content.setPadding(new Insets(40));
        content.setMaxWidth(1200);
        content.setAlignment(Pos.TOP_CENTER);

        statsGrid = createStatsSection();
        content.getChildren().addAll(
                createHeroSection(),
                statsGrid,
                createQuickActionsSection());

        setContent(content);
    }

    private void reloadStats() {
        if (statsGrid != null) {
            long artistCount = artistService.countArtists();
            long beatCount = beatService.countBeats();

            Platform.runLater(() -> {
                statsGrid.getChildren().clear();
                statsGrid.getChildren().addAll(
                        createStatCard("Total Artists", String.valueOf(artistCount), "👥", "#6366f1"),
                        createStatCard("Total Beats", String.valueOf(beatCount), "🎵", "#f43f5e"));
            });
        }
    }

    private void navigateTo(Node page) {
        com.kuspid.ui.layout.RootLayout root = findRootLayout();
        if (root != null) {
            root.setContent(page);
        }
    }

    private com.kuspid.ui.layout.RootLayout findRootLayout() {
        javafx.scene.Parent parent = getParent();
        while (parent != null) {
            if (parent instanceof com.kuspid.ui.layout.RootLayout) {
                return (com.kuspid.ui.layout.RootLayout) parent;
            }
            parent = parent.getParent();
        }
        return null;
    }

    private VBox createHeroSection() {
        VBox hero = new VBox(15);
        hero.setPadding(new Insets(40));
        hero.setAlignment(Pos.CENTER_LEFT);
        // Modern gradient background
        hero.setStyle("-fx-background-color: linear-gradient(to right, #2b5876, #4e4376);" +
                "-fx-background-radius: 15;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 5);");

        Label title = new Label("Welcome Back, User");
        title.setStyle(
                "-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: white; -fx-font-family: 'Segoe UI', sans-serif;");

        Label subtitle = new Label("Here's what's happening in your studio today.");
        subtitle.setStyle("-fx-font-size: 16px; -fx-text-fill: rgba(255,255,255,0.8);");

        hero.getChildren().addAll(title, subtitle);
        return hero;
    }

    private FlowPane createStatsSection() {
        FlowPane statsGrid = new FlowPane(30, 30);
        statsGrid.setAlignment(Pos.CENTER);

        // Efficiently load statistics
        long artistCount = artistService.countArtists();
        long beatCount = beatService.countBeats();

        statsGrid.getChildren().addAll(
                createStatCard("Total Artists", String.valueOf(artistCount), "👥", "#6366f1"),
                createStatCard("Total Beats", String.valueOf(beatCount), "🎵", "#f43f5e"));

        return statsGrid;
    }

    private VBox createStatCard(String title, String value, String icon, String accentColor) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(25));
        card.setPrefWidth(250);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle("-fx-background-color: white;" +
                "-fx-background-radius: 12;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 4);");

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 28px; -fx-text-fill: " + accentColor + ";");

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: #1f2937;");

        Label titleLabel = new Label(title);
        titleLabel.setStyle(
                "-fx-font-size: 14px; -fx-text-fill: #6b7280; -fx-font-weight: 600; text-transform: uppercase;");

        card.getChildren().addAll(iconLabel, valueLabel, titleLabel);
        return card;
    }

    private VBox createQuickActionsSection() {
        VBox section = new VBox(20);
        section.setAlignment(Pos.TOP_LEFT);

        Label sectionTitle = new Label("Quick Actions");
        sectionTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #374151;");

        FlowPane actions = new FlowPane(20, 20);
        actions.setAlignment(Pos.TOP_LEFT);
        actions.getChildren().addAll(
                createActionCard("Add New Beat", "Import a new track", "➕", () -> {
                    AddBeatPage.show(null, this::reloadStats);
                }),
                createActionCard("New Artist", "Register a contact", "👤", () -> {
                    navigateTo(new ArtistsPage());
                }),
                createActionCard("Send Pack", "Email beats", "📨", () -> {
                    navigateTo(new CommunicationsPage());
                }));

        section.getChildren().addAll(sectionTitle, actions);
        return section;
    }

    private VBox createActionCard(String title, String desc, String icon, Runnable action) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(20));
        card.setPrefWidth(200);
        card.setStyle("-fx-background-color: white;" +
                "-fx-background-radius: 12;" +
                "-fx-cursor: hand;" +
                "-fx-border-color: transparent;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 5, 0, 0, 2);");

        // Hover effect helper (pseudo-code logic, implemented via style property
        // update)
        card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: white;" +
                "-fx-background-radius: 12;" +
                "-fx-cursor: hand;" +
                "-fx-border-color: #6366f1;" +
                "-fx-border-width: 2;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(99, 102, 241, 0.2), 10, 0, 0, 4);"));

        card.setOnMouseExited(e -> card.setStyle("-fx-background-color: white;" +
                "-fx-background-radius: 12;" +
                "-fx-cursor: hand;" +
                "-fx-border-color: transparent;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 5, 0, 0, 2);"));

        card.setOnMouseClicked(e -> action.run());

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 24px;");

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #111827;");

        Label descLabel = new Label(desc);
        descLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #6b7280;");

        card.getChildren().addAll(iconLabel, titleLabel, descLabel);
        return card;
    }
}
