module Sportify {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.web;
    requires jdk.jsobject;

    opens  controllers;
    opens entities to javafx.base, javafx.fxml;

}