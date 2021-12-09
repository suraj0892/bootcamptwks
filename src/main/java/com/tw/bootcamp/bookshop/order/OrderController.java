package com.tw.bootcamp.bookshop.order;

import com.tw.bootcamp.bookshop.book.BookService;
import com.tw.bootcamp.bookshop.user.User;
import com.tw.bootcamp.bookshop.user.UserService;
import com.tw.bootcamp.bookshop.user.address.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping
    ResponseEntity<Order> create(@RequestBody CreateOrderRequest createOrderRequest, Principal principal) throws InvalidOrderRequestException {
        createOrderRequest.setUserEmail(principal.getName());
        Order order = orderService.create(createOrderRequest);

        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }
}
