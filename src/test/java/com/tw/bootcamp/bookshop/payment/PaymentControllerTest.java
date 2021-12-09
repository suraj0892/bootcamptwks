package com.tw.bootcamp.bookshop.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tw.bootcamp.bookshop.user.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
@WithMockUser
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PaymentService paymentService;

    @MockBean
    UserService userService;

    @Test
    void OrderShouldBePlacedWhenPaymentIsSuccess() throws PaymentFailedException, Exception {
        PaymentRequest paymentRequest = new PaymentRequest(123, new CardDetails());

        when(paymentService.pay(eq(paymentRequest))).thenReturn("Order Success");

        mockMvc.perform(post("/pay")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(status().isAccepted());

    }

    @Test
    void OrderWillBeCancelledWhenPaymentHasFailed() throws PaymentFailedException, Exception {

        CardDetails cardDetails = new CardDetails.CardDetailsBuilder().cardSecurityCode(123).creditCardExpiration("12/2021")
                .creditCardNumber("1234-1234-1234-1234").amount(100.12).build();
        PaymentRequest paymentRequest = new PaymentRequest(123, cardDetails);


        when(paymentService.pay(paymentRequest)).thenThrow(PaymentFailedException.class);

        mockMvc.perform(post("/pay")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(status().isBadRequest());


    }


}