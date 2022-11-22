package app;

import app.util.MongoConnector;
import app.util.TimeLines;
import app.util.Toast;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.bson.Document;

import java.io.IOException;
import java.util.Objects;

import static app.util.TimeLines.opacityTimeLine;
import static com.mongodb.client.model.Filters.eq;

public class LoginController {
    public static final MongoConnector mongoConnector =
            new MongoConnector("localhost", 27017, "social_network");
    public static String username;

    @FXML
    private AnchorPane mainPane;
    @FXML
    private TextField usernameTextF;
    @FXML
    private TextField passwordTextF;
    @FXML
    private ImageView logoIcon;
    @FXML
    private ImageView exitIcon;
    @FXML
    private ImageView minimizeIcon;
    @FXML
    private Button loginButton;
    @FXML
    private Button registerButton;
    @FXML
    private Label registerText;
    @FXML
    private Text registerQuestionText;
    @FXML
    private Label backLabel;

    @FXML
    protected void enterKeyHandler() throws IOException {
        MongoCollection<Document> coll = mongoConnector.getCollection("users");
        Document doc = coll.find(eq("_id", usernameTextF.getText())).first();
        if (doc != null) {
            if (doc.getString("password").equals(passwordTextF.getText())) {
                username = usernameTextF.getText();
                initStage();
            } else {
                Toast.makeText((Stage) mainPane.getScene().getWindow(),"Wrong password", 1000, 300, 300);
            }
        } else {
            Toast.makeText((Stage) mainPane.getScene().getWindow(),"User doesn't exits", 1000, 300, 300);
        }
    }

    private void initStage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("ddbb.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.setFill(Color.TRANSPARENT);

        DDBB_Controller controller = fxmlLoader.getController();

        Stage stage = new Stage();
        //stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(username+" posts");
        //Icono en la barra de windows
        stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResource("images/Logo_Style2.png")).openStream()));
        stage.setResizable(false);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);

        stage.show();

        controller.initPosts();

        TimeLines.stageClose((Stage) mainPane.getScene().getWindow(), 1, 0, 0.3);
        TimeLines.stageDisplay(stage, 0, 1, 0.4);
    }

    @FXML
    public void registerButtonHandler() {
        try {
            if (!(usernameTextF.getText().isEmpty() || passwordTextF.getText().isEmpty())) {

                MongoCollection<Document> coll = mongoConnector.getCollection("users");

                Document doc = new Document("_id", usernameTextF.getText())
                        .append("password", passwordTextF.getText());

                coll.insertOne(doc);

                Toast.makeText((Stage) mainPane.getScene().getWindow(),"User created", 1000, 300, 300);

                passwordTextF.setText("");

            }else {
                Toast.makeText((Stage) mainPane.getScene().getWindow(),"Please fill all fields", 1000, 300, 300);
            }
        } catch (MongoWriteException e) {
            Toast.makeText((Stage) mainPane.getScene().getWindow(),"User already exists", 1000, 300, 300);
        }
    }
    private boolean registerMode = false;
    @FXML
    public void changeRegisterMode(){
        //registerMode = true: registerButton & backText visible
        //registerMode = false: loginButton & registerTexts visible
        registerButton.setVisible(!registerMode);
        backLabel.setVisible(!registerMode);
        loginButton.setVisible(registerMode);
        registerQuestionText.setVisible(registerMode);
        registerText.setVisible(registerMode);

        registerMode = !registerMode;
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
        TimeLines.stageClose((Stage) mainPane.getScene().getWindow(), 1, 0, 0.3);
    }


    //Components opacity actions
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
    private void onMouseEnteredRegisterText() {
        opacityTimeLine(registerText, registerText.getOpacity(), 0.75, 0.5);
    }
    @FXML
    private void onMouseExitedRegisterText() {
        opacityTimeLine(registerText, registerText.getOpacity(), 1, 0.5);
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
