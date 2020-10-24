package jdos.exploitr;

import com.sun.deploy.Environment;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Main extends Application {

    private static final String KEY_PTO = "pto";
    private static final String KEY_THC = "thc";
    private static final String KEY_PPS = "pps";
    private static final String KEY_PHC = "phc";

    static int DISPLAY_HEIGHT = 1080;
    static int DISPLAY_WIDTH = 1920;

    private static boolean saveEnabled = true;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("scene.fxml"));
        primaryStage.setTitle("JDoS | GitHub/ExploiTR | Apache 2.0");
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.show();
        primaryStage.centerOnScreen();
    }


    public static void main(String[] args) {
        try {
            checkSettings(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        checkRes();
        launch(args);
    }

    private static void checkRes() {
        GraphicsDevice gfx_d = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        DISPLAY_WIDTH = gfx_d.getDisplayMode().getWidth() > 0 ? gfx_d.getDisplayMode().getWidth() : DISPLAY_WIDTH;
        DISPLAY_HEIGHT = gfx_d.getDisplayMode().getHeight() > 0 ? gfx_d.getDisplayMode().getHeight() : DISPLAY_HEIGHT;
    }

    public static void checkSettings(boolean save_mode) throws IOException {
        File file = new File(Environment.getenv("TEMP") + File.separator + "JDoS");
        File actualFile = new File(file.getAbsolutePath() + File.separator + "settings.txt");

        System.out.println(actualFile.getAbsolutePath());

        if (save_mode) {
            if (saveEnabled)
                edit(actualFile);
            else
                throw new IOException("Read Error!");
            return;
        }

        if (actualFile.exists() && actualFile.canRead()) {
            FileReader reader = new FileReader(actualFile);
            StringBuilder builder = new StringBuilder();
            int read;
            while ((read = reader.read()) != -1) {
                builder.append((char) read);
            }
            JSONObject read_done = new JSONObject(builder.toString());
            Attack.THREAD_COUNT = read_done.getInt(KEY_THC);
            Attack.timeOut = read_done.getInt(KEY_PTO);
            Attack.byteSize = read_done.getInt(KEY_PPS);
            Attack.hopSize = read_done.getInt(KEY_PHC);
            edit(actualFile);
        } else if ((file.exists() || file.mkdirs()) && actualFile.createNewFile() && actualFile.canWrite()) {
            edit(actualFile);
        } else {
            saveEnabled = false;
        }
    }

    private static void edit(File actualFile) throws IOException {
        JSONObject object = new JSONObject();
        object.put(KEY_PTO, Attack.timeOut);
        object.put(KEY_PPS, Attack.byteSize);
        object.put(KEY_THC, Attack.THREAD_COUNT);
        object.put(KEY_PHC, Attack.hopSize);
        FileWriter writer = new FileWriter(actualFile);
        writer.write(object.toString());
        writer.flush();
    }
}
