package jdos.exploitr;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.CacheHint;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Controller implements Initializable {

    static final ArrayList<Process> processArrayList = new ArrayList<>();

    @FXML
    Button settings;
    @FXML
    TextField host_id;
    @FXML
    ToggleButton start_attack;
    @FXML
    Text info;

    private boolean ignoreResponse = false;
    private boolean attackRunning = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        host_id.setCache(true);
        host_id.setCacheShape(true);
        host_id.setCacheHint(CacheHint.SPEED);
        //use later
    }

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
                            ExecutorService executor = Executors.newFixedThreadPool(Attack.THREAD_COUNT);
                            for (int i = 0; i <= Attack.THREAD_COUNT; i++) {
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
                    ExecutorService executor = Executors.newFixedThreadPool(Attack.THREAD_COUNT);
                    for (int i = 0; i <= Attack.THREAD_COUNT; i++) {
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

    @FXML
    private void openSettings() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("settings.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(Objects.requireNonNull(root), 260, 172);
            stage.setTitle("JDoS | Settings");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
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
