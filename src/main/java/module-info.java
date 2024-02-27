module Sportify {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    opens entities;
    opens  controllers;
}