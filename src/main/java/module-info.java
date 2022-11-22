module MongoDB.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires mongodb.jdbc;

    opens app to javafx.fxml;
    exports app;

    opens app.util to javafx.fxml;
    exports app.util;
}