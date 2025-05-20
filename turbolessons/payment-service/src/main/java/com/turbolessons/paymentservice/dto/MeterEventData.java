package com.turbolessons.paymentservice.dto;

public record MeterEventData(String identifier, String eventName, String stripeCustomerId, String value) {
}
