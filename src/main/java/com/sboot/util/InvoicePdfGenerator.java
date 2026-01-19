package com.sboot.util;

//import com.github.librepdf.openpdf.text.*;
//import com.github.librepdf.openpdf.text.pdf.*;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.sboot.dto.InvoiceResponseDTO;
import com.sboot.dto.OrderItemDTO;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.util.List;

public class InvoicePdfGenerator {

    public static byte[] generatePdf(InvoiceResponseDTO invoice) throws Exception {

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, out);
        document.open();

        // ===============================
        // Company Branding
        // ===============================
        Font companyFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, new BaseColor(0, 102, 204) );
        Paragraph companyName = new Paragraph("OrderCraft", companyFont);
        companyName.setAlignment(Element.ALIGN_CENTER);
        document.add(companyName);

        // ===============================
        // Invoice Heading
        // ===============================
        Font headingFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
        Paragraph heading = new Paragraph("Invoice for Order #" + invoice.getOrderId(), headingFont);
        heading.setAlignment(Element.ALIGN_CENTER);
        document.add(heading);
        document.add(Chunk.NEWLINE);

        // ===============================
        // Customer Details
        // ===============================
        Font infoFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
        document.add(new Paragraph("Customer Name: " + invoice.getCustomerName(), infoFont));
        document.add(new Paragraph("Email: " + invoice.getCustomerEmail(), infoFont));
        document.add(new Paragraph("Mobile: " + invoice.getPhoneNumber(), infoFont));
        document.add(new Paragraph("Address: " + invoice.getBillingAddress(), infoFont));
        document.add(Chunk.NEWLINE);

        // ===============================
        // Product Table
        // ===============================
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{4f, 3f, 2f, 3f}); // column widths

        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
        String[] headers = {"Product Name", "Category", "Quantity", "Unit Price"};

        for (String title : headers) {
            PdfPCell headerCell = new PdfPCell(new Phrase(title, headerFont));
            headerCell.setBackgroundColor(new BaseColor(0, 102, 204));
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.setPadding(5);
            table.addCell(headerCell);
        }

        // ===============================
        // Item Rows
        // ===============================
        List<OrderItemDTO> items = invoice.getItems();
        double totalAmount = 0.0;

        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
        for (OrderItemDTO item : items) {
            table.addCell(new PdfPCell(new Phrase(item.getProductName(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(item.getCategory(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(item.getQuantity()), cellFont)));
            table.addCell(new PdfPCell(new Phrase(String.format("₹%.2f", item.getUnitPrice()), cellFont)));
            totalAmount += item.getQuantity() * item.getUnitPrice();
        }

        document.add(table);
        document.add(Chunk.NEWLINE);

        // ===============================
        // Totals & GST
        // ===============================
        double gst = totalAmount * 0.18;
        double finalAmount = totalAmount + gst;

        Font totalFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, new BaseColor(0, 102, 0));
        Paragraph subtotal = new Paragraph("Subtotal: ₹" + String.format("%.2f", totalAmount), totalFont);
        Paragraph gstPara = new Paragraph("GST (18%): ₹" + String.format("%.2f", gst), totalFont);
        Paragraph totalPara = new Paragraph("Total Amount to Pay: ₹" + String.format("%.2f", finalAmount), totalFont);

        subtotal.setAlignment(Element.ALIGN_RIGHT);
        gstPara.setAlignment(Element.ALIGN_RIGHT);
        totalPara.setAlignment(Element.ALIGN_RIGHT);

        document.add(subtotal);
        document.add(gstPara);
        document.add(totalPara);

        document.close();
        return out.toByteArray();
    }
}

//Updated 
