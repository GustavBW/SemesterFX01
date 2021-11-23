module worldofzuul {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires eu.hansolo.tilesfx;

    opens worldofzuul to javafx.fxml;
    exports worldofzuul;
    exports Realtime;
    opens Realtime to javafx.fxml;
    exports BackEnd;
    opens BackEnd to javafx.fxml;
    exports Realtime.interfaces;
    opens Realtime.interfaces to javafx.fxml;
}