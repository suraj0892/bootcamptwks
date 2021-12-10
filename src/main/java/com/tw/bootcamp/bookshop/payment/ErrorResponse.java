package com.tw.bootcamp.bookshop.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@Getter
public class ErrorResponse {
    private String message;
    private String[] details;
}
