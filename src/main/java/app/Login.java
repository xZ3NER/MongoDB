package app;

import app.util.TimeLines;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class Login extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.setFill(Color.TRANSPARENT);
        stage.initStyle(StageStyle.TRANSPARENT); //Barra superior
        stage.setTitle("LogIn");
        //Icono en la barra de windows
        stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResource("images/Logo_Style2.png")).openStream()));
        stage.setScene(scene);
        stage.show();
        TimeLines.stageDisplay(stage,  0.4);
    }
}
