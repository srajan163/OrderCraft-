
package com.sboot.service;

import com.sboot.dto.InvoiceResponseDTO;

public interface InvoiceService {
    InvoiceResponseDTO generateInvoice(Long orderId);
}
