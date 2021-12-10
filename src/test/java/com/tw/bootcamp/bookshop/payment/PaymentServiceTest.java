package com.tw.bootcamp.bookshop.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tw.bootcamp.bookshop.order.OrderService;
import com.tw.bootcamp.bookshop.order.OrderStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    OrderService orderService;

    @Mock
    PaymentGatewayService paymentGatewayService;

    @InjectMocks
    PaymentService paymentService = new PaymentService();

    @Test
    void ShouldReturnOrderSuccessAndUpdatePaymentSuccess() throws PaymentFailedException, JsonProcessingException {
        CardDetails cardDetails = new CardDetails.CardDetailsBuilder().cardSecurityCode(123).creditCardExpiration("12/2021")
                .creditCardNumber("1234-1234-1234-1234").amount(100.12).build();
        PaymentRequest paymentRequest = new PaymentRequest(1, cardDetails);


        when(paymentGatewayService.payWithCreditCard(cardDetails)).thenReturn(new ResponseEntity(HttpStatus.ACCEPTED));
        doNothing().when(orderService).updateOrderStatus(paymentRequest.getOrderId(), OrderStatus.PAYMENT_COMPLETE);

        String message = paymentService.pay(paymentRequest);

        assertEquals("Order Placed Successfully!!!", message);

        verify(orderService).updateOrderStatus(paymentRequest.getOrderId(), OrderStatus.PAYMENT_COMPLETE);
    }

    @Test
    void ShouldReturnEmptyWhenNotAcceptedButStatusIsOk() throws PaymentFailedException, JsonProcessingException {
        CardDetails cardDetails = new CardDetails.CardDetailsBuilder().cardSecurityCode(123).creditCardExpiration("12/2021")
                .creditCardNumber("1234-1234-1234-1234").amount(100.12).build();
        PaymentRequest paymentRequest = new PaymentRequest(1, cardDetails);

        when(paymentGatewayService.payWithCreditCard(cardDetails)).thenReturn(new ResponseEntity(HttpStatus.OK));

        String message = paymentService.pay(paymentRequest);

        assertEquals("", message);
    }

    @Test
    void ShouldUpdatePaymentFailedWhenPaymentHasFailed() throws PaymentFailedException, JsonProcessingException {
        CardDetails cardDetails = new CardDetails.CardDetailsBuilder().cardSecurityCode(123).creditCardExpiration("12/2021")
                .creditCardNumber("1234-1234-1234-1234").amount(100.12).build();
        PaymentRequest paymentRequest = new PaymentRequest(1, cardDetails);
        String[] details = {"Card Security Code is not Valid"};
        ErrorResponse errorResponse = new ErrorResponse("Validation Failed", details);

        HttpClientErrorException httpClientErrorException = new HttpClientErrorException("Payment Failed", HttpStatus.BAD_REQUEST, "Payment Failed",
                null, errorResponse.toString().getBytes(), null);

        when(paymentGatewayService.payWithCreditCard(cardDetails)).thenThrow(httpClientErrorException);
        doNothing().when(orderService).updateOrderStatus(paymentRequest.getOrderId(), OrderStatus.PAYMENT_FAILED);

        assertThrows(Exception.class , () -> paymentService.pay(paymentRequest));
        verify(orderService).updateOrderStatus(paymentRequest.getOrderId(), OrderStatus.PAYMENT_FAILED);
    }

}

