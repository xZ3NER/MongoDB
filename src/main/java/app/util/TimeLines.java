package app.util;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TimeLines {
    public static void opacityTimeLine(Node component, double firstValue, double secondValue, double duration) {
        Timeline opacityTimeLine = new Timeline(
                new KeyFrame(
                        Duration.ZERO,
                        new KeyValue(component.opacityProperty(), firstValue)
                ),
                new KeyFrame(
                        Duration.seconds(duration),
                        new KeyValue(component.opacityProperty(), secondValue)
                )
        );
        opacityTimeLine.play();
    }

    public static void imageClear(ImageView imageView, double duration) {
        Timeline opacityTimeLine = new Timeline(
                new KeyFrame(
                        Duration.ZERO,
                        new KeyValue(imageView.opacityProperty(), 1)
                ),
                new KeyFrame(
                        Duration.seconds(duration),
                        new KeyValue(imageView.opacityProperty(), 0)
                )
        );
        opacityTimeLine.play();
        opacityTimeLine.setOnFinished(event -> {
            imageView.setImage(null);
        });
    }

    public static void stageDisplay(Stage stage, double duration) {
        Timeline opacityTimeLine = new Timeline(
                new KeyFrame(
                        Duration.ZERO,
                        new KeyValue(stage.opacityProperty(), 0)
                ),
                new KeyFrame(
                        Duration.seconds(duration),
                        new KeyValue(stage.opacityProperty(), 1)
                )
        );
        opacityTimeLine.play();
    }

    public static void stageClose(Stage stage, double duration) {
        Timeline opacityTimeLine = new Timeline(
                new KeyFrame(
                        Duration.ZERO,
                        new KeyValue(stage.opacityProperty(), 1)
                ),
                new KeyFrame(
                        Duration.seconds(duration),
                        new KeyValue(stage.opacityProperty(), 0)
                )
        );
        opacityTimeLine.play();

        opacityTimeLine.setOnFinished(event -> {
            stage.close();
        });
    }

    public static void stageMinimize(Stage stage, double duration) {

        Timeline opacityTimeLine = new Timeline(
                new KeyFrame(
                        Duration.ZERO,
                        new KeyValue(stage.opacityProperty(), 1)
                ),
                new KeyFrame(
                        Duration.seconds(duration),
                        new KeyValue(stage.opacityProperty(), 0)
                )
        );
        opacityTimeLine.play();

        opacityTimeLine.setOnFinished(event -> {
            stage.setIconified(true);
            stage.setOpacity(1);
        });
    }
}
