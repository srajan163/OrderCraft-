package com.sboot.util;

 
import com.itextpdf.text.*;

import com.itextpdf.text.pdf.*;

import com.sboot.dto.InventoryTransactionReportDTO;

import com.sboot.dto.StockReportDTO;
 
import java.io.ByteArrayOutputStream;

import java.io.OutputStreamWriter;

import java.time.format.DateTimeFormatter;

import java.util.List;

import java.util.stream.Stream;

public class InventoryReportExportUtil {

    /** ===== Stock Level Export ===== */

    public static byte[] exportStockReportPdf(List<StockReportDTO> report) throws Exception {

        Document document = new Document();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, baos);

        document.open();

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);

        Paragraph title = new Paragraph("Stock Level Report", font);

        title.setAlignment(Element.ALIGN_CENTER);

        document.add(title);

        document.add(Chunk.NEWLINE);

        PdfPTable table = new PdfPTable(8);

        table.setWidthPercentage(100);

        table.setWidths(new int[]{2, 4, 5, 3, 2, 2, 2, 3});

        Stream.of("ID", "Name", "Description", "Category", "Unit Price", "Quantity", "Min Threshold", "Last Updated")

                .forEach(header -> {

                    PdfPCell cell = new PdfPCell();

                    cell.setPhrase(new Phrase(header));

                    cell.setBackgroundColor(BaseColor.LIGHT_GRAY);

                    table.addCell(cell);

                });

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (StockReportDTO dto : report) {

            table.addCell(String.valueOf(dto.getProductId()));

            table.addCell(dto.getProductName());

            table.addCell(dto.getProductDescription() != null ? dto.getProductDescription() : "");

            table.addCell(dto.getCategoryName() != null ? dto.getCategoryName() : "");

            table.addCell(String.valueOf(dto.getUnitPrice()));

            table.addCell(String.valueOf(dto.getQuantity()));

            table.addCell(String.valueOf(dto.getMinThreshold()));

            table.addCell(dto.getLastUpdated() != null ? dto.getLastUpdated().format(dtf) : "");

        }

        document.add(table);

        document.close();

        return baos.toByteArray();

    }

    public static byte[] exportStockReportCsv(List<StockReportDTO> report) throws Exception {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        OutputStreamWriter writer = new OutputStreamWriter(baos);

        // Header

        writer.write("ID,Name,Description,Category,Unit Price,Quantity,Min Threshold,Max Threshold,Last Updated\n");

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (StockReportDTO dto : report) {

            writer.write(String.format("%d,%s,%s,%s,%.2f,%d,%d,%d,%s\n",

                    dto.getProductId(),

                    dto.getProductName().replaceAll(",", " "), // remove commas from text

                    dto.getProductDescription() != null ? dto.getProductDescription().replaceAll(",", " ") : "",

                    dto.getCategoryName() != null ? dto.getCategoryName().replaceAll(",", " ") : "",

                    dto.getUnitPrice(),

                    dto.getQuantity(),

                    dto.getMinThreshold(),

                    dto.getMaxThreshold(),

                    dto.getLastUpdated() != null ? dto.getLastUpdated().format(dtf) : ""

            ));

        }

        writer.flush();

        writer.close();

        return baos.toByteArray();

    }


    /** ===== Inventory Transaction Export ===== */

    public static byte[] exportTransactionReportPdf(List<InventoryTransactionReportDTO> report) throws Exception {

        Document document = new Document();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, baos);

        document.open();

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);

        Paragraph title = new Paragraph("Inventory Transaction Report", font);

        title.setAlignment(Element.ALIGN_CENTER);

        document.add(title);

        document.add(Chunk.NEWLINE);

        PdfPTable table = new PdfPTable(5);

        table.setWidthPercentage(100);

        table.setWidths(new int[]{2, 4, 3, 2, 3});

        Stream.of("ID", "Product Name", "Product ID", "Quantity", "Date")

                .forEach(header -> {

                    PdfPCell cell = new PdfPCell();

                    cell.setPhrase(new Phrase(header));

                    cell.setBackgroundColor(BaseColor.LIGHT_GRAY);

                    table.addCell(cell);

                });

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (InventoryTransactionReportDTO dto : report) {

            table.addCell(String.valueOf(dto.getTransactionId()));

            table.addCell(dto.getProductName() != null ? dto.getProductName() : "");

            table.addCell(String.valueOf(dto.getProductId()));

            table.addCell(String.valueOf(dto.getQuantity()));

            table.addCell(dto.getTransactionDate() != null ? dto.getTransactionDate().format(dtf) : "");

        }

        document.add(table);

        document.close();

        return baos.toByteArray();

    }

    public static byte[] exportTransactionReportCsv(List<InventoryTransactionReportDTO> report) throws Exception {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        OutputStreamWriter writer = new OutputStreamWriter(baos);

        // Header

        writer.write("Transaction ID,Product Name,Product ID,Quantity,Transaction Type,Transaction Date\n");

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (InventoryTransactionReportDTO dto : report) {

            String line = String.format("%d,%s,%d,%d,%s,%s\n",

                    dto.getTransactionId(),

                    dto.getProductName() != null ? dto.getProductName().replaceAll(",", " ") : "",

                    dto.getProductId(),

                    dto.getQuantity(),

                    dto.getTransactionType() != null ? dto.getTransactionType().replaceAll(",", " ") : "",

                    dto.getTransactionDate() != null ? dto.getTransactionDate().format(dtf) : ""

            );

            writer.write(line);

        }

        writer.flush();

        writer.close();

        return baos.toByteArray();

    }


}

 