package com.turbolessons.paymentservice.dto;

public record LineItemData(String id, long amount, String description, long periodStart, long periodEnd, String priceId,
                           String invoiceId) {
}
