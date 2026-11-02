package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import model.CalculatorModel;

/**
 * CONTROLLER: menghubungkan tombol di FXML (view) dengan CalculatorModel.
 */
public class CalculatorController {

    @FXML
    private Label display;

    private final CalculatorModel model = new CalculatorModel();

    @FXML
    private void initialize() {
        refresh();
        // Scene baru tersedia setelah FXML dipasang, jadi keyboard dipasang lewat listener.
        display.sceneProperty().addListener((obs, oldScene, scene) -> {
            if (scene != null) attachKeyboard(scene);
        });
    }

    /** Dipanggil semua tombol (onAction="#onButtonClick"). */
    @FXML
    private void onButtonClick(ActionEvent event) {
        Button b = (Button) event.getSource();
        press(b.getText());
    }

    private void press(String key) {
        model.press(key);
        refresh();
    }

    private void refresh() {
        String shown = model.getDisplay();
        display.setText(shown);

        // Kecilkan font kalau angkanya panjang
        int len = shown.length();
        int size = len <= 7 ? 64 : len <= 10 ? 48 : len <= 13 ? 38 : 30;
        display.setStyle("-fx-font-size: " + size + "px;");
    }

    private void attachKeyboard(Scene scene) {
        scene.setOnKeyTyped(e -> {
            switch (e.getCharacter()) {
                case "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" -> press(e.getCharacter());
                case ".", "," -> press(".");
                case "+" -> press("+");
                case "-" -> press("-");
                case "*" -> press("×");
                case "/" -> press("÷");
                case "%" -> press("%");
                case "=" -> press("=");
                default -> { }
            }
        });
        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case ENTER -> press("=");
                case ESCAPE, DELETE -> press("C");
                case BACK_SPACE -> press("⌫");
                default -> { }
            }
        });
    }
}
