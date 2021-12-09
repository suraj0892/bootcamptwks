package com.tw.bootcamp.bookshop.payment;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
public class PaymentRequest {

    private Integer orderId;
    private CardDetails cardDetails;

}
