package com.tw.bootcamp.bookshop.order;

import com.tw.bootcamp.bookshop.book.Book;
import com.tw.bootcamp.bookshop.money.Money;
import com.tw.bootcamp.bookshop.user.address.Address;
import lombok.Builder;
import lombok.Getter;


import javax.persistence.*;

@Builder
@Getter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Long userId;

    @OneToOne
    @JoinColumn(name="book_id")
    private Book book;

    @OneToOne
    @JoinColumn(name="address_id")
    private Address address;

    private int count;

    @Embedded
    private Money totalAmount;

    private OrderStatus status;
}
