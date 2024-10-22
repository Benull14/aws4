package com.aws.view;

import com.aws.model.Aufgabe;
import com.aws.service.AufgabenService;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class AufgabenGUI extends Application {

    private AufgabenService aufgabenService;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        aufgabenService = new AufgabenService();

        showMainMenu();
    }

    private void showMainMenu() {
        // Layout
        VBox root = new VBox(10);
        root.setPadding(new Insets(15));

        Label titleLabel = new Label("Aufgabenverwaltung");

        Button addTaskButton = new Button("Aufgabe hinzufügen");
        addTaskButton.setOnAction(e -> showAddTaskView());

        Button showTasksButton = new Button("Alle Aufgaben anzeigen");
        showTasksButton.setOnAction(e -> showTaskListView());

        root.getChildren().addAll(titleLabel, addTaskButton, showTasksButton);

        Scene scene = new Scene(root, 300, 200);
        primaryStage.setTitle("Aufgabenverwaltung - Hauptmenü");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showAddTaskView() {
        // Layout
        VBox root = new VBox(10);
        root.setPadding(new Insets(15));

        // Title input
        TextField titelField = new TextField();
        titelField.setPromptText("Titel eingeben");

        // Description input
        TextArea beschreibungArea = new TextArea();
        beschreibungArea.setPromptText("Beschreibung eingeben");

        // Priority dropdown
        ComboBox<String> prioritaetBox = new ComboBox<>();
        prioritaetBox.getItems().addAll("Hoch", "Mittel", "Niedrig");
        prioritaetBox.setValue("Mittel");

        // Add task button
        Button addButton = new Button("Aufgabe hinzufügen");
        addButton.setOnAction(e -> {
            String titel = titelField.getText();
            String beschreibung = beschreibungArea.getText();
            int prioritaet = prioritaetBox.getSelectionModel().getSelectedIndex() + 1;

            if (titel.isEmpty()) {
                showAlert("Fehler", "Titel darf nicht leer sein.");
                return;
            }

            Aufgabe aufgabe = new Aufgabe(titel, beschreibung, prioritaet);
            aufgabenService.aufgabeHinzufuegen(aufgabe);

            showAlert("Erfolg", "Aufgabe hinzugefügt.");

            // Zurück zum Hauptmenü
            showMainMenu();
        });

        Button cancelButton = new Button("Abbrechen");
        cancelButton.setOnAction(e -> showMainMenu());

        HBox buttonBox = new HBox(10, addButton, cancelButton);

        root.getChildren().addAll(
                new Label("Neue Aufgabe hinzufügen"),
                titelField,
                beschreibungArea,
                prioritaetBox,
                buttonBox
        );

        Scene scene = new Scene(root, 400, 400);
        primaryStage.setTitle("Aufgabe hinzufügen");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showTaskListView() {
        // Layout
        VBox root = new VBox(10);
        root.setPadding(new Insets(15));

        // TableView for tasks
        TableView<Aufgabe> tableView = new TableView<>();

        // Columns
        TableColumn<Aufgabe, String> titelColumn = new TableColumn<>("Titel");
        titelColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTitel()));

        TableColumn<Aufgabe, String> beschreibungColumn = new TableColumn<>("Beschreibung");
        beschreibungColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getBeschreibung()));

        TableColumn<Aufgabe, String> prioritaetColumn = new TableColumn<>("Priorität");
        prioritaetColumn.setCellValueFactory(cellData -> {
            int prio = cellData.getValue().getPrioritaet();
            String prioText = switch (prio) {
                case 1 -> "Hoch";
                case 2 -> "Mittel";
                case 3 -> "Niedrig";
                default -> "Unbekannt";
            };
            return new javafx.beans.property.SimpleStringProperty(prioText);
        });

        TableColumn<Aufgabe, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().isErledigt() ? "Erledigt" : "Offen"
        ));

        tableView.getColumns().addAll(titelColumn, beschreibungColumn, prioritaetColumn, statusColumn);

        // Daten laden
        tableView.getItems().addAll(aufgabenService.getAufgabenListe());

        // Buttons
        Button deleteButton = new Button("Aufgabe löschen");
        deleteButton.setOnAction(e -> {
            Aufgabe selectedTask = tableView.getSelectionModel().getSelectedItem();
            if (selectedTask != null) {
                aufgabenService.aufgabeLoeschen(selectedTask.getTitel());
                showAlert("Erfolg", "Aufgabe gelöscht.");
                showTaskListView();
            } else {
                showAlert("Fehler", "Bitte wählen Sie eine Aufgabe aus.");
            }
        });

        Button markDoneButton = new Button("Als erledigt markieren");
        markDoneButton.setOnAction(e -> {
            Aufgabe selectedTask = tableView.getSelectionModel().getSelectedItem();
            if (selectedTask != null) {
                aufgabenService.aufgabeAlsErledigtMarkieren(selectedTask.getTitel());
                showAlert("Erfolg", "Aufgabe als erledigt markiert.");
                showTaskListView();
            } else {
                showAlert("Fehler", "Bitte wählen Sie eine Aufgabe aus.");
            }
        });

        Button addButton = new Button("Neue Aufgabe hinzufügen");
        addButton.setOnAction(e -> showAddTaskView());

        Button backButton = new Button("Zurück zum Hauptmenü");
        backButton.setOnAction(e -> showMainMenu());

        HBox buttonBox = new HBox(10, deleteButton, markDoneButton, addButton, backButton);

        root.getChildren().addAll(new Label("Alle Aufgaben"), tableView, buttonBox);

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("Alle Aufgaben anzeigen");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void stop() throws Exception {
        aufgabenService.close();
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
