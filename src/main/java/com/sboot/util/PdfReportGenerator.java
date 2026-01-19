package com.sboot.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.sboot.dto.SupplierPerformanceReportDTO;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.Map;

@Component
public class PdfReportGenerator {

    public byte[] generateSupplierPerformancePdf(SupplierPerformanceReportDTO dto) throws Exception {
        Document document = new Document(PageSize.A4, 40, 40, 50, 40);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            // --- Fonts ---
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
            Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK);
            Font normal = FontFactory.getFont(FontFactory.HELVETICA, 11);

            // --- Title Section ---
            Paragraph title = new Paragraph("Supplier Performance Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            LineSeparator line = new LineSeparator();
            line.setLineColor(BaseColor.GRAY);
            document.add(new Chunk(line));
            document.add(Chunk.NEWLINE);

            // --- Supplier Info Section Header ---
            document.add(new Paragraph("Supplier Information", sectionFont));
            document.add(Chunk.NEWLINE);

            // --- Supplier Basic Info Table ---
            PdfPTable info = new PdfPTable(2);
            info.setWidthPercentage(100);
            info.setWidths(new int[]{1, 3});
            info.setSpacingBefore(5);
            info.setSpacingAfter(10);

            info.addCell(headerCell("Supplier ID", headerFont));
            info.addCell(valueCell(String.valueOf(dto.getSupplierId()), normal));

            info.addCell(headerCell("Supplier Name", headerFont));
            info.addCell(valueCell(dto.getSupplierName(), normal));

            info.addCell(headerCell("Email", headerFont));
            info.addCell(valueCell(dto.getSupplierEmail(), normal));

            info.addCell(headerCell("Phone", headerFont));
            info.addCell(valueCell(dto.getSupplierPhone(), normal));

            info.addCell(headerCell("Rating Field", headerFont));
            info.addCell(valueCell(
                    dto.getSupplierRatingField() == null ? "-" : String.valueOf(dto.getSupplierRatingField()),
                    normal
            ));

            document.add(info);

            // --- Summary Section ---
            document.add(Chunk.NEWLINE);
            document.add(new Paragraph("Summary", sectionFont));
            document.add(Chunk.NEWLINE);

            PdfPTable summary = new PdfPTable(2);
            summary.setWidthPercentage(70);
            summary.setSpacingBefore(5);

            summary.addCell(headerCell("Average Rating", headerFont));
            summary.addCell(valueCell(String.format("%.2f", dto.getAverageRating()), normal));

            summary.addCell(headerCell("Total Reviews", headerFont));
            summary.addCell(valueCell(String.valueOf(dto.getTotalReviews()), normal));

            if (dto.getLatestReview() != null) {
                summary.addCell(headerCell("Latest Rating", headerFont));
                summary.addCell(valueCell(String.valueOf(dto.getLatestReview().getRating()), normal));

                summary.addCell(headerCell("Latest Review Date", headerFont));
                summary.addCell(valueCell(
                        dto.getLatestReview().getDate() == null ? "-" : dto.getLatestReview().getDate().toString(),
                        normal
                ));
            }

            document.add(summary);

            // --- Rating Breakdown Section ---
            document.add(Chunk.NEWLINE);
            document.add(new Paragraph("Rating Breakdown", sectionFont));
            document.add(Chunk.NEWLINE);

            PdfPTable breakdown = new PdfPTable(new float[]{1, 1, 1});
            breakdown.setWidthPercentage(70);
            breakdown.setSpacingBefore(5);

            breakdown.addCell(headerCell("Rating", headerFont));
            breakdown.addCell(headerCell("Count", headerFont));
            breakdown.addCell(headerCell("Percentage", headerFont));

            for (Map.Entry<Integer, Long> e : dto.getRatingCounts().entrySet()) {
                breakdown.addCell(valueCell(String.valueOf(e.getKey()), normal));
                breakdown.addCell(valueCell(String.valueOf(e.getValue()), normal));
                breakdown.addCell(valueCell(
                        String.format("%.2f%%", dto.getRatingPercentages().getOrDefault(e.getKey(), 0.0)),
                        normal
                ));
            }

            document.add(breakdown);

            // --- Latest Review Text ---
            if (dto.getLatestReview() != null && dto.getLatestReview().getReview() != null) {
                document.add(Chunk.NEWLINE);
                document.add(new Paragraph("Latest Review", sectionFont));
                document.add(Chunk.NEWLINE);

                PdfPCell reviewCell = new PdfPCell(new Phrase(dto.getLatestReview().getReview(), normal));
                reviewCell.setPadding(10);
                reviewCell.setBackgroundColor(new BaseColor(245, 245, 245)); // Light gray
                reviewCell.setBorderColor(BaseColor.LIGHT_GRAY);

                PdfPTable reviewBox = new PdfPTable(1);
                reviewBox.setWidthPercentage(100);
                reviewBox.addCell(reviewCell);

                document.add(reviewBox);
            }

            document.close();
            return baos.toByteArray();

        } finally {
            baos.close();
        }
    }

    // --- Stylish Header Cell ---
    private PdfPCell headerCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(new BaseColor(70, 130, 180)); // Steel Blue
        cell.setPadding(8);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorderColor(BaseColor.GRAY);
        return cell;
    }

    // --- Generic Value Cell ---
    private PdfPCell valueCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(7);
        cell.setBorderColor(BaseColor.LIGHT_GRAY);
        return cell;
    }
}
