package app;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.List;

import static app.util.TimeLines.opacityTimeLine;
import static com.mongodb.client.model.Filters.eq;

public class DDBB_Post_Info_Controller {

    private ObjectId postId;
    private String title;
    private String postBody;
    private Image image;
    @FXML
    private Button editButton;
    @FXML
    private Button confirmButton;
    @FXML
    private TextArea titleLabel;
    @FXML
    private ImageView postImgView;
    @FXML
    private TextArea post_body_textA;
    @FXML
    private AnchorPane mainPane;
    @FXML
    private ImageView exitIcon;


    public void setPostId(ObjectId postId,Image image) {
        this.postId = postId;
        this.image = image;

        initPostInfo();

        this.titleLabel.setText(this.title);
        this.post_body_textA.setText(this.postBody);
        this.postImgView.setImage(image);
    }

    private void initPostInfo() {
        MongoCollection<Document> coll = LoginController.mongoConnector.getCollection("posts");
        Document doc = coll.find(eq("_id", this.postId)).first();
        assert doc != null;
        doc = doc.getEmbedded(List.of("post_info"), Document.class);
        this.title = doc.getString("title");
        this.postBody = doc.getString("post_body");
    }

    private DDBB_Controller controller;

    @FXML
    protected void deleteHandler(MouseEvent mouseEvent) {
        controller.deletePost(postId);

        ((Stage) mainPane.getScene().getWindow()).close();
    }

    public void initController(DDBB_Controller controller) {
        this.controller = controller;
    }

    @FXML
    protected void editButtonHandler(MouseEvent mouseEvent) {
        this.editButton.setVisible(false);
        this.confirmButton.setVisible(true);

        this.titleLabel.setEditable(true);
        this.post_body_textA.setEditable(true);
    }

    @FXML
    protected void confirmButtonHandler(MouseEvent mouseEvent) {
        this.confirmButton.setVisible(false);
        this.editButton.setVisible(true);

        this.titleLabel.setEditable(false);
        this.post_body_textA.setEditable(false);

        this.title = this.titleLabel.getText();
        this.postBody = this.post_body_textA.getText();

        MongoCollection<Document> coll = LoginController.mongoConnector.getCollection("posts");
        coll.updateMany(eq("_id", postId),
                new Document("$set",
                        new Document("post_info",
                                new Document("title",title).append("post_body",postBody))));

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
    private void onMouseEnteredExitIcon() {
        opacityTimeLine(exitIcon, exitIcon.getOpacity(), 0.8, 1);
    }

    @FXML
    private void onMouseExitedExitIcon() {
        opacityTimeLine(exitIcon, exitIcon.getOpacity(), 1, 1);
    }

    @FXML
    protected void exitHandler(MouseEvent mouseEvent) {
        ((Stage) mainPane.getScene().getWindow()).close();
    }
}
