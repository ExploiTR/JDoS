package jdos.main;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Controller {

    static final ArrayList<Process> processArrayList = new ArrayList<>();

    @FXML
    TextField host_id;
    @FXML
    ToggleButton start_attack;

    private boolean ignoreResponse = false;
    private boolean attackRunning = false;

    @FXML
    private void toggleResponse() {
        ignoreResponse = !ignoreResponse;
    }

    @FXML
    private void checkHostValidity() {
        if (new Attack(host_id.getText()).validIP()) {
            host_id.setStyle("-fx-border-color: green");
        } else {
            host_id.setStyle("-fx-border-color: red");
        }
    }

    @FXML
    private void startAttack() {
        if (attackRunning) {
            start_attack.setText("Start Attack");
            attackRunning = false;
            close();
        } else {
            if (!host_id.getText().isEmpty()) {
                if (!ignoreResponse) {
                    new Attack(host_id.getText()).checkIP(what -> {
                        if (what) {
                            Platform.runLater(() -> start_attack.setText("Stop Attack"));
                            attackRunning = true;
                            ExecutorService executor = Executors.newFixedThreadPool(100);
                            for (int i = 0; i <= 100; i++) {
                                Runnable worker = new Attack(host_id.getText());
                                executor.execute(worker);
                            }
                        } else {
                            showAlert(Alert.AlertType.ERROR, "Can't Reach IP Address");
                        }
                    });
                } else {
                    start_attack.setText("Stop Attack");
                    attackRunning = true;
                    ExecutorService executor = Executors.newFixedThreadPool(100);
                    for (int i = 0; i <= 100; i++) {
                        Runnable worker = new Attack(host_id.getText());
                        executor.execute(worker);
                    }
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "Invalid Host");
                start_attack.setSelected(false);
            }
        }
    }

    void close() {
        new Thread(() -> {
            for (Process _local : processArrayList.toArray(new Process[0])) {
                _local.destroy();
            }
        }).start();
    }

    private void showAlert(Alert.AlertType type, String what) {
        Platform.runLater(() -> {
                    Alert alert = new Alert(type);
                    alert.setTitle(type.name());
                    alert.setHeaderText(what);
                    alert.showAndWait();
                }
        );
    }
}
