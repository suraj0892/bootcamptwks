package com.tw.bootcamp.bookshop.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tw.bootcamp.bookshop.book.Book;
import com.tw.bootcamp.bookshop.book.BookService;
import com.tw.bootcamp.bookshop.book.BookTestBuilder;
import com.tw.bootcamp.bookshop.money.Money;
import com.tw.bootcamp.bookshop.user.UserService;
import com.tw.bootcamp.bookshop.user.address.Address;
import com.tw.bootcamp.bookshop.user.address.AddressService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@WithMockUser
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    OrderService orderService;

    @MockBean
    UserService userService;

    @MockBean
    BookService bookService;

    @MockBean
    AddressService addressService;

    @Autowired
    ObjectMapper objectMapper;

    final static String email = "test@testemail.com";

    @Test
    void shouldCreateOrderWhenOrderDetailsAreValid() throws Exception, InvalidOrderRequestException {
        Book book = new BookTestBuilder().withId(1).withName("Harry Potter").withAuthor("J.K").withPrice(100).build();
        Address address = createAddress();
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(book.getId(), address.getId(), 2, email);
        Order order = new Order.OrderBuilder()
                .id(1)
                .book(book)
                .count(2)
                .address(address)
                .totalAmount(new Money("INR", 200))
                .build();

        when(orderService.create(any())).thenReturn(order);

        mockMvc.perform(post("/order")
                        .content(objectMapper.writeValueAsString(createOrderRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string(objectMapper.writeValueAsString(order)));
        verify(orderService, times(1)).create(any());
    }

    @Test
    void shouldThrowExceptionWhenOrderDetailsAreInvalid() throws Exception, InvalidOrderRequestException {
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(2L, 5L, 2, email);

        when(orderService.create(any())).thenThrow(InvalidOrderRequestException.class);

        mockMvc.perform(post("/order")
                        .content(objectMapper.writeValueAsString(createOrderRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(orderService, times(1)).create(any());
    }

    private Address createAddress() {
        return Address.builder()
                .id(1L)
                .lineNoOne("4 Privet Drive")
                .lineNoTwo("Little Whinging")
                .city("God Stone")
                .pinCode("A22 001")
                .country("Surrey")
                .build();
    }
}