package com.turbolessons.paymentservice.service.invoice;

import com.turbolessons.paymentservice.dto.InvoiceData;
import com.turbolessons.paymentservice.dto.LineItemData;
import reactor.core.publisher.Mono;

import java.util.List;

public interface InvoiceService {
    Mono<List<InvoiceData>> listAllInvoices();

    Mono<List<InvoiceData>> listAllInvoiceByCustomer(String customerId);


    Mono<List<InvoiceData>> listAllInvoiceBySubscription(String subscriptionId);

    Mono<InvoiceData> retrieveInvoice(String id);

    Mono<InvoiceData> retrieveUpcomingInvoice(String customerId);

    Mono<InvoiceData> createInvoice(InvoiceData invoiceDto);

    Mono<Void> updateInvoice(String id, InvoiceData invoiceDto);

    Mono<Void> deleteDraftInvoice(String id);

    Mono<Void> finalizeInvoice(String id);

    Mono<InvoiceData> payInvoice(String id);

    Mono<InvoiceData> voidInvoice(String id);

    Mono<InvoiceData> markInvoiceUncollectible(String id);

    Mono<List<LineItemData>> getLineItems(String id);

    Mono<List<LineItemData>> getUpcomingLineItems(String customerId);
}
