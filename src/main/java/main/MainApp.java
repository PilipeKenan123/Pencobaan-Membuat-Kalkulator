package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Titik masuk aplikasi. Jalankan file ini (Run File / Shift+F6).
 */
public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/view/calculator.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 360, 580);
        scene.getStylesheets().add(MainApp.class.getResource("/view/calculator.css").toExternalForm());

        stage.setTitle("Kalkulator");
        stage.setMinWidth(300);
        stage.setMinHeight(480);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
