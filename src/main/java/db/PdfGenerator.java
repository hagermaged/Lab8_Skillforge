package db;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import model.Certificate;

public class PdfGenerator {

    public boolean generateProfessionalPDFCertificate(Certificate certificate, String filePath) {
        try {
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Set page size to A4 for better printing
            pdfDoc.setDefaultPageSize(com.itextpdf.kernel.geom.PageSize.A4.rotate());

            // Add certificate border (optional)
            document.setMargins(50, 50, 50, 50);

            // Title
            Paragraph title = new Paragraph("CERTIFICATE OF COMPLETION")
                    .setFontSize(36)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(ColorConstants.DARK_GRAY);
            document.add(title);

            // Subtitle
            Paragraph subtitle = new Paragraph("This certifies that")
                    .setFontSize(18)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(20);
            document.add(subtitle);

            // Student Name
            Paragraph studentName = new Paragraph(certificate.getStudentName())
                    .setFontSize(28)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(10)
                    .setFontColor(ColorConstants.BLUE);
            document.add(studentName);

            // Completion text
            Paragraph completionText = new Paragraph("has successfully completed the course")
                    .setFontSize(18)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(20);
            document.add(completionText);

            // Course Name
            Paragraph courseName = new Paragraph(certificate.getCourseName())
                    .setFontSize(24)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(10)
                    .setFontColor(ColorConstants.GREEN); // Changed from DARK_GREEN to GREEN
            document.add(courseName);

            // Issue Date
            java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("MMMM dd, yyyy");
            String formattedDate = dateFormat.format(certificate.getIssueDate());

            Paragraph date = new Paragraph("Issued on: " + formattedDate)
                    .setFontSize(14)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(30);
            document.add(date);

            // Certificate ID
            Paragraph certId = new Paragraph("Certificate ID: " + certificate.getCertificateId())
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(10);
            document.add(certId);

            document.close();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}