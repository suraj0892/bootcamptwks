package com.tw.bootcamp.bookshop.order;

import com.tw.bootcamp.bookshop.book.Book;
import com.tw.bootcamp.bookshop.book.BookService;
import com.tw.bootcamp.bookshop.book.BookTestBuilder;
import com.tw.bootcamp.bookshop.user.Role;
import com.tw.bootcamp.bookshop.user.User;
import com.tw.bootcamp.bookshop.user.UserService;
import com.tw.bootcamp.bookshop.user.address.Address;
import com.tw.bootcamp.bookshop.user.address.AddressService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    OrderService orderService;

    @MockBean
    BookService bookService;

    @MockBean
    AddressService addressService;

    @MockBean
    OrderRepository orderRepository;

    @MockBean
    UserService userService;

    final String email = "test@testemail.com";

    @Test
    void shouldThrowExceptionWhenBookNotFound() {
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(1L, 1L, 2, email);
        User user = User.builder().id(1L).build();

        when(userService.findByEmail(email)).thenReturn(Optional.of(user));
        when(bookService.getById(1)).thenReturn(Optional.empty());
        when(orderRepository.save(any())).thenReturn(any());

        assertThrows(InvalidOrderRequestException.class, () -> orderService.create(createOrderRequest));
    }

    @Test
    void shouldThrowExceptionWhenAddressNotFound() {
        Book book = new BookTestBuilder().withId(1L).withName("Harry Potter").withAuthor("J.K.").build();
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(1L, 1L, 2, email);
        User user = User.builder().id(1L).build();

        when(userService.findByEmail(email)).thenReturn(Optional.of(user));
        when(bookService.getById(1)).thenReturn(Optional.of(book));
        when(addressService.getById(1)).thenReturn(Optional.empty());
        when(orderRepository.save(any())).thenReturn(any());

        assertThrows(InvalidOrderRequestException.class, () -> orderService.create(createOrderRequest));
    }

    @Test
    void shouldThrowExceptionWhenBookCountIsInvalid() {
        Book book = new BookTestBuilder().withId(1L).withName("Harry Potter").withAuthor("J.K.").build();
        Address address = Address.builder().id(1L).build();
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(1L, 1L, 0, email);
        User user = User.builder().id(1L).build();

        when(userService.findByEmail(email)).thenReturn(Optional.of(user));
        when(bookService.getById(1)).thenReturn(Optional.of(book));
        when(addressService.getById(1)).thenReturn(Optional.of(address));
        when(orderRepository.save(any())).thenReturn(any());

        assertThrows(InvalidOrderRequestException.class, () -> orderService.create(createOrderRequest));
    }
}