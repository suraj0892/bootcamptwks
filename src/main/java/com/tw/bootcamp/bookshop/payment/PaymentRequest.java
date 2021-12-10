package com.tw.bootcamp.bookshop.payment;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class PaymentRequest {

    private Integer orderId;
    private CardDetails cardDetails;

}
