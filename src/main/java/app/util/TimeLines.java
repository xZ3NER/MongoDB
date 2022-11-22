package app.util;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class TimeLines{
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
}
