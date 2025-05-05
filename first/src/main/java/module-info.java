module com.chat {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    
    opens com.chat to javafx.fxml;
    exports com.chat;
}
