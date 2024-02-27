module Sportify {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    opens  controllers;
    opens entities to javafx.base;
}
