package com.sboot.service;

import com.sboot.dto.ProductionReportRequest;
import com.sboot.dto.ProductionReportResponse;
import com.sboot.entity.ProductionSchedule;
import com.sboot.repository.ProductionScheduleRepository;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductionReportService {

    private final ProductionScheduleRepository repo;

    public ProductionReportService(ProductionScheduleRepository repo) {
        this.repo = repo;
    }

    // Generate report data
    public List<ProductionReportResponse> generateReport(ProductionReportRequest request) {
        List<ProductionSchedule> schedules = repo.findForReport(
                request.getStartDate(),
                request.getEndDate(),
                request.getPsStatus(),
                request.getProductId()
        );

        return schedules.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    // Map entity -> DTO
    private ProductionReportResponse mapToResponse(ProductionSchedule ps) {
        ProductionReportResponse dto = new ProductionReportResponse();
        dto.setPsId(ps.getPsId());
        dto.setProductId(ps.getProduct() != null ? ps.getProduct().getProductsId() : null);
        dto.setProductName(ps.getProduct() != null ? ps.getProduct().getProductsName() : null);
        dto.setPsStartDate(ps.getPsStartDate());
        dto.setPsDeadline(ps.getPsDeadline());
        dto.setPsEndDate(ps.getPsEndDate());
        dto.setPsQuantity(ps.getPsQuantity());
        dto.setPsStatus(ps.getStatus());
        return dto;
    }

    // Excel Export (Styled)
    public byte[] exportToExcel(List<ProductionReportResponse> reports) throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Production Report");

        // Header style
        CellStyle headerStyle = workbook.createCellStyle();
        org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont(); // fully qualified
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);

        // Odd row style
        CellStyle oddRowStyle = workbook.createCellStyle();
        oddRowStyle.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        oddRowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Header row
        String[] columns = {"Schedule ID","Product ID","Product Name","Start Date","Deadline","End Date","Quantity","Status"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);
        }

        // Data rows
        int rowIdx = 1;
        for (ProductionReportResponse r : reports) {
            Row row = sheet.createRow(rowIdx);
            row.createCell(0).setCellValue(r.getPsId() != null ? r.getPsId() : 0);
            row.createCell(1).setCellValue(r.getProductId() != null ? r.getProductId() : 0);
            row.createCell(2).setCellValue(r.getProductName() != null ? r.getProductName() : "");
            row.createCell(3).setCellValue(r.getPsStartDate() != null ? r.getPsStartDate().toString() : "");
            row.createCell(4).setCellValue(r.getPsDeadline() != null ? r.getPsDeadline().toString() : "");
            row.createCell(5).setCellValue(r.getPsEndDate() != null ? r.getPsEndDate().toString() : "");
            row.createCell(6).setCellValue(r.getPsQuantity() != null ? r.getPsQuantity() : 0);
            row.createCell(7).setCellValue(r.getPsStatus() != null ? r.getPsStatus() : "");

            // Alternate row color
            if (rowIdx % 2 == 0) {
                for (int i = 0; i < columns.length; i++) {
                    row.getCell(i).setCellStyle(oddRowStyle);
                }
            }
            rowIdx++;
        }

        // Auto-size columns
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();
        return out.toByteArray();
    }

    // PDF Export (Styled)
    public byte[] exportToPdf(List<ProductionReportResponse> reports) throws Exception {
        Document document = new Document(PageSize.A4, 20, 20, 20, 20);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);
        document.open();

        // Title
        com.itextpdf.text.Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.DARK_GRAY);
        Paragraph title = new Paragraph("Production Report", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20f);
        document.add(title);

        // Table
        String[] headers = {"Schedule ID","Product ID","Product Name","Start Date","Deadline","End Date","Quantity","Status"};
        PdfPTable table = new PdfPTable(headers.length);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        // Header row
        com.itextpdf.text.Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
            cell.setBackgroundColor(new BaseColor(0, 102, 204));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(6f);
            table.addCell(cell);
        }

        // Data rows
        com.itextpdf.text.Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);
        boolean alternate = false;
        for (ProductionReportResponse r : reports) {
            BaseColor rowColor = alternate ? new BaseColor(230, 240, 255) : BaseColor.WHITE;

            addPdfCell(table, r.getPsId() != null ? String.valueOf(r.getPsId()) : "", dataFont, rowColor);
            addPdfCell(table, r.getProductId() != null ? String.valueOf(r.getProductId()) : "", dataFont, rowColor);
            addPdfCell(table, r.getProductName() != null ? r.getProductName() : "", dataFont, rowColor);
            addPdfCell(table, r.getPsStartDate() != null ? r.getPsStartDate().toString() : "", dataFont, rowColor);
            addPdfCell(table, r.getPsDeadline() != null ? r.getPsDeadline().toString() : "", dataFont, rowColor);
            addPdfCell(table, r.getPsEndDate() != null ? r.getPsEndDate().toString() : "", dataFont, rowColor);
            addPdfCell(table, r.getPsQuantity() != null ? r.getPsQuantity().toString() : "", dataFont, rowColor);
            addPdfCell(table, r.getPsStatus() != null ? r.getPsStatus() : "", dataFont, rowColor);

            alternate = !alternate;
        }

        document.add(table);
        document.close();
        return out.toByteArray();
    }

    // Helper method for PDF cells
    private void addPdfCell(PdfPTable table, String text, com.itextpdf.text.Font font, BaseColor bgColor) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(bgColor);
        cell.setPadding(5f);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }
}
