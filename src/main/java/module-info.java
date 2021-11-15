module com.semesterprojekt01.semesterprojekt01 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires eu.hansolo.tilesfx;

    opens com.semesterprojekt01.semesterprojekt01 to javafx.fxml;
    exports com.semesterprojekt01.semesterprojekt01;
}