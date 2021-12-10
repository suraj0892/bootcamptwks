package com.tw.bootcamp.bookshop.order;

import com.tw.bootcamp.bookshop.book.Book;
import com.tw.bootcamp.bookshop.book.BookService;
import com.tw.bootcamp.bookshop.book.BookTestBuilder;
import com.tw.bootcamp.bookshop.money.Money;
import com.tw.bootcamp.bookshop.user.User;
import com.tw.bootcamp.bookshop.user.UserService;
import com.tw.bootcamp.bookshop.user.address.Address;
import com.tw.bootcamp.bookshop.user.address.AddressService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(1L, 1L, 2);
        User user = User.builder().id(1L).build();

        when(userService.findByEmail(email)).thenReturn(Optional.of(user));
        when(bookService.getById(1)).thenReturn(Optional.empty());
        when(orderRepository.save(any())).thenReturn(any());

        assertThrows(InvalidOrderRequestException.class, () -> orderService.create(createOrderRequest, email));
    }

    @Test
    void shouldThrowExceptionWhenAddressNotFound() {
        Book book = new BookTestBuilder().withId(1L).withName("Harry Potter").withAuthor("J.K.").build();
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(1L, 1L, 2);
        User user = User.builder().id(1L).build();

        when(userService.findByEmail(email)).thenReturn(Optional.of(user));
        when(bookService.getById(1)).thenReturn(Optional.of(book));
        when(addressService.getById(1)).thenReturn(Optional.empty());
        when(orderRepository.save(any())).thenReturn(any());

        assertThrows(InvalidOrderRequestException.class, () -> orderService.create(createOrderRequest, email));
    }

    @Test
    void shouldThrowExceptionWhenBookCountIsInvalid() {
        Book book = new BookTestBuilder().withId(1L).withName("Harry Potter").withAuthor("J.K.").build();
        Address address = Address.builder().id(1L).build();
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(1L, 1L, 0);
        User user = User.builder().id(1L).build();

        when(userService.findByEmail(email)).thenReturn(Optional.of(user));
        when(bookService.getById(1)).thenReturn(Optional.of(book));
        when(addressService.getById(1)).thenReturn(Optional.of(address));
        when(orderRepository.save(any())).thenReturn(any());

        assertThrows(InvalidOrderRequestException.class, () -> orderService.create(createOrderRequest, email));
    }

    @Nested
    public class PaymentProcessing {
        @Test
        void shouldUpdateOrderStatusToCompleteWhenPaymentIsSuccessful() {
            Optional<Order> order = Optional.ofNullable(new Order.OrderBuilder().id(1).status(OrderStatus.INITIATED).build());
            when(orderRepository.findById(1L)).thenReturn(order);

            Order orderSuccess = new Order.OrderBuilder().id(1).status(OrderStatus.PAYMENT_COMPLETE).build();
            when(orderRepository.save(order.get())).thenReturn(orderSuccess);

            orderService.updateOrderStatus(1, OrderStatus.PAYMENT_COMPLETE);

            assertEquals(order.get().getStatus(), orderSuccess.getStatus());

        }

        @Test
        void shouldUpdateOrderStatusToFailedWhenPaymentIsFailed() {
            Optional<Order> order = Optional.ofNullable(new Order.OrderBuilder().id(1).status(OrderStatus.INITIATED).build());
            when(orderRepository.findById(1L)).thenReturn(order);

            Order orderSuccess = new Order.OrderBuilder().id(1).status(OrderStatus.PAYMENT_FAILED).build();
            when(orderRepository.save(order.get())).thenReturn(orderSuccess);

            orderService.updateOrderStatus(1, OrderStatus.PAYMENT_FAILED);

            assertEquals(order.get().getStatus(), orderSuccess.getStatus());

        }
    }
}