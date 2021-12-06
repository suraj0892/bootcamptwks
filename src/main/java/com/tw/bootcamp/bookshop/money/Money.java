package com.tw.bootcamp.bookshop.money;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@Embeddable
@NoArgsConstructor
public class Money {
    private String currency;

    @Column(columnDefinition = "NUMERIC")
    @NotNull
    @NotEmpty
    private double amount;

    public static Money rupees(double amount) {
        return new Money("INR", amount);
    }
}
