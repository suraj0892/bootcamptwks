package com.tw.bootcamp.bookshop.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PaymentService {

    @Autowired
    RestTemplate paymentGateway;


    public ResponseEntity pay(PaymentRequest paymentRequest) {

        ResponseEntity resFromPaymentGateway  = payWithCreditCard(paymentRequest.getCardDetails());

        if(HttpStatus.ACCEPTED.equals(resFromPaymentGateway.getStatusCode())){

            return new ResponseEntity("Order Placed Successfully!!!" , HttpStatus.ACCEPTED);
        } else{

            return resFromPaymentGateway;
        }

    }

    private ResponseEntity payWithCreditCard(CardDetails cardDetails){
        return paymentGateway.exchange(
                "https://tw-mock-credit-service.herokuapp.com/payments",
                HttpMethod.POST,
                new HttpEntity(cardDetails),
                ResponseEntity.class);
    }
}
