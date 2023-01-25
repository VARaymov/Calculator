package main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private final DBConnection connection = new DBConnection();
    @FXML
    private TextField result;
    @FXML
    private ListView<String> history;

    private enum Operation {
        PLUS, MINUS, ROOT_OF_NUMBER, MULTIPLICATION, DIVISION, EXPONENTIATION
    }

    private Operation operation;
    private Double firstNumber, secondNumber, value;

    public void command(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        String command = button.getId();
        switch (command) {
            case "zero":
                addNumber("0");
                break;
            case "one":
                addNumber("1");
                break;
            case "two":
                addNumber("2");
                break;
            case "three":
                addNumber("3");
                break;
            case "four":
                addNumber("4");
                break;
            case "five":
                addNumber("5");
                break;
            case "six":
                addNumber("6");
                break;
            case "seven":
                addNumber("7");
                break;
            case "eight":
                addNumber("8");
                break;
            case "nine":
                addNumber("9");
                break;
            case "point":
                addPoint();
                break;
            case "plusMinus":
                changeMinusSign();
                break;
            case "clear":
                clearResult();
                break;
            case "delete":
                deleteNumber();
                break;
            case "plus":
                addition();
                break;
            case "minus":
                subtraction();
                break;
            case "rootOfNumber":
                extractRoot();
                break;
            case "multiplication":
                multiply();
                break;
            case "division":
                division();
                break;
            case "exponentiation":
                raiseToDegree();
                break;
            case "equally":
                calculate();
                break;
            case "showHistory":
                showHideHistory();
                break;
        }
    }

    private void deleteNumber() {
        if (result.getText().length() > 0) {
            String text = result.getText().substring(0, result.getText().length() - 1);
            result.setText(text);
        }
    }

    private void addNumber(String number) {
        result.setText(result.getText() + number);
    }

    private void changeMinusSign() {
        if (result.getText().contains("-")) {
            result.setText(result.getText().replace("-", ""));
        } else {
            result.setText("-" + result.getText());
        }
    }

    private void addPoint() {
        if (result.getText().isEmpty()) {
            result.setText("0,");
        } else if (!result.getText().contains(",")) {
            result.setText(result.getText() + ",");
        }
    }

    private void clearResult() {
        result.setText("");
        value = null;
        firstNumber = null;
        secondNumber = null;
    }

    private void addition() {
        String text = result.getText();
        if (text.isEmpty()) {
            return;
        }
        if (text.contains(",")) {
            String replace = text.replace(",", ".");
            firstNumber = Double.parseDouble(replace);
        } else {
            firstNumber = Double.parseDouble(text);
        }
        result.setText("");
        operation = Operation.PLUS;
    }

    private void subtraction() {
        String text = result.getText();
        if (text.isEmpty()) {
            return;
        }
        if (text.contains(",")) {
            String replace = text.replace(",", ".");
            firstNumber = Double.parseDouble(replace);
        } else {
            firstNumber = Double.parseDouble(text);
        }
        result.setText("");
        operation = Operation.MINUS;
    }

    private void multiply() {
        String text = result.getText();
        if (text.isEmpty()) {
            return;
        }
        if (text.contains(",")) {
            String replace = text.replace(",", ".");
            firstNumber = Double.parseDouble(replace);
        } else {
            firstNumber = Double.parseDouble(text);
        }
        result.setText("");
        operation = Operation.MULTIPLICATION;
    }

    private void division() {
        String text = result.getText();
        if (text.isEmpty()) {
            return;
        }
        if (text.contains(",")) {
            String replace = text.replace(",", ".");
            firstNumber = Double.parseDouble(replace);
        } else {
            firstNumber = Double.parseDouble(text);
        }
        result.setText("");
        operation = Operation.DIVISION;
    }

    private void raiseToDegree() {
        String text = result.getText();
        if (text.isEmpty()) {
            return;
        }
        if (text.contains(",")) {
            String replace = text.replace(",", ".");
            firstNumber = Double.parseDouble(replace);
        } else {
            firstNumber = Double.parseDouble(text);
        }
        result.setText("");
        operation = Operation.EXPONENTIATION;
    }

    private void extractRoot() {
        firstNumber = Double.parseDouble(result.getText());
        operation = Operation.ROOT_OF_NUMBER;
        calculate();
    }

    private void calculate() {
        if (operation == null) {
            return;
        }
        if (!result.getText().isEmpty()) {
            secondNumber = Double.parseDouble(result.getText());
        }

        if (operation == Operation.PLUS) {
            value = firstNumber + secondNumber;
            connection.insertRecord(String.format("%f + %f = %f", firstNumber, secondNumber, value));
            result.setText(String.format("%f", value));
            updateHistory();
            operation = null;
        }

        if (operation == Operation.MINUS) {
            value = firstNumber - secondNumber;
            connection.insertRecord(String.format("%f - %f = %f", firstNumber, secondNumber, value));
            result.setText(String.format("%f", value));
            updateHistory();
            operation = null;
        }

        if (operation == Operation.MULTIPLICATION) {
            value = firstNumber * secondNumber;
            connection.insertRecord(String.format("%f * %f = %f", firstNumber, secondNumber, value));
            result.setText(String.format("%f", value));
            updateHistory();
            operation = null;
        }

        if (operation == Operation.DIVISION) {
            value = firstNumber / secondNumber;
            connection.insertRecord(String.format("%f / %f = %f", firstNumber, secondNumber, value));
            result.setText(String.format("%f", value));
            updateHistory();
            operation = null;
        }

        if (operation == Operation.EXPONENTIATION) {
            value = Math.pow(firstNumber, secondNumber);
            connection.insertRecord(String.format("%f ^ %f = %f", firstNumber, secondNumber, value));
            result.setText(String.format("%f", value));
            updateHistory();
            operation = null;
        }

        if (operation == Operation.ROOT_OF_NUMBER) {
            String text = result.getText();
            value = Math.sqrt(Double.parseDouble(text));
            result.setText(value.toString());
            connection.insertRecord(String.format("√%s = %s", text, value));
            updateHistory();
            operation = null;
        }
    }

    private void showHideHistory() {
        if (history.isVisible()) {
            history.setVisible(false);
        } else {
            updateHistory();
            history.setVisible(true);
        }
    }

    private void updateHistory() {
        try {
            ResultSet resultSet = connection.getRecords();
            history.getItems().clear();
            while (resultSet.next()) {
                String value = resultSet.getString("result");
                history.getItems().add(value);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        result.setEditable(false);
        history.setVisible(false);
    }
}
