package com.sboot.service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.sboot.dto.SupplierPerformanceReportDTO;
import com.sboot.entity.Supplier;
import com.sboot.entity.SupplierRating;
import com.sboot.repository.SuppliersRepository;
import com.sboot.repository.SupplierRatingRepository;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SupplierPerformanceService {

    private final SuppliersRepository supplierRepo;
    private final SupplierRatingRepository ratingRepo;

    public SupplierPerformanceService(SuppliersRepository supplierRepo,
                                      SupplierRatingRepository ratingRepo) {
        this.supplierRepo = supplierRepo;
        this.ratingRepo = ratingRepo;
    }

    // =====================================================================================
    // REQUIRED: This method was missing but your controller needs it
    // =====================================================================================
    public SupplierPerformanceReportDTO getReportForSupplier(Long supplierId) {
        return buildRatingReport(supplierId);
    }

    // =====================================================================================
    // Build Supplier Rating Report DTO
    // =====================================================================================
    public SupplierPerformanceReportDTO buildRatingReport(Long supplierId) {

        Supplier supplier = supplierRepo.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        List<SupplierRating> ratings = ratingRepo.findBySupplier_SuppliersId(supplierId);

        SupplierPerformanceReportDTO dto = new SupplierPerformanceReportDTO();
        dto.setSupplierId(supplier.getSuppliersId());
        dto.setSupplierName(supplier.getSuppliersName());
        dto.setSupplierEmail(supplier.getSuppliersEmail());
        dto.setSupplierPhone(supplier.getSuppliersPhone());
        dto.setSupplierRatingField(supplier.getSuppliersRating());

        long totalReviews = ratings.size();
        dto.setTotalReviews(totalReviews);

        if (totalReviews > 0) {

            double avg = ratings.stream()
                    .mapToInt(SupplierRating::getRating)
                    .average()
                    .orElse(0.0);
            dto.setAverageRating(avg);

            SupplierRating latest = ratings.stream()
                    .max(Comparator.comparing(SupplierRating::getCreatedAt))
                    .orElse(null);

            if (latest != null) {
                dto.setLatestReview(new SupplierPerformanceReportDTO.RatingSummary(
                        latest.getRating(),
                        latest.getReview(),
                        latest.getCreatedAt()
                ));
            }

            Map<Integer, Long> counts = ratings.stream()
                    .collect(Collectors.groupingBy(SupplierRating::getRating, Collectors.counting()));

            for (int i = 1; i <= 5; i++) counts.putIfAbsent(i, 0L);

            dto.setRatingCounts(counts);

            Map<Integer, Double> percentages = new LinkedHashMap<>();
            for (int i = 1; i <= 5; i++) {
                percentages.put(i, (counts.get(i) * 100.0) / totalReviews);
            }
            dto.setRatingPercentages(percentages);

        } else {

            dto.setAverageRating(0.0);
            dto.setRatingCounts(Map.of(1,0L,2,0L,3,0L,4,0L,5,0L));
            dto.setRatingPercentages(Map.of(1,0.0,2,0.0,3,0.0,4,0.0,5,0.0));
            dto.setLatestReview(null);
        }

        return dto;
    }

    // =====================================================================================
    // ALL SUPPLIERS → JSON
    // =====================================================================================
    public List<SupplierPerformanceReportDTO> getAllSupplierPerformances() {
        return supplierRepo.findAll().stream()
                .map(s -> buildRatingReport(s.getSuppliersId()))
                .collect(Collectors.toList());
    }

    // =====================================================================================
    // PDF: ALL SUPPLIERS SUMMARY
    // =====================================================================================
    public byte[] generateAllSuppliersPDF() {

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Document document = new Document(PageSize.A4, 40, 40, 50, 40);
            PdfWriter writer = PdfWriter.getInstance(document, out);

            // ---- Page Header/Footer ----
            writer.setPageEvent(new PdfPageEventHelper() {

                Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.GRAY);

                @Override
                public void onEndPage(PdfWriter writer, Document document) {
                    PdfContentByte cb = writer.getDirectContent();
                    Phrase header = new Phrase("Supplier Performance Summary Report", headerFont);

                    ColumnText.showTextAligned(
                            cb,
                            Element.ALIGN_RIGHT,
                            header,
                            document.right(),
                            document.top() + 20,
                            0
                    );
                }
            });

            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
            Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, new BaseColor(0, 70, 140));
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 11);
            Font headerCellFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);

            for (Supplier supplier : supplierRepo.findAll()) {

                // ❌ Skip BGSW completely
                if (supplier.getSuppliersName() != null &&
                    supplier.getSuppliersName().equalsIgnoreCase("BGSW")) {
                    continue;
                }

                SupplierPerformanceReportDTO dto = buildRatingReport(supplier.getSuppliersId());

                // Title for each supplier page
                Paragraph title = new Paragraph(dto.getSupplierName(), titleFont);
                title.setSpacingAfter(10);
                document.add(title);

                // Supplier Information Table
                PdfPTable info = new PdfPTable(2);
                info.setWidthPercentage(100);
                info.setWidths(new float[]{1.8f, 3.5f});
                info.setSpacingAfter(15);

                info.addCell(headerCell("Field", headerCellFont));
                info.addCell(headerCell("Value", headerCellFont));

                info.addCell(valueCell("Supplier ID"));
                info.addCell(valueCell(String.valueOf(dto.getSupplierId())));

                info.addCell(valueCell("Email"));
                info.addCell(valueCell(dto.getSupplierEmail()));

                info.addCell(valueCell("Phone"));
                info.addCell(valueCell(dto.getSupplierPhone()));

                info.addCell(valueCell("Average Rating"));
                info.addCell(valueCell(String.format("%.2f", dto.getAverageRating())));

                info.addCell(valueCell("Total Reviews"));
                info.addCell(valueCell(String.valueOf(dto.getTotalReviews())));

                document.add(info);

                // Rating Breakdown
                Paragraph breakdownTitle = new Paragraph("Rating Breakdown", sectionFont);
                breakdownTitle.setSpacingAfter(10);
                document.add(breakdownTitle);

                PdfPTable breakdown = new PdfPTable(3);
                breakdown.setWidthPercentage(60);
                breakdown.setWidths(new float[]{1.2f, 1, 1.2f});
                breakdown.setSpacingAfter(20);

                breakdown.addCell(headerCell("Rating", headerCellFont));
                breakdown.addCell(headerCell("Count", headerCellFont));
                breakdown.addCell(headerCell("Percentage", headerCellFont));

                for (int rating = 1; rating <= 5; rating++) {
                    breakdown.addCell(valueCell(rating + " Stars"));
                    breakdown.addCell(valueCell(String.valueOf(dto.getRatingCounts().get(rating))));
                    breakdown.addCell(valueCell(String.format("%.2f%%", dto.getRatingPercentages().get(rating))));
                }

                document.add(breakdown);

                // Latest Review
                if (dto.getLatestReview() != null) {

                    Paragraph rTitle = new Paragraph("Latest Review", sectionFont);
                    rTitle.setSpacingAfter(8);
                    document.add(rTitle);

                    PdfPCell reviewCell = new PdfPCell(
                            new Phrase(dto.getLatestReview().getReview(), normalFont)
                    );
                    reviewCell.setPadding(10);
                    reviewCell.setBackgroundColor(new BaseColor(245, 245, 245));
                    reviewCell.setBorderColor(BaseColor.LIGHT_GRAY);

                    PdfPTable reviewBox = new PdfPTable(1);
                    reviewBox.setWidthPercentage(100);
                    reviewBox.addCell(reviewCell);

                    document.add(reviewBox);
                }

                // New page for next supplier
                document.newPage();
            }

            document.close();
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Professional PDF generation failed", e);
        }
    }

    // =====================================================================================
    // EXCEL: ALL SUPPLIERS SUMMARY
    // =====================================================================================
    public byte[] generateAllSuppliersExcel() {

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             XSSFWorkbook wb = new XSSFWorkbook()) {

            var sheet = wb.createSheet("Supplier Performance");
            int row = 0;

            // -------------------------------
            // STYLES
            // -------------------------------

            var headerStyle = wb.createCellStyle();
            var headerFont = wb.createFont();
            headerFont.setBold(true);
            headerFont.setColor((short) 9);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor((short) 12);
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            var bodyStyle = wb.createCellStyle();
            bodyStyle.setBorderBottom(BorderStyle.THIN);
            bodyStyle.setBorderTop(BorderStyle.THIN);
            bodyStyle.setBorderLeft(BorderStyle.THIN);
            bodyStyle.setBorderRight(BorderStyle.THIN);

            var wrapStyle = wb.createCellStyle();
            wrapStyle.cloneStyleFrom(bodyStyle);
            wrapStyle.setWrapText(true);

            var numberStyle = wb.createCellStyle();
            numberStyle.cloneStyleFrom(bodyStyle);
            numberStyle.setDataFormat(wb.createDataFormat().getFormat("0.00"));

            // -------------------------------
            // COLUMNS
            // -------------------------------

            String[] columns = {
                    "Supplier ID",
                    "Supplier Name",
                    "Email",
                    "Phone",
                    "Rating Field",
                    "Avg Rating",
                    "Total Reviews",
                    "1★ Count", "2★ Count", "3★ Count", "4★ Count", "5★ Count",
                    "1★ %", "2★ %", "3★ %", "4★ %", "5★ %",
                    "Latest Rating",
                    "Latest Review Date",
                    "Latest Review Text"
            };

            // HEADER ROW
            var headerRow = sheet.createRow(row++);
            for (int i = 0; i < columns.length; i++) {
                var c = headerRow.createCell(i);
                c.setCellValue(columns[i]);
                c.setCellStyle(headerStyle);
            }

            // -------------------------------
            // DATA ROWS FOR EACH SUPPLIER
            // -------------------------------
            for (Supplier supplier : supplierRepo.findAll()) {

                // ❌ Skip BGSW
                if (supplier.getSuppliersName() != null &&
                    supplier.getSuppliersName().equalsIgnoreCase("BGSW")) {
                    continue;
                }

                SupplierPerformanceReportDTO dto =
                        buildRatingReport(supplier.getSuppliersId());

                var r = sheet.createRow(row++);
                int col = 0;

                // Supplier ID
                var c1 = r.createCell(col++);
                c1.setCellValue(dto.getSupplierId());
                c1.setCellStyle(bodyStyle);

                // Supplier Name
                var c2 = r.createCell(col++);
                c2.setCellValue(String.valueOf(dto.getSupplierName()));
                c2.setCellStyle(bodyStyle);

                // Email
                var c3 = r.createCell(col++);
                c3.setCellValue(String.valueOf(dto.getSupplierEmail()));
                c3.setCellStyle(bodyStyle);

                // Phone
                var c4 = r.createCell(col++);
                c4.setCellValue(String.valueOf(dto.getSupplierPhone()));
                c4.setCellStyle(bodyStyle);

                // Rating Field
                var c5 = r.createCell(col++);
                c5.setCellValue(
                        dto.getSupplierRatingField() == null ?
                                "-" :
                                String.valueOf(dto.getSupplierRatingField())
                );
                c5.setCellStyle(bodyStyle);

                // Average Rating
                var avgCell = r.createCell(col++);
                avgCell.setCellValue(dto.getAverageRating());
                avgCell.setCellStyle(numberStyle);

                // Total Reviews
                var trCell = r.createCell(col++);
                trCell.setCellValue(dto.getTotalReviews());
                trCell.setCellStyle(bodyStyle);

                // Rating counts 1–5
                for (int i = 1; i <= 5; i++) {
                    var c = r.createCell(col++);
                    c.setCellValue(dto.getRatingCounts().get(i));
                    c.setCellStyle(bodyStyle);
                }

                // Rating percentage 1–5
                for (int i = 1; i <= 5; i++) {
                    var c = r.createCell(col++);
                    c.setCellValue(dto.getRatingPercentages().get(i));
                    c.setCellStyle(numberStyle);
                }

                // Latest review details
                if (dto.getLatestReview() != null) {

                    var rateCell = r.createCell(col++);
                    rateCell.setCellValue(dto.getLatestReview().getRating());
                    rateCell.setCellStyle(bodyStyle);

                    var dateCell = r.createCell(col++);
                    dateCell.setCellValue(
                            dto.getLatestReview().getDate() == null ?
                                    "-" :
                                    String.valueOf(dto.getLatestReview().getDate())
                    );
                    dateCell.setCellStyle(bodyStyle);

                    var textCell = r.createCell(col++);
                    textCell.setCellValue(
                            dto.getLatestReview().getReview() == null ?
                                    "" :
                                    dto.getLatestReview().getReview()
                    );
                    textCell.setCellStyle(wrapStyle);

                } else {
                    for (int i = 0; i < 3; i++) {
                        var c = r.createCell(col++);
                        c.setCellValue("-");
                        c.setCellStyle(bodyStyle);
                    }
                }
            }

            // Auto-size columns
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            wb.write(out);
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Enhanced Excel generation failed", e);
        }
    }

    
    private PdfPCell headerCell(String text, Font f) {
        PdfPCell c = new PdfPCell(new Phrase(text, f));
        c.setBackgroundColor(new BaseColor(0, 70, 140));
        c.setPadding(8);
        c.setHorizontalAlignment(Element.ALIGN_CENTER);
        return c;
    }

    private PdfPCell valueCell(String text) {
        PdfPCell c = new PdfPCell(new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA, 11)));
        c.setPadding(7);
        c.setBackgroundColor(BaseColor.WHITE);
        return c;
    }

}
