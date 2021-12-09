package com.tw.bootcamp.bookshop.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateOrderRequest {
    private Long bookId;
    private Long addressId;
    private int count;
}
