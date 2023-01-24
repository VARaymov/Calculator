package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainWindow extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(MainWindow.class.getResource("/mainWindow-view.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Simple Calculator");
        stage.show();
    }

    public void run() {
        launch();
    }

    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow();
        mainWindow.run();
    }
}
