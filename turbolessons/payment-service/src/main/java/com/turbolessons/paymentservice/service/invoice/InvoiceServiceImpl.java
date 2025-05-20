package com.turbolessons.paymentservice.service.invoice;

import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.Invoice;
import com.stripe.model.InvoiceLineItem;
import com.stripe.param.*;
import com.turbolessons.paymentservice.dto.InvoiceData;
import com.turbolessons.paymentservice.dto.LineItemData;
import com.turbolessons.paymentservice.service.StripeClientHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
public class InvoiceServiceImpl implements InvoiceService {
    private final StripeClient stripeClient;
    private final StripeClientHelper stripeClientHelper;

    public InvoiceServiceImpl(StripeClient stripeClient, StripeClientHelper stripeClientHelper) {
        this.stripeClient = stripeClient;
        this.stripeClientHelper = stripeClientHelper;
    }

    private InvoiceData mapInvoiceData(Invoice invoice) {
        try {
            // Handle potential null values for Long fields
            Long dueDate = invoice.getDueDate();
            Long effectiveAt = invoice.getEffectiveAt();
            Long nextPaymentAttempt = invoice.getNextPaymentAttempt();
            
            return new InvoiceData(invoice.getId(),
                                invoice.getAccountName(),
                                invoice.getCustomer(),
                                invoice.getAmountDue(),
                                invoice.getAmountPaid(),
                                invoice.getAmountRemaining(),
                                invoice.getAttemptCount(),
                                invoice.getAttempted(),
                                invoice.getCreated(),
                                dueDate != null ? dueDate : 0L,
                                effectiveAt != null ? effectiveAt : 0L,
                                invoice.getEndingBalance(),
                                nextPaymentAttempt != null ? nextPaymentAttempt : 0L,
                                invoice.getPaid());
        } catch (Exception e) {
            log.error("Error mapping Invoice to InvoiceData: {}", invoice.getId(), e);
            throw e;
        }
    }

    private LineItemData mapLineItemData(InvoiceLineItem lineItem) {
        try {
            return new LineItemData(lineItem.getId(),
                                    lineItem.getAmount(),
                                    lineItem.getDescription(),
                                    lineItem.getPeriod()
                                            .getStart(),
                                    lineItem.getPeriod()
                                            .getEnd(),
                                    lineItem.getPrice()
                                            .getId(),
                                    lineItem.getInvoice());
        } catch (Exception e) {
            log.error("Error mapping InvoiceLineItem to LineItemData: {}", lineItem.getId(), e);
            throw e;
        }
    }

    @Override
    public Mono<List<InvoiceData>> listAllInvoices() {
        try {
            return stripeClientHelper.executeStripeCall(() -> {
                try {
                    return stripeClient.invoices().list();
                } catch (StripeException e) {
                    log.error("Stripe exception while listing all invoices", e);
                    throw new RuntimeException("Error listing all invoices from Stripe", e);
                } catch (Exception e) {
                    log.error("Unexpected exception while listing all invoices", e);
                    throw e;
                }
            })
            .map(stripeCollection -> {
                if (stripeCollection != null && stripeCollection.getData() != null) {
                    return stripeCollection.getData()
                            .stream()
                            .map(this::mapInvoiceData)
                            .toList();
                }
                return List.<InvoiceData>of();
            })
            .doOnError(e -> log.error("Error in listAllInvoices", e));
        } catch (Exception e) {
            log.error("Unexpected exception in listAllInvoices method", e);
            return Mono.error(e);
        }
    }

    @Override
    public Mono<List<InvoiceData>> listAllInvoiceByCustomer(String customerId) {
        try {
            InvoiceListParams params = InvoiceListParams.builder()
                    .setCustomer(customerId)
                    .build();
            
            return stripeClientHelper.executeStripeCall(() -> {
                try {
                    return stripeClient.invoices().list(params);
                } catch (StripeException e) {
                    log.error("Stripe exception while listing invoices for customer: {}", customerId, e);
                    throw new RuntimeException("Error listing invoices for customer from Stripe", e);
                } catch (Exception e) {
                    log.error("Unexpected exception while listing invoices for customer: {}", customerId, e);
                    throw e;
                }
            })
            .map(stripeCollection -> {
                if (stripeCollection != null && stripeCollection.getData() != null) {
                    return stripeCollection.getData()
                            .stream()
                            .map(this::mapInvoiceData)
                            .toList();
                }
                return List.<InvoiceData>of();
            })
            .doOnError(e -> log.error("Error in listAllInvoiceByCustomer for customer: {}", customerId, e));
        } catch (Exception e) {
            log.error("Unexpected exception in listAllInvoiceByCustomer method for customer: {}", customerId, e);
            return Mono.error(e);
        }
    }

    @Override
    public Mono<List<InvoiceData>> listAllInvoiceBySubscription(String subscriptionId) {
        try {
            InvoiceListParams params = InvoiceListParams.builder()
                    .setSubscription(subscriptionId)
                    .build();
            
            return stripeClientHelper.executeStripeCall(() -> {
                try {
                    return stripeClient.invoices().list(params);
                } catch (StripeException e) {
                    log.error("Stripe exception while listing invoices for subscription: {}", subscriptionId, e);
                    throw new RuntimeException("Error listing invoices for subscription from Stripe", e);
                } catch (Exception e) {
                    log.error("Unexpected exception while listing invoices for subscription: {}", subscriptionId, e);
                    throw e;
                }
            })
            .map(stripeCollection -> {
                if (stripeCollection != null && stripeCollection.getData() != null) {
                    return stripeCollection.getData()
                            .stream()
                            .map(this::mapInvoiceData)
                            .toList();
                }
                return List.<InvoiceData>of();
            })
            .doOnError(e -> log.error("Error in listAllInvoiceBySubscription for subscription: {}", subscriptionId, e));
        } catch (Exception e) {
            log.error("Unexpected exception in listAllInvoiceBySubscription method for subscription: {}", subscriptionId, e);
            return Mono.error(e);
        }
    }

    @Override
    public Mono<InvoiceData> retrieveInvoice(String id) {
        try {
            return stripeClientHelper.executeStripeCall(() -> {
                try {
                    return stripeClient.invoices().retrieve(id);
                } catch (StripeException e) {
                    log.error("Stripe exception while retrieving invoice: {}", id, e);
                    throw new RuntimeException("Error retrieving invoice from Stripe", e);
                } catch (Exception e) {
                    log.error("Unexpected exception while retrieving invoice: {}", id, e);
                    throw e;
                }
            })
            .map(this::mapInvoiceData)
            .doOnError(e -> log.error("Error in retrieveInvoice for invoice: {}", id, e));
        } catch (Exception e) {
            log.error("Unexpected exception in retrieveInvoice method for invoice: {}", id, e);
            return Mono.error(e);
        }
    }

    @Override
    public Mono<InvoiceData> retrieveUpcomingInvoice(String customerId) {
        try {
            InvoiceUpcomingParams params = InvoiceUpcomingParams.builder()
                    .setCustomer(customerId)
                    .build();
            
            return stripeClientHelper.executeStripeCall(() -> {
                try {
                    return stripeClient.invoices().upcoming(params);
                } catch (StripeException e) {
                    log.error("Stripe exception while retrieving upcoming invoice for customer: {}", customerId, e);
                    throw new RuntimeException("Error retrieving upcoming invoice from Stripe", e);
                } catch (Exception e) {
                    log.error("Unexpected exception while retrieving upcoming invoice for customer: {}", customerId, e);
                    throw e;
                }
            })
            .map(this::mapInvoiceData)
            .doOnError(e -> log.error("Error in retrieveUpcomingInvoice for customer: {}", customerId, e));
        } catch (Exception e) {
            log.error("Unexpected exception in retrieveUpcomingInvoice method for customer: {}", customerId, e);
            return Mono.error(e);
        }
    }

    @Override
    public Mono<InvoiceData> createInvoice(InvoiceData invoiceData) {
        try {
            InvoiceCreateParams params = InvoiceCreateParams.builder()
                    .setCustomer(invoiceData.customer())
                    .build();
            
            return stripeClientHelper.executeStripeCall(() -> {
                try {
                    return stripeClient.invoices().create(params);
                } catch (StripeException e) {
                    log.error("Stripe exception while creating invoice for customer: {}", invoiceData.customer(), e);
                    throw new RuntimeException("Error creating invoice in Stripe", e);
                } catch (Exception e) {
                    log.error("Unexpected exception while creating invoice for customer: {}", invoiceData.customer(), e);
                    throw e;
                }
            })
            .map(this::mapInvoiceData)
            .doOnError(e -> log.error("Error in createInvoice for customer: {}", invoiceData.customer(), e));
        } catch (Exception e) {
            log.error("Unexpected exception in createInvoice method for customer: {}", invoiceData.customer(), e);
            return Mono.error(e);
        }
    }

    @Override
    public Mono<Void> updateInvoice(String id, InvoiceData invoiceData) {
        try {
            InvoiceUpdateParams params = InvoiceUpdateParams.builder()
                    .setDueDate(invoiceData.due_date())
                    .setEffectiveAt(invoiceData.effective_at())
                    .build();
            
            return stripeClientHelper.executeStripeVoidCall(() -> {
                try {
                    stripeClient.invoices().update(id, params);
                } catch (StripeException e) {
                    log.error("Stripe exception while updating invoice: {}", id, e);
                    throw new RuntimeException("Error updating invoice in Stripe", e);
                } catch (Exception e) {
                    log.error("Unexpected exception while updating invoice: {}", id, e);
                    throw e;
                }
            })
            .doOnError(e -> log.error("Error in updateInvoice for invoice: {}", id, e));
        } catch (Exception e) {
            log.error("Unexpected exception in updateInvoice method for invoice: {}", id, e);
            return Mono.error(e);
        }
    }

    @Override
    public Mono<Void> deleteDraftInvoice(String id) {
        try {
            return stripeClientHelper.executeStripeVoidCall(() -> {
                try {
                    stripeClient.invoices().delete(id);
                } catch (StripeException e) {
                    log.error("Stripe exception while deleting draft invoice: {}", id, e);
                    throw new RuntimeException("Error deleting draft invoice in Stripe", e);
                } catch (Exception e) {
                    log.error("Unexpected exception while deleting draft invoice: {}", id, e);
                    throw e;
                }
            })
            .doOnError(e -> log.error("Error in deleteDraftInvoice for invoice: {}", id, e));
        } catch (Exception e) {
            log.error("Unexpected exception in deleteDraftInvoice method for invoice: {}", id, e);
            return Mono.error(e);
        }
    }

    @Override
    public Mono<Void> finalizeInvoice(String id) {
        try {
            return stripeClientHelper.executeStripeVoidCall(() -> {
                try {
                    stripeClient.invoices().finalizeInvoice(id);
                } catch (StripeException e) {
                    log.error("Stripe exception while finalizing invoice: {}", id, e);
                    throw new RuntimeException("Error finalizing invoice in Stripe", e);
                } catch (Exception e) {
                    log.error("Unexpected exception while finalizing invoice: {}", id, e);
                    throw e;
                }
            })
            .doOnError(e -> log.error("Error in finalizeInvoice for invoice: {}", id, e));
        } catch (Exception e) {
            log.error("Unexpected exception in finalizeInvoice method for invoice: {}", id, e);
            return Mono.error(e);
        }
    }

    @Override
    public Mono<InvoiceData> payInvoice(String id) {
        try {
            return stripeClientHelper.executeStripeCall(() -> {
                try {
                    return stripeClient.invoices().pay(id);
                } catch (StripeException e) {
                    log.error("Stripe exception while paying invoice: {}", id, e);
                    throw new RuntimeException("Error paying invoice in Stripe", e);
                } catch (Exception e) {
                    log.error("Unexpected exception while paying invoice: {}", id, e);
                    throw e;
                }
            })
            .map(this::mapInvoiceData)
            .doOnError(e -> log.error("Error in payInvoice for invoice: {}", id, e));
        } catch (Exception e) {
            log.error("Unexpected exception in payInvoice method for invoice: {}", id, e);
            return Mono.error(e);
        }
    }

    @Override
    public Mono<InvoiceData> voidInvoice(String id) {
        try {
            return stripeClientHelper.executeStripeCall(() -> {
                try {
                    return stripeClient.invoices().voidInvoice(id);
                } catch (StripeException e) {
                    log.error("Stripe exception while voiding invoice: {}", id, e);
                    throw new RuntimeException("Error voiding invoice in Stripe", e);
                } catch (Exception e) {
                    log.error("Unexpected exception while voiding invoice: {}", id, e);
                    throw e;
                }
            })
            .map(this::mapInvoiceData)
            .doOnError(e -> log.error("Error in voidInvoice for invoice: {}", id, e));
        } catch (Exception e) {
            log.error("Unexpected exception in voidInvoice method for invoice: {}", id, e);
            return Mono.error(e);
        }
    }

    @Override
    public Mono<InvoiceData> markInvoiceUncollectible(String id) {
        try {
            return stripeClientHelper.executeStripeCall(() -> {
                try {
                    return stripeClient.invoices().markUncollectible(id);
                } catch (StripeException e) {
                    log.error("Stripe exception while marking invoice as uncollectible: {}", id, e);
                    throw new RuntimeException("Error marking invoice as uncollectible in Stripe", e);
                } catch (Exception e) {
                    log.error("Unexpected exception while marking invoice as uncollectible: {}", id, e);
                    throw e;
                }
            })
            .map(this::mapInvoiceData)
            .doOnError(e -> log.error("Error in markInvoiceUncollectible for invoice: {}", id, e));
        } catch (Exception e) {
            log.error("Unexpected exception in markInvoiceUncollectible method for invoice: {}", id, e);
            return Mono.error(e);
        }
    }

    @Override
    public Mono<List<LineItemData>> getLineItems(String id) {
        try {
            return stripeClientHelper.executeStripeCall(() -> {
                try {
                    return stripeClient.invoices().lineItems().list(id);
                } catch (StripeException e) {
                    log.error("Stripe exception while getting line items for invoice: {}", id, e);
                    throw new RuntimeException("Error getting line items for invoice from Stripe", e);
                } catch (Exception e) {
                    log.error("Unexpected exception while getting line items for invoice: {}", id, e);
                    throw e;
                }
            })
            .map(stripeCollection -> {
                if (stripeCollection != null && stripeCollection.getData() != null) {
                    return stripeCollection.getData()
                            .stream()
                            .map(this::mapLineItemData)
                            .toList();
                }
                return List.<LineItemData>of();
            })
            .doOnError(e -> log.error("Error in getLineItems for invoice: {}", id, e));
        } catch (Exception e) {
            log.error("Unexpected exception in getLineItems method for invoice: {}", id, e);
            return Mono.error(e);
        }
    }

    @Override
    public Mono<List<LineItemData>> getUpcomingLineItems(String customerId) {
        try {
            InvoiceUpcomingLinesParams params = InvoiceUpcomingLinesParams.builder()
                    .setCustomer(customerId)
                    .build();
            
            return stripeClientHelper.executeStripeCall(() -> {
                try {
                    return Invoice.upcomingLines(params);
                } catch (StripeException e) {
                    log.error("Stripe exception while getting upcoming line items for customer: {}", customerId, e);
                    throw new RuntimeException("Error getting upcoming line items from Stripe", e);
                } catch (Exception e) {
                    log.error("Unexpected exception while getting upcoming line items for customer: {}", customerId, e);
                    throw e;
                }
            })
            .map(stripeCollection -> {
                if (stripeCollection != null && stripeCollection.getData() != null) {
                    return stripeCollection.getData()
                            .stream()
                            .map(this::mapLineItemData)
                            .toList();
                }
                return List.<LineItemData>of();
            })
            .doOnError(e -> log.error("Error in getUpcomingLineItems for customer: {}", customerId, e));
        } catch (Exception e) {
            log.error("Unexpected exception in getUpcomingLineItems method for customer: {}", customerId, e);
            return Mono.error(e);
        }
    }
}
