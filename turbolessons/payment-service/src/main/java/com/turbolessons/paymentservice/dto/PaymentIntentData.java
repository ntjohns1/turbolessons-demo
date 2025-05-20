package com.turbolessons.paymentservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class PaymentIntentData {

    String id;
    Long amount;
    String currency = "usd";
    String customer;
    String description;
    String paymentMethod;
    String invoice;
    String status;

}
