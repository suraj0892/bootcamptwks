package com.tw.bootcamp.bookshop.payment;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
public class CardDetails {
        private Double amount;
        private String creditCardNumber;
        private String creditCardExpiration;
        private Integer cardSecurityCode;
}
