package br.cesul.musicbattle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        var url = getClass().getResource("/br/cesul/musicbattle/MusicBattleView.fxml");
        var root = FXMLLoader.load(url);
        stage.setTitle("Music Battle");
        stage.setScene(new Scene((Parent) root));
        stage.show();
    }
    public static void main(String[] args) { launch(args); }
}
