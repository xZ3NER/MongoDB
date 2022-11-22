package app;

import app.util.TimeLines;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.bson.Document;
import org.bson.types.ObjectId;


import java.io.IOException;
import java.net.URL;

import java.util.Objects;
import java.util.ResourceBundle;

import static app.util.TimeLines.opacityTimeLine;
import static com.mongodb.client.model.Filters.eq;


public class DDBB_Controller implements Initializable {

    //TODO Controlar que no meta mas post si no hay hueco.
    private static final ObservableList<ImageView> imageViewList = FXCollections.observableArrayList();

    public static int clickedIndex;
    @FXML
    private ImageView logoIcon;
    @FXML
    private GridPane gridPane;
    @FXML
    private Text usernameText;
    @FXML
    private ImageView minimizeIcon;
    @FXML
    private ImageView exitIcon;
    @FXML
    private Pane dragablePane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        usernameText.setText(LoginController.username);
    }

    public void initPosts() {

        MongoCollection<Document> coll = LoginController.mongoConnector.getCollection("posts");
        try (MongoCursor<Document> cursor = coll.find(eq("creator_id", LoginController.username)).iterator()) {
            Document doc;
            while ((doc = cursor.tryNext()) != null) {
                Image image = new Image(doc.getString("img"), 200, 200, true, true);
                ObjectId id = doc.getObjectId("_id");
                updateImageViewArrayList(image, id);
            }
        }

        initGridPane();
    }

    private void updateImageViewArrayList(Image image, ObjectId id) {

        ImageView imageView = new ImageView();

        imageView.setImage(image);
        imageView.setOpacity(0.5);

        imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            clickedIndex = imageViewList.indexOf(imageView);
            initPostInfoStage(id, image);
            event.consume();
        });

        imageView.addEventHandler(MouseEvent.MOUSE_ENTERED,event -> {
            TimeLines.opacityTimeLine(imageView,imageView.getOpacity(), 1, 0.8);
        });

        imageView.addEventHandler(MouseEvent.MOUSE_EXITED,event -> {
            TimeLines.opacityTimeLine(imageView,imageView.getOpacity(), 0.5, 0.8);
        });

        imageViewList.add(imageView);
    }

    private void initPostInfoStage(ObjectId id, Image image) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("ddbb_post_info.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            DDBB_Post_Info_Controller controller = fxmlLoader.getController();
            controller.setPostId(id, image);
            controller.initController(this);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Post");
            stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResource("images/Logo_Style2.png")).openStream()));
            stage.setResizable(false);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(scene);
            stage.showAndWait();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initGridPane() {
        int pos = 0;
        for (int y = 0; y < gridPane.getRowCount(); y++) {
            for (int x = 0; x < gridPane.getColumnCount(); x++) {
                if (pos < imageViewList.size()) {
                    gridPane.add(imageViewList.get(pos++), x, y);
                }
            }
        }
    }

    private void updateGridPane() {
        gridPane.getChildren().clear();

        initGridPane();
    }

    @FXML
    protected void createButtonHandler(MouseEvent mouseEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("ddbb-insert.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            DDBB_InsertController controller = fxmlLoader.getController();
            controller.initController(this);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Create new post");
            stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResource("images/Logo_Style2.png")).openStream()));
            stage.setResizable(false);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(scene);
            stage.showAndWait();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void setNewPost(ObjectId id, String imageUrl) {
        Image image = new Image(imageUrl, 200, 200, true, true);
        ImageView imageView = new ImageView();

        imageView.setImage(image);
        imageView.setOpacity(0.5);

        imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            clickedIndex = imageViewList.indexOf(imageView);
            initPostInfoStage(id,image);
            event.consume();
        });

        imageView.addEventHandler(MouseEvent.MOUSE_ENTERED,event -> {
            TimeLines.opacityTimeLine(imageView,imageView.getOpacity(), 1, 0.8);
        });

        imageView.addEventHandler(MouseEvent.MOUSE_EXITED,event -> {
            TimeLines.opacityTimeLine(imageView,imageView.getOpacity(), 0.5, 0.8);
        });

        imageViewList.add(imageView);

        updateGridPane();
    }
    public void deletePost(ObjectId objectId) {

        imageViewList.remove(clickedIndex);

        MongoCollection<Document> coll = LoginController.mongoConnector.getCollection("posts");
        coll.deleteOne(eq("_id", objectId));

        updateGridPane();
    }


    private double x, y;
    @FXML
    protected void dragHandler(MouseEvent mouseEvent) {
        Stage stage = (Stage) gridPane.getScene().getWindow();
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
        ((Stage) gridPane.getScene().getWindow()).setIconified(true);
    }
    @FXML
    public void exitHandler() {
        ((Stage) gridPane.getScene().getWindow()).close();
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
