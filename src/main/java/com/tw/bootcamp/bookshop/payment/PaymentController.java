package com.tw.bootcamp.bookshop.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tw.bootcamp.bookshop.book.Book;
import com.tw.bootcamp.bookshop.error.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {
    @Autowired
    PaymentService paymentService;

    @Operation(summary = "Payment Service", description = "Payment to be done for placed Orders", tags = {"Payment Service"})
    @ApiResponse(responseCode = "202", description = "Payment Successful",
            content = @Content(schema = @Schema(implementation = String.class)
            ))
    @ApiResponse(responseCode = "400", description = "Payment Failure",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorised",
            content = @Content(schema = @Schema()))
    @PostMapping("/pay")
    public ResponseEntity pay(@RequestBody PaymentRequest paymentRequest) throws PaymentFailedException, JsonProcessingException {
        String message = paymentService.pay(paymentRequest);
        return new ResponseEntity(message, HttpStatus.ACCEPTED);
    }
}
