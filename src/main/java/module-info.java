module Sportify {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.mail;
    requires twilio;
    opens  controllers;
    opens entities to javafx.base;
}