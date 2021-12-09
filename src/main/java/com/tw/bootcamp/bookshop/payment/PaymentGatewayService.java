package com.tw.bootcamp.bookshop.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PaymentGatewayService {

    @Autowired
    RestTemplate paymentGateway;

    @Value("${payment.gateway}")
    private String paymentGatewayUrl;

    public ResponseEntity payWithCreditCard(CardDetails cardDetails){
        return paymentGateway.exchange(
                "https://tw-mock-credit-service.herokuapp.com/payments",
                HttpMethod.POST,
                new HttpEntity(cardDetails),
                ResponseEntity.class);
    }
}
