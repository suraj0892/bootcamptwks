package com.tw.bootcamp.bookshop.payment;

import com.tw.bootcamp.bookshop.order.OrderService;
import com.tw.bootcamp.bookshop.order.OrderStatus;
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
    OrderService orderService;

    @Autowired
    PaymentGatewayService paymentGatewayService;

    public String pay(PaymentRequest paymentRequest) throws PaymentFailedException {

        ResponseEntity paymentStatus = paymentGatewayService.payWithCreditCard(paymentRequest.getCardDetails());

        if (HttpStatus.ACCEPTED.equals(paymentStatus.getStatusCode())) {
            orderService.updateOrderStatus(paymentRequest.getOrderId(), OrderStatus.PAYMENT_COMPLETE);
            return "Order Placed Successfully!!!";
        } else {
            orderService.updateOrderStatus(paymentRequest.getOrderId(), OrderStatus.PAYMENT_FAILED);
            throw new PaymentFailedException("Payment Failed , please contact Customer Care.");
        }
    }
}
