package com.turbolessons.paymentservice.dto;

public record InvoiceData(String id, String account_name, String customer, long amount_due, long amount_paid,
                          long amount_remaining, long attempt_count, boolean attempted, long created, long due_date,
                          long effective_at, long ending_balance, long next_payment_attempt, boolean paid) {

}
