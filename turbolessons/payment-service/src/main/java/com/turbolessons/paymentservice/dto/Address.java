package com.turbolessons.paymentservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Address {

    String city;
    String country;
    String line1;
    String line2;
    String postalCode;
    String state;

}
