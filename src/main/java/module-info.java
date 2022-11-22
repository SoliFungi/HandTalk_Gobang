module com.solifungi.handtalkgobang {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.solifungi.handtalkgobang to javafx.fxml;
    exports com.solifungi.handtalkgobang;
    exports com.solifungi.handtalkgobang.controllers;
    opens com.solifungi.handtalkgobang.controllers to javafx.fxml;
    exports com.solifungi.handtalkgobang.game;
    opens com.solifungi.handtalkgobang.game to javafx.fxml;
    exports com.solifungi.handtalkgobang.util;
    opens com.solifungi.handtalkgobang.util to javafx.fxml;
    exports com.solifungi.handtalkgobang.util.handlers;
    opens com.solifungi.handtalkgobang.util.handlers to javafx.fxml;
}