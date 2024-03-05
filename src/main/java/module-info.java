module Sportify {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.mail;
    requires com.google.zxing;
    requires com.google.zxing.javase;
    opens  controllers;
    opens entities to javafx.base;
}
