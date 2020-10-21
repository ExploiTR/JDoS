package jdos.exploitr;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {

    @FXML
    TextField pto;
    @FXML
    TextField trc;
    @FXML
    TextField pbs;
    @FXML
    Button save_set;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pto.textProperty().addListener(listener(pto));
        trc.textProperty().addListener(listener(trc));
        pbs.textProperty().addListener(listener(pbs));

        pto.setText("" + Attack.timeOut);
        trc.setText("" + Attack.THREAD_COUNT);
        pbs.setText("" + Attack.byteSize);
    }

    private ChangeListener<String> listener(TextField field) {
        return (observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                field.setText(newValue.replaceAll("[^\\d]", ""));
            }
            if (field.getId().equals(pto.getId())) {
                if (newValue.length() > 5) {
                    pto.setText(oldValue);
                }
            } else if (field.getId().equals(trc.getId())) {
                if (newValue.length() > 4) {
                    trc.setText(oldValue);
                }
            } else {
                if (newValue.length() > 5) {
                    pbs.setText(oldValue);
                }
            }
        };
    }

    @FXML
    private void saveSettings() {
        Attack.timeOut = Integer.parseInt(pto.getText());
        Attack.byteSize = Integer.parseInt(pbs.getText());
        Attack.THREAD_COUNT = Integer.parseInt(trc.getText());
        try {
            Main.checkSettings(true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ((Stage)save_set.getScene().getWindow()).close();
    }
}
