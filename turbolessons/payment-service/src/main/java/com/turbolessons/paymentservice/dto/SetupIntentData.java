package com.turbolessons.paymentservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class SetupIntentData {

    String id;
    String customer;
    String paymentMethod;
    String description;
}
