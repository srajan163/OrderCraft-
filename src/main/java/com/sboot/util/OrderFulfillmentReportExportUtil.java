package com.sboot.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class OrderFulfillmentReportExportUtil {

    /**
     * Exports list of maps (each map is one purchase order) to PDF.
     * Columns are the union of all keys across the maps (ordered).
     */
    public static byte[] exportEntitiesPdf(java.util.List<Map<String, Object>> rows) throws Exception {
        // Determine column headers (union of keys) preserving a simple stable order:
        LinkedHashSet<String> headers = new LinkedHashSet<>();
        for (Map<String, Object> r : rows) {
            headers.addAll(r.keySet());
        }

        // If no rows, produce a simple PDF with "No data"
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);
        document.open();

        Font title = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        document.add(new Paragraph("Purchase Orders - Full Report", title));
        document.add(Chunk.NEWLINE);

        if (rows.isEmpty()) {
            document.add(new Paragraph("No purchase orders found"));
            document.close();
            return baos.toByteArray();
        }

        int columnCount = Math.max(1, headers.size());
        PdfPTable table = new PdfPTable(columnCount);
        table.setWidthPercentage(100f);

        // Header cells
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h));
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);
        }

        // Rows
        for (Map<String, Object> r : rows) {
            for (String col : headers) {
                Object v = r.get(col);
                String text = v == null ? "" : v.toString();
                PdfPCell cell = new PdfPCell(new Phrase(text));
                table.addCell(cell);
            }
        }

        document.add(table);
        document.close();
        return baos.toByteArray();
    }

    /**
     * Exports list of maps to CSV (UTF-8).
     */
    public static byte[] exportEntitiesCsv(java.util.List<Map<String, Object>> rows) {
        // union of headers
        LinkedHashSet<String> headers = new LinkedHashSet<>();
        for (Map<String, Object> r : rows) headers.addAll(r.keySet());

        StringBuilder sb = new StringBuilder();
        // header line
        String headerLine = headers.stream().collect(Collectors.joining(","));
        sb.append(headerLine).append("\n");

        for (Map<String, Object> r : rows) {
            java.util.List<String> values = new ArrayList<>();
            for (String h : headers) {
                Object v = r.get(h);
                values.add(escapeCsv(v == null ? "" : v.toString()));
            }
            sb.append(String.join(",", values)).append("\n");
        }
        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    private static String escapeCsv(String s) {
        if (s == null) return "";
        if (s.contains(",") || s.contains("\n") || s.contains("\r") || s.contains("\"")) {
            return "\"" + s.replace("\"", "\"\"") + "\"";
        }
        return s;
    }
}
