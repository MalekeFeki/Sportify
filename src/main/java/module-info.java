module Sportify {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires stripe.java;
    requires org.apache.pdfbox;
    requires java.desktop;

    opens  controllers;
}