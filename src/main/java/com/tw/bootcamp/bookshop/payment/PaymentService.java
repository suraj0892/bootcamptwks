package com.tw.bootcamp.bookshop.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tw.bootcamp.bookshop.order.OrderService;
import com.tw.bootcamp.bookshop.order.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class PaymentService {

    @Autowired
    OrderService orderService;

    @Autowired
    PaymentGatewayService paymentGatewayService;

    public String pay(PaymentRequest paymentRequest) throws PaymentFailedException, JsonProcessingException {
        ErrorResponse error = null;
        try{
            ResponseEntity resFromPaymentGateway = paymentGatewayService.payWithCreditCard(paymentRequest.getCardDetails());
            if (HttpStatus.ACCEPTED.equals(resFromPaymentGateway.getStatusCode())) {
                orderService.updateOrderStatus(paymentRequest.getOrderId(), OrderStatus.PAYMENT_COMPLETE);
                return "Order Placed Successfully!!!";
            }
        }
        catch(HttpClientErrorException ex){
            orderService.updateOrderStatus(paymentRequest.getOrderId(), OrderStatus.PAYMENT_FAILED);
            error =  new ObjectMapper().readValue(ex.getResponseBodyAsString(), ErrorResponse.class);
        }
        throw new PaymentFailedException(error);
    }

}
