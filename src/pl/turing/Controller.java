package pl.turing;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Controller {

    @FXML
    private HBox tapePane;

    @FXML
    private Button loadButton;

    @FXML
    private Button nextButton;

    @FXML
    private Button autoButton;

    @FXML
    private TextField inputTextField;

    @FXML
    private TextArea historyField;

    @FXML
    private Label currentStateLabel;

    @FXML
    private Button stopButton;

    private TuringMachine turingMachine;
    private List<Button> symbolIcons;

    Timer timer = new Timer();

    @FXML
    public void initialize() {
        loadButton.setOnAction(event -> {
            try {
                turingMachine = new TuringMachine(inputTextField.getText());
                setTapePane();
                historyField.setText(turingMachine.getHistory());
                currentStateLabel.setText(turingMachine.getCurrentState().name);
            } catch (InvalidInputException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("No valid input provided");
                alert.setContentText("Please provide valid input to load");
                alert.showAndWait();
            }
        });

        nextButton.setOnAction(event -> {
            if (turingMachine == null) {
                showNoInputLoadedError();
            } else {
                turingMachine.proccess();
                setTapePane();
                historyField.setText(turingMachine.getHistory());
                currentStateLabel.setText(turingMachine.getCurrentState().name);
            }
        });

        autoButton.setOnAction(event -> {
            if (turingMachine == null) {
                showNoInputLoadedError();
            } else {
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Platform.runLater(() -> {
                            turingMachine.proccess();
                            setTapePane();
                            historyField.setText(turingMachine.getHistory());
                            currentStateLabel.setText(turingMachine.getCurrentState().name);
                        });
                    }
                }, 0, 1000);
            }
        });

        stopButton.setOnAction(event -> {
            timer.cancel();
            timer = new Timer();
        });
    }

    private void showNoInputLoadedError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("No valid input loaded");
        alert.setContentText("Please load valid input before attempting to process");
        alert.showAndWait();
    }

    private void setTapePane() {
        tapePane.getChildren().clear();
        List<Symbol> tape = turingMachine.getTape();
        int currentTapePosition = turingMachine.getCurrentTapePosition();
        symbolIcons = new ArrayList<>();
        for (Symbol symbol : tape) {
            Button symbolIcon = getSymbolIcon(symbol);
            symbolIcons.add(symbolIcon);
            tapePane.getChildren().add(symbolIcon);
        }
        symbolIcons.get(currentTapePosition).setDisable(false);
    }

    private Button getSymbolIcon(Symbol symbol) {
        Button stateIcon = new Button();
        stateIcon.setDisable(true);
        stateIcon.setText(String.valueOf(symbol.symbolIcon));
        return stateIcon;
    }

}
