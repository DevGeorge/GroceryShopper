module com.fastfoods {
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.controls;
    requires java.sql;
    requires mysql.connector.java;

    opens com.fastfoods to javafx.fxml;
    exports com.fastfoods;
}