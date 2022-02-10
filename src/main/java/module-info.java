module com.iesfranciscodelosrios.UD3Socket {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    opens com.iesfranciscodelosrios.UD3Socket.controller to javafx.fxml;
    exports com.iesfranciscodelosrios.UD3Socket;
}
