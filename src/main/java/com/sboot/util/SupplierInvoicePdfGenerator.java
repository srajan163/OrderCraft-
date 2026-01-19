package com.sboot.util;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.sboot.dto.SupplierInvoiceResponseDTO;

public class SupplierInvoicePdfGenerator {

    public static byte[] generatePdf(List<SupplierInvoiceResponseDTO> invoiceItems) throws Exception {
        if (invoiceItems == null || invoiceItems.isEmpty()) {
            throw new IllegalArgumentException("No invoice items to print.");
        }

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);
        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph title = new Paragraph("Supplier Invoice", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(Chunk.NEWLINE);

        SupplierInvoiceResponseDTO first = invoiceItems.get(0);

        // Header Info
        document.add(new Paragraph("Purchase Order ID: " + first.getPurchaseOrderId()));
        document.add(new Paragraph("Date: " + LocalDate.now()));
        document.add(Chunk.NEWLINE);

        // Supplier Details
        document.add(new Paragraph("Supplier Name: " + first.getSupplierName()));
        document.add(new Paragraph("Email: " + first.getSupplierEmail()));
        document.add(new Paragraph("Contact: " + first.getSupplierContact()));
        if (first.getSupplierAddress() != null) {
            document.add(new Paragraph("Address: " + first.getSupplierAddress()));
        }
        document.add(Chunk.NEWLINE);

        // Material Table
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.addCell("Material");
        table.addCell("Quantity");
        table.addCell("Unit Price");
        table.addCell("Total");

        double grandTotal = 0;
        for (SupplierInvoiceResponseDTO item : invoiceItems) {
            table.addCell(item.getMaterialName());
            table.addCell(String.valueOf(item.getQuantity()));
            table.addCell("₹" + String.format("%.2f", item.getUnitPrice()));
            table.addCell("₹" + String.format("%.2f", item.getTotal()));
            grandTotal += item.getTotal();
        }

        document.add(table);
        document.add(Chunk.NEWLINE);

        // Totals
        double gst = grandTotal * 0.18;
        double finalAmount = grandTotal + gst;

        document.add(new Paragraph("Subtotal: ₹" + String.format("%.2f", grandTotal)));
        document.add(new Paragraph("GST (18%): ₹" + String.format("%.2f", gst)));
        document.add(new Paragraph("Total Payable: ₹" + String.format("%.2f", finalAmount)));

        document.close();
        return out.toByteArray();
    }
}
