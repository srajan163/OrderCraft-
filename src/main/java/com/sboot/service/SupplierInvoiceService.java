// com.sboot.service.SupplierInvoiceService.java

package com.sboot.service;
 
import com.itextpdf.text.*;

import com.itextpdf.text.pdf.*;

import com.sboot.dto.SupplierOrderInvoiceDTO;

import org.springframework.stereotype.Service;
 
import java.io.ByteArrayOutputStream;
 
@Service

public class SupplierInvoiceService {
 
    public byte[] generateSupplierOrderPdf(SupplierOrderInvoiceDTO dto) throws Exception {

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Document doc = new Document();

        PdfWriter.getInstance(doc, out);

        doc.open();
 
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);

        doc.add(new Paragraph("Supplier Order Invoice", titleFont));

        doc.add(new Paragraph(" "));
 
        doc.add(new Paragraph("PO ID: " + dto.getPoId()));

        doc.add(new Paragraph("Supplier: " + dto.getSupplierName()));

        doc.add(new Paragraph("Order Date: " + dto.getOrderDate()));

        doc.add(new Paragraph("Expected Delivery: " + dto.getExpectedDeliveryDate()));

        doc.add(new Paragraph("Created By: " + dto.getCreatedBy()));

        doc.add(new Paragraph(" "));
 
        PdfPTable table = new PdfPTable(3);

        table.addCell("Product");

        table.addCell("Quantity");

        table.addCell("Unit Price");
 
        for (SupplierOrderInvoiceDTO.Item item : dto.getItems()) {

            table.addCell(item.getProductName());

            table.addCell(String.valueOf(item.getQuantity()));

            table.addCell(String.valueOf(item.getUnitPrice()));

        }
 
        doc.add(table);

        doc.close();

        return out.toByteArray();

    }

}

 