package app;

import com.mongodb.client.MongoCollection;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.time.LocalDateTime;

import static app.util.TimeLines.opacityTimeLine;
import static com.mongodb.client.model.Filters.eq;

public class DDBB_InsertController {
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

        if (!(titleInput.getText().isEmpty()) && isImage()) {
            MongoCollection<Document> coll = LoginController.mongoConnector.getCollection("posts");
            ObjectId objectId = new ObjectId();
            Document doc = new Document("_id",objectId)
                    .append("creator_id", LoginController.username)
                    .append("created", LocalDateTime.now())
                    .append("img",imageURLInput.getText())
                    .append("post_info",
                            new Document("title", titleInput.getText()).append("post_body", postBodyInput.getText())
                    );
            coll.insertOne(doc);

            System.out.println("Guardado!");

            changeStage(objectId,imageURLInput.getText());
        }else {
            System.out.println("Not saved!");
        }

    }
    private DDBB_Controller controller;
    private void changeStage(ObjectId id, String imageUrl){
        controller.setNewPost(id,imageUrl);

        ((Stage) mainPane.getScene().getWindow()).close();
    }
    public void initController(DDBB_Controller controller) {
        this.controller = controller;
    }


    @FXML
    private void cleanImageURL() {
        imageURLInput.setText("");
        imageView.setImage(null);
    }
    @FXML
    private void cleanTittle() {
        titleInput.setText("");
    }
    @FXML
    private void imagePreview() {
        isImage();
    }
    private boolean isImage() {
        try {
            Image image = new Image(imageURLInput.getText());
            imageView.setImage(image);
            return true;
        } catch (IllegalArgumentException ignored) {}
        return false;
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
    protected void releasedHandler(){
        dragablePane.setCursor(Cursor.OPEN_HAND);
    }
    @FXML
    public void minimizeHandler() {
        ((Stage) mainPane.getScene().getWindow()).setIconified(true);
    }
    @FXML
    public void exitHandler() {
        ((Stage) mainPane.getScene().getWindow()).close();
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
}
