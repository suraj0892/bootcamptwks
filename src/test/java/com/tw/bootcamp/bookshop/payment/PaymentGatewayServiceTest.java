package com.tw.bootcamp.bookshop.payment;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentGatewayServiceTest {

    @InjectMocks
    private PaymentGatewayService paymentGatewayService = new PaymentGatewayService();

    @Mock
    RestTemplate paymentGateway;

    @Test
    void ShouldReturnPaymentAcceptedWhenCardDetailsAreValid() {
        CardDetails cardDetails = new CardDetails.CardDetailsBuilder().cardSecurityCode(123).creditCardExpiration("12/2021")
                .creditCardNumber("1234-1234-1234-1234").amount(100.12).build();

        when(paymentGateway.exchange(
                "https://tw-mock-credit-service.herokuapp.com/payments",
                HttpMethod.POST,
                new HttpEntity(cardDetails), ResponseEntity.class))
                .thenReturn(new ResponseEntity(HttpStatus.ACCEPTED));

        ResponseEntity response = paymentGatewayService.payWithCreditCard(cardDetails);

        assertEquals(202, response.getStatusCodeValue());
    }

    @Test
    void ShouldReturnPaymentFailedWhenCardDetailsAreInValid() {
        CardDetails cardDetails = new CardDetails.CardDetailsBuilder().cardSecurityCode(123).creditCardExpiration("12/2021")
                .creditCardNumber("1234-1234-1234-1234").amount(100.12).build();

        when(paymentGateway.exchange(
                "https://tw-mock-credit-service.herokuapp.com/payments",
                HttpMethod.POST,
                new HttpEntity(cardDetails), ResponseEntity.class)).thenReturn(new ResponseEntity(HttpStatus.BAD_REQUEST));

        ResponseEntity response = paymentGatewayService.payWithCreditCard(cardDetails);

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void ShouldReturnPaymentFailedWithErrorMessageWhenCardDetailsAreInValid() {
        CardDetails cardDetails = new CardDetails.CardDetailsBuilder().cardSecurityCode(123).creditCardExpiration("12/2021")
                .creditCardNumber("1234-1234-1234-1234").amount(100.12).build();

        String[] details = {"Card Security Code is not Valid"};

        when(paymentGateway.exchange(
                "https://tw-mock-credit-service.herokuapp.com/payments",
                HttpMethod.POST,
                new HttpEntity(cardDetails), ResponseEntity.class))
                .thenReturn(new ResponseEntity(new ErrorResponse("Validation Failed", details), HttpStatus.BAD_REQUEST));

        ResponseEntity response = paymentGatewayService.payWithCreditCard(cardDetails);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Validation Failed", ((ErrorResponse) response.getBody()).getMessage());
    }
}