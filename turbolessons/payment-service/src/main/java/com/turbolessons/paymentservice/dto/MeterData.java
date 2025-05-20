package com.turbolessons.paymentservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MeterData(String id,
                        @JsonProperty("display_name")
                        String display_name,
                        @JsonProperty("event_name")
                        String event_name) {
}
