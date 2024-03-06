module Sportify {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.mail;
    requires twilio;
    requires com.google.zxing;
    requires com.google.zxing.javase;
    requires javafx.web;
    requires jdk.jsobject;
    requires stripe.java;
    requires org.apache.pdfbox;
    requires java.desktop;
    requires org.controlsfx.controls;
    opens  controllers;
    opens entities to javafx.base, javafx.fxml;
}