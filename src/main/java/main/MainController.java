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
    private Operation lastOperation;

    public void command(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        String command = button.getId();
        System.out.println(command);
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
            result.setText("0.");
        } else if (!result.getText().contains(".")) {
            result.setText(result.getText() + ".");
        }
    }

    private void clearResult() {
        result.setText("");
        value = null;
        firstNumber = null;
        secondNumber = null;
    }

    private void addition() {
        firstNumber = Double.parseDouble(result.getText());
        result.setText("");
        operation = Operation.PLUS;
    }

    private void subtraction() {
        firstNumber = Double.parseDouble(result.getText());
        result.setText("");
        operation = Operation.MINUS;
    }

    private void multiply() {
        firstNumber = Double.parseDouble(result.getText());
        result.setText("");
        operation = Operation.MULTIPLICATION;
    }

    private void division() {
        firstNumber = Double.parseDouble(result.getText());
        result.setText("");
        operation = Operation.DIVISION;
    }

    private void raiseToDegree() {
        firstNumber = Double.parseDouble(result.getText());
        result.setText("");
        operation = Operation.EXPONENTIATION;
    }

    private void extractRoot() { // извлечь корень
        firstNumber = Double.parseDouble(result.getText());
        operation = Operation.ROOT_OF_NUMBER;
        calculate();
    }

    private void calculate() {
        if (!result.getText().isEmpty() && !isOperationRepeat(operation)) {
            secondNumber = Double.parseDouble(result.getText());
        }

        if (operation == Operation.PLUS) {
            if (isOperationRepeat(Operation.PLUS)) {
                System.out.println("OPERATION IS REPEAT");
                Double temp = value;
                value += secondNumber;
                connection.insertRecord(String.format("%f + %f = %f", temp, secondNumber, value));
            } else {
                System.out.println("OPERATION IS NOT REPEAT");
                value = firstNumber + secondNumber;
                connection.insertRecord(String.format("%f + %f = %f", firstNumber, secondNumber, value));
                lastOperation = Operation.PLUS;
            }
            result.setText(String.valueOf(value));
            updateHistory();
        }

        if (operation == Operation.MINUS) {
            if (isOperationRepeat(Operation.MINUS)) {
                System.out.println("OPERATION IS REPEAT");
                Double temp = value;
                value -= secondNumber;
                connection.insertRecord(String.format("%f - %f = %f", temp, secondNumber, value));
            } else {
                System.out.println("OPERATION IS NOT REPEAT");
                value = firstNumber - secondNumber;
                connection.insertRecord(String.format("%f - %f = %f", firstNumber, secondNumber, value));
                lastOperation = Operation.MINUS;
            }
            result.setText(String.valueOf(value));
            updateHistory();
        }

        if (operation == Operation.MULTIPLICATION) {
            if (isOperationRepeat(Operation.MULTIPLICATION)) {
                System.out.println("OPERATION IS REPEAT");
                Double temp = value;
                value *= secondNumber;
                connection.insertRecord(String.format("%f * %f = %f", temp, secondNumber, value));
            } else {
                System.out.println("OPERATION IS NOT REPEAT");
                value = firstNumber * secondNumber;
                connection.insertRecord(String.format("%f * %f = %f", firstNumber, secondNumber, value));
                lastOperation = Operation.MULTIPLICATION;
            }
            result.setText(String.valueOf(value));
            updateHistory();
        }

        if (operation == Operation.DIVISION) {
            if (isOperationRepeat(Operation.DIVISION)) {
                System.out.println("OPERATION IS REPEAT");
                Double temp = value;
                value /= secondNumber;
                connection.insertRecord(String.format("%f / %f = %f", temp, secondNumber, value));
            } else {
                System.out.println("OPERATION IS NOT REPEAT");
                value = firstNumber / secondNumber;
                connection.insertRecord(String.format("%f / %f = %f", firstNumber, secondNumber, value));
                lastOperation = Operation.DIVISION;
            }
            result.setText(String.valueOf(value));
            updateHistory();
        }

        if (operation == Operation.EXPONENTIATION) {
            if (isOperationRepeat(Operation.EXPONENTIATION)) {
                System.out.println("OPERATION IS REPEAT");
                Double temp = value;
                value = Math.pow(value, secondNumber);
                connection.insertRecord(String.format("%f ^ %f = %f", temp, secondNumber, value));
            } else {
                System.out.println("OPERATION IS NOT REPEAT");
                value = Math.pow(firstNumber, secondNumber);
                connection.insertRecord(String.format("%f ^ %f = %f", firstNumber, secondNumber, value));
                lastOperation = Operation.EXPONENTIATION;
            }
            result.setText(String.valueOf(value));
            updateHistory();
        }

        if (operation == Operation.ROOT_OF_NUMBER) {
            String text = result.getText();
            value = Math.sqrt(Double.parseDouble(text));
            result.setText(value.toString());
            connection.insertRecord(String.format("√%s = %s", text, value));
            updateHistory();
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
                System.out.println(value);
                history.getItems().add(value);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean isOperationRepeat(Operation operation) {
        return operation == lastOperation;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        result.setEditable(false);
        history.setVisible(false);
    }
}
