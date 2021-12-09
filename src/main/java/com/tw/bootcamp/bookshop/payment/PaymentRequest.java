package com.tw.bootcamp.bookshop.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class PaymentRequest {

    private Integer orderId;
    private CardDetails cardDetails;

}
