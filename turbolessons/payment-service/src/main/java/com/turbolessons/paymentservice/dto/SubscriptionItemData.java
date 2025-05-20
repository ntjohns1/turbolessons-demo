package com.turbolessons.paymentservice.dto;

public record SubscriptionItemData(
    String id,
    String subscription,
    String price,
    Integer quantity,
    String priceData,
    Long proration_date,
    String[] tax_rates,
    Boolean deleted,
    String metadata
) {
    // Static factory methods instead of additional constructors to avoid duplication
    
    // Factory method for creating a new subscription item
    public static SubscriptionItemData forCreate(String subscription, String price, Integer quantity) {
        return new SubscriptionItemData(null, subscription, price, quantity, null, null, null, null, null);
    }
    
    // Factory method for updating an existing subscription item
    public static SubscriptionItemData forUpdate(String id, String price, Integer quantity) {
        return new SubscriptionItemData(id, null, price, quantity, null, null, null, null, null);
    }
}
