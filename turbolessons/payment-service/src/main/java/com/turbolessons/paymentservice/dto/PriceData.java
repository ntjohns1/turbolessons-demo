package com.turbolessons.paymentservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class PriceData {

    private String id;
//    In cents;
    private Long unitAmount;
    private String currency = "usd";
    private String lookupKey;
    private String product;
    private Boolean isActive = true;
    private Boolean isRecurring = true;
}
