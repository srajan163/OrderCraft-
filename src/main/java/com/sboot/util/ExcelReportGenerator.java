package com.sboot.util;

import com.sboot.dto.SupplierPerformanceReportDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.Map;

@Component
public class ExcelReportGenerator {

    public byte[] generateSupplierPerformanceExcel(SupplierPerformanceReportDTO dto) throws Exception {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Supplier Performance");

            int rowIdx = 0;
            // Title
            Row title = sheet.createRow(rowIdx++);
            Cell tcell = title.createCell(0);
            tcell.setCellValue("Supplier Performance Report");

            // header style
            CellStyle headerStyle = workbook.createCellStyle();
            Font hfont = workbook.createFont();
            hfont.setBold(true);
            headerStyle.setFont(hfont);

            rowIdx++; // blank

            // Info rows
            rowIdx = createKeyValueRow(sheet, rowIdx, "Supplier ID", String.valueOf(dto.getSupplierId()), headerStyle);
            rowIdx = createKeyValueRow(sheet, rowIdx, "Supplier Name", dto.getSupplierName(), headerStyle);
            rowIdx = createKeyValueRow(sheet, rowIdx, "Email", dto.getSupplierEmail(), headerStyle);
            rowIdx = createKeyValueRow(sheet, rowIdx, "Phone", dto.getSupplierPhone(), headerStyle);
            rowIdx = createKeyValueRow(sheet, rowIdx, "Supplier.suppliersRating (field)", dto.getSupplierRatingField() == null ? "-" : String.valueOf(dto.getSupplierRatingField()), headerStyle);

            rowIdx++; // blank

            rowIdx = createKeyValueRow(sheet, rowIdx, "Average Rating", String.format("%.2f", dto.getAverageRating()), headerStyle);
            rowIdx = createKeyValueRow(sheet, rowIdx, "Total Reviews", String.valueOf(dto.getTotalReviews()), headerStyle);
            if (dto.getLatestReview() != null) {
                rowIdx = createKeyValueRow(sheet, rowIdx, "Latest Rating", String.valueOf(dto.getLatestReview().getRating()), headerStyle);
                rowIdx = createKeyValueRow(sheet, rowIdx, "Latest Review Date", dto.getLatestReview().getDate() == null ? "-" : dto.getLatestReview().getDate().toString(), headerStyle);
            }

            rowIdx++; // blank

            // Breakdown header
            Row brkHeader = sheet.createRow(rowIdx++);
            Cell brc1 = brkHeader.createCell(0); brc1.setCellValue("Rating"); brc1.setCellStyle(headerStyle);
            Cell brc2 = brkHeader.createCell(1); brc2.setCellValue("Count"); brc2.setCellStyle(headerStyle);
            Cell brc3 = brkHeader.createCell(2); brc3.setCellValue("Percentage"); brc3.setCellStyle(headerStyle);

            for (Map.Entry<Integer, Long> e : dto.getRatingCounts().entrySet()) {
                Row r = sheet.createRow(rowIdx++);
                r.createCell(0).setCellValue(e.getKey());
                r.createCell(1).setCellValue(e.getValue());
                Double pct = dto.getRatingPercentages().getOrDefault(e.getKey(), 0.0);
                r.createCell(2).setCellValue(String.format("%.2f%%", pct));
            }

            // auto-size
            for (int i = 0; i < 4; i++) sheet.autoSizeColumn(i);

            workbook.write(baos);
            return baos.toByteArray();
        }
    }

    private int createKeyValueRow(Sheet sheet, int rowIdx, String key, String value, CellStyle headerStyle) {
        Row r = sheet.createRow(rowIdx++);
        Cell c0 = r.createCell(0);
        c0.setCellValue(key);
        c0.setCellStyle(headerStyle);
        r.createCell(1).setCellValue(value == null ? "-" : value);
        return rowIdx;
    }
}
