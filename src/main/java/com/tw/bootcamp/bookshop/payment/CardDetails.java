package com.tw.bootcamp.bookshop.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class CardDetails {
        private Double amount;
        private String creditCardNumber;
        private String creditCardExpiration;
        private Integer cardSecurityCode;
}
