package com.turbolessons.paymentservice.controller.invoice;

import com.turbolessons.paymentservice.controller.BaseHandler;
import com.turbolessons.paymentservice.dto.InvoiceData;
import com.turbolessons.paymentservice.service.invoice.InvoiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class InvoiceHandlerImpl extends BaseHandler implements InvoiceHandler {

    private final InvoiceService invoiceService;

    public InvoiceHandlerImpl(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @Override
    public Mono<ServerResponse> listAll(ServerRequest r) {
        return handleList(r,
                          request -> invoiceService.listAllInvoices()
                                      .doOnError(e -> log.error("Error retrieving all invoices", e)),
                          new ParameterizedTypeReference<>() {});
    }

    @Override
    public Mono<ServerResponse> listAllByCustomer(ServerRequest r) {
        return handleList(r,
                          request -> {
                              String customerId = id(request);
                              return invoiceService.listAllInvoiceByCustomer(customerId)
                                      .doOnError(e -> log.error("Error retrieving invoices for customer: {}", customerId, e));
                          },
                          new ParameterizedTypeReference<>() {});
    }

    @Override
    public Mono<ServerResponse> listAllBySubscription(ServerRequest r) {
        return handleList(r,
                          request -> {
                              String subscriptionId = id(request);
                              return invoiceService.listAllInvoiceBySubscription(subscriptionId)
                                      .doOnError(e -> log.error("Error retrieving invoices for subscription: {}", subscriptionId, e));
                          },
                          new ParameterizedTypeReference<>() {});
    }

    @Override
    public Mono<ServerResponse> retrieve(ServerRequest r) {
        return handleRetrieve(r,
                              request -> {
                                  String invoiceId = id(request);
                                  return invoiceService.retrieveInvoice(invoiceId)
                                          .doOnError(e -> log.error("Error retrieving invoice: {}", invoiceId, e));
                              },
                              InvoiceData.class);
    }

    @Override
    public Mono<ServerResponse> retrieveUpcoming(ServerRequest r) {
        return handleRetrieve(r,
                              request -> {
                                  String customerId = id(request);
                                  return invoiceService.retrieveUpcomingInvoice(customerId)
                                          .doOnError(e -> log.error("Error retrieving upcoming invoice for customer: {}", customerId, e));
                              },
                              InvoiceData.class);
    }

    @Override
    public Mono<ServerResponse> create(ServerRequest r) {
        return handleCreate(r,
                            requestBody -> requestBody
                                    .flatMap(data -> this.invoiceService.createInvoice(data)
                                            .doOnError(e -> log.error("Error creating invoice", e))),
                            InvoiceData.class,
                            InvoiceData.class);
    }

    @Override
    public Mono<ServerResponse> update(ServerRequest r) {
        String id = id(r);
        return handleUpdate(r,
                            (idParam, requestBody) -> requestBody
                                    .flatMap(data -> this.invoiceService.updateInvoice(idParam, data)
                                            .doOnError(e -> log.error("Error updating invoice: {}", idParam, e))),
                            id,
                            InvoiceData.class);
    }

    @Override
    public Mono<ServerResponse> deleteDraft(ServerRequest r) {
        return handleDelete(r,
                            request -> {
                                String invoiceId = id(request);
                                return invoiceService.deleteDraftInvoice(invoiceId)
                                        .doOnError(e -> log.error("Error deleting draft invoice: {}", invoiceId, e));
                            });
    }

    @Override
    public Mono<ServerResponse> finalize(ServerRequest r) {
        return invoiceService.finalizeInvoice(id(r))
                .doOnError(e -> log.error("Error finalizing invoice: {}", id(r), e))
                .then(ServerResponse.noContent()
                              .build())
                .onErrorResume(e -> {
                    log.error("Error in finalize handler", e);
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .build();
                });
    }

    @Override
    public Mono<ServerResponse> payInvoice(ServerRequest r) {
        return invoiceService.payInvoice(id(r))
                .doOnError(e -> log.error("Error paying invoice: {}", id(r), e))
                .then(ServerResponse.ok()
                              .build())
                .onErrorResume(e -> {
                    log.error("Error in payInvoice handler", e);
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .build();
                });
    }

    @Override
    public Mono<ServerResponse> voidInvoice(ServerRequest r) {
        return invoiceService.voidInvoice(id(r))
                .doOnError(e -> log.error("Error voiding invoice: {}", id(r), e))
                .then(ServerResponse.ok()
                              .build())
                .onErrorResume(e -> {
                    log.error("Error in voidInvoice handler", e);
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .build();
                });
    }

    @Override
    public Mono<ServerResponse> markUncollectible(ServerRequest r) {
        return invoiceService.markInvoiceUncollectible(id(r))
                .doOnError(e -> log.error("Error marking invoice as uncollectible: {}", id(r), e))
                .then(ServerResponse.ok()
                              .build())
                .onErrorResume(e -> {
                    log.error("Error in markUncollectible handler", e);
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .build();
                });
    }

    @Override
    public Mono<ServerResponse> retrieveLineItems(ServerRequest r) {
        return handleSearch(r,
                            request -> {
                                String invoiceId = id(request);
                                return this.invoiceService.getLineItems(invoiceId)
                                        .doOnError(e -> log.error("Error retrieving line items for invoice: {}", invoiceId, e));
                            },
                            new ParameterizedTypeReference<>() {});
    }

    @Override
    public Mono<ServerResponse> retrieveUpcomingLineItems(ServerRequest r) {
        return handleSearch(r,
                            request -> {
                                String customerId = id(request);
                                return this.invoiceService.getUpcomingLineItems(customerId)
                                        .doOnError(e -> log.error("Error retrieving upcoming line items for customer: {}", customerId, e));
                            },
                            new ParameterizedTypeReference<>() {});
    }
}
