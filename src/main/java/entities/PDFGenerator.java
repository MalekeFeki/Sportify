package entities;

import javafx.scene.layout.GridPane;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class PDFGenerator {

    public static void generatePDF(GridPane gridPane, String filePath) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Set font and color for the header
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
                contentStream.setNonStrokingColor(Color.BLUE);

                // Write the header
                contentStream.beginText();
                contentStream.newLineAtOffset(100, 700);
                contentStream.showText("Payment Information:");
                contentStream.endText();

                // Draw a rectangle around the header
                contentStream.addRect(90, 695, 300, 20);
                contentStream.stroke();

                // Set font and color for the payment info
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.setNonStrokingColor(Color.BLACK);

                // Write the payment info from GridPane
                int row = 0;
                for (javafx.scene.Node node : gridPane.getChildren()) {
                    if (node instanceof javafx.scene.text.Text) {
                        javafx.scene.text.Text text = (javafx.scene.text.Text) node;
                        String line = text.getText();
                        contentStream.beginText();
                        contentStream.newLineAtOffset(100, 670 - row * 20);
                        contentStream.showText(line);
                        contentStream.endText();
                        row++;
                    }
                }
            }

            document.save(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
