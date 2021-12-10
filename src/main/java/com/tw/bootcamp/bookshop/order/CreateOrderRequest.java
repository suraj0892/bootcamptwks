package com.tw.bootcamp.bookshop.order;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
@AllArgsConstructor
public class CreateOrderRequest {
    private Long bookId;
    private Long addressId;
    private int count;
}
