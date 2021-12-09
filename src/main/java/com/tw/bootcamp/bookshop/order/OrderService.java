package com.tw.bootcamp.bookshop.order;

import com.tw.bootcamp.bookshop.book.Book;
import com.tw.bootcamp.bookshop.book.BookService;
import com.tw.bootcamp.bookshop.money.Money;
import com.tw.bootcamp.bookshop.user.address.Address;
import com.tw.bootcamp.bookshop.user.address.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class OrderService {

    @Autowired
    BookService bookService;

    @Autowired
    AddressService addressService;

    @Autowired
    OrderRepository orderRepository;

    public Order create(CreateOrderRequest createOrderRequest) throws InvalidOrderRequestException {
        Optional<Book> book = bookService.getById(createOrderRequest.getBookId());
        if (!book.isPresent()) {
            throw new InvalidOrderRequestException("Book not found");
        }
        Optional<Address> address = addressService.getById(createOrderRequest.getAddressId());
        if (!address.isPresent()) {
            throw new InvalidOrderRequestException("Address not found");
        }
        if (createOrderRequest.getCount() <= 0) {
            throw new InvalidOrderRequestException("Invalid order count");
        }

        Order order = Order.builder()
                .book(book.get())
                .address(address.get())
                .count(createOrderRequest.getCount())
                .build();

        return orderRepository.save(order);
    }
}