package entities;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class PDFGenerator {

    public static void generatePDF(String paymentInfo, String filePath) {
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

                // Write the payment info
                contentStream.beginText();
                contentStream.newLineAtOffset(100, 670);

                String[] lines = paymentInfo.split("\n");
                for (String line : lines) {
                    contentStream.showText(line);
                    contentStream.newLine();
                }

                contentStream.endText();
            }

            document.save(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}