package app;

import app.util.TimeLines;
import app.util.Toast;
import com.mongodb.client.MongoCollection;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import static app.util.TimeLines.opacityTimeLine;

public class DDBB_InsertController implements Initializable {
    @FXML
    private StackPane imagePane;
    @FXML
    private AnchorPane mainPane;
    @FXML
    private ImageView imageView;
    @FXML
    private TextField imageURLInput;
    @FXML
    private TextField titleInput;
    @FXML
    private TextArea postBodyInput;
    @FXML
    private ImageView logoIcon;
    @FXML
    private ImageView exitIcon;
    @FXML
    private ImageView minimizeIcon;

    @FXML
    protected void confirmButtonHandler() {

        if (!(titleInput.getText().isEmpty()) && (isImage()!=null)) {
            MongoCollection<Document> coll = LoginController.mongoConnector.getCollection("posts");
            ObjectId objectId = new ObjectId();
            Document doc = new Document("_id", objectId)
                    .append("creator_id", LoginController.username)
                    .append("created", LocalDateTime.now())
                    .append("img", imageURLInput.getText())
                    .append("post_info",
                            new Document("title", titleInput.getText()).append("post_body", postBodyInput.getText())
                    );
            coll.insertOne(doc);

            changeStage(objectId, imageURLInput.getText());
        } else {
            Toast.makeText((Stage) mainPane.getScene().getWindow(), "Creation process failed", 1000, 300, 300);
        }

    }

    private DDBB_Controller controller;

    private void changeStage(ObjectId id, String imageUrl) {
        controller.setNewPost(id, imageUrl);

        TimeLines.stageClose((Stage) mainPane.getScene().getWindow(),  0.3);
    }

    public void initController(DDBB_Controller controller) {
        this.controller = controller;
    }


    @FXML
    private void cleanImageURL() {
        imageURLInput.setText("");

        TimeLines.imageClear(imageView,0.3);
    }

    @FXML
    private void cleanTittle() {
        titleInput.setText("");
    }

    @FXML
    private void imagePreview() {
        this.imagePane.getChildren().clear();

        ImageView iv;

        if((iv=isImage())!=null){
            Rectangle roundRect = new Rectangle();
            roundRect.arcWidthProperty().bind(roundRect.heightProperty().divide(16));
            roundRect.arcHeightProperty().bind(roundRect.heightProperty().divide(16));
            roundRect.setWidth(iv.getLayoutBounds().getWidth());
            roundRect.setHeight(iv.getLayoutBounds().getHeight());

            iv.setClip(roundRect);

            Group group = new Group(iv);
            DropShadow dropShadow = new DropShadow();
            dropShadow.setWidth(15);
            dropShadow.setHeight(15);
            dropShadow.setRadius(7);
            dropShadow.setColor(Color.web("#000000b3"));
            group.setEffect(dropShadow);

            this.imagePane.getChildren().add(group);
            this.imagePane.setAlignment(Pos.CENTER);

            TimeLines.opacityTimeLine(iv,0,1,0.3);
        }
    }

    private ImageView isImage() {
        try {

            Image image = new Image(imageURLInput.getText());
            ImageView imageView = this.imageView;
            imageView.setImage(image);

            return imageView;
        } catch (IllegalArgumentException ignored) {
            Toast.makeText((Stage) mainPane.getScene().getWindow(),"Image not found", 1000, 300, 300);
        }
        return null;
    }


    @FXML
    private Pane dragablePane;
    private double x, y;

    @FXML
    protected void dragHandler(MouseEvent mouseEvent) {
        Stage stage = (Stage) mainPane.getScene().getWindow();
        stage.setX(mouseEvent.getScreenX() - x);
        stage.setY(mouseEvent.getScreenY() - y);
    }

    @FXML
    protected void pressHandler(MouseEvent mouseEvent) {
        x = mouseEvent.getSceneX();
        y = mouseEvent.getSceneY();
        dragablePane.setCursor(Cursor.CLOSED_HAND);
    }

    @FXML
    protected void releasedHandler() {
        dragablePane.setCursor(Cursor.OPEN_HAND);
    }

    @FXML
    public void minimizeHandler() {
        TimeLines.stageMinimize((Stage) mainPane.getScene().getWindow(), 0.2);
    }

    @FXML
    public void exitHandler() {
        TimeLines.stageClose((Stage) mainPane.getScene().getWindow(), 0.2);
    }


    @FXML
    private void onMouseEnteredApp() {
        opacityTimeLine(logoIcon, logoIcon.getOpacity(), 1, 1);
    }

    @FXML
    private void onMouseExitedApp() {
        opacityTimeLine(logoIcon, logoIcon.getOpacity(), 0, 1);
    }

    @FXML
    private void onMouseEnteredExitIcon() {
        opacityTimeLine(exitIcon, exitIcon.getOpacity(), 0.8, 1);
    }

    @FXML
    private void onMouseExitedExitIcon() {
        opacityTimeLine(exitIcon, exitIcon.getOpacity(), 1, 1);
    }

    @FXML
    private void onMouseEnteredMinimizeIcon() {
        opacityTimeLine(minimizeIcon, minimizeIcon.getOpacity(), 0.8, 1);
    }

    @FXML
    private void onMouseExitedMinimizeIcon() {
        opacityTimeLine(minimizeIcon, minimizeIcon.getOpacity(), 1, 1);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        int maxLength = 40;

        titleInput.textProperty().addListener((observableValue, s, t1) -> {
            if (titleInput.getText().length() > maxLength) {
                String string = titleInput.getText().substring(0, maxLength);
                titleInput.setText(string);
            }
        });

    }
}
