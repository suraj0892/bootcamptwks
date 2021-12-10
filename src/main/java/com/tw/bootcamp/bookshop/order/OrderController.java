package com.tw.bootcamp.bookshop.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/order")
@Tag(name = "Order Service", description = "APIs for order service")
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping
    @OrderBookDocumentation
    ResponseEntity<Order> create(@RequestBody CreateOrderRequest createOrderRequest, Principal principal) throws InvalidOrderRequestException {
//        createOrderRequest.setUserEmail(principal.getName());
        Order order = orderService.create(createOrderRequest, principal.getName());

        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }
}
