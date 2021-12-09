package com.tw.bootcamp.bookshop.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PaymentService {


    @Autowired
    PaymentGatewayService paymentGatewayService;

    public String pay(PaymentRequest paymentRequest) {

        ResponseEntity resFromPaymentGateway  = paymentGatewayService.payWithCreditCard(paymentRequest.getCardDetails());

        if(HttpStatus.ACCEPTED.equals(resFromPaymentGateway.getStatusCode())){

            return "Order Placed Successfully!!!";
        } else{

            return "Order Cancelled due To Payment Failure..Please contact Customer Support.";
        }

    }


}
