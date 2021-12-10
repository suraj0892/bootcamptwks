package com.tw.bootcamp.bookshop.documentation;

import com.tw.bootcamp.bookshop.error.ErrorResponse;
import com.tw.bootcamp.bookshop.user.address.Address;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Operation(summary = "Order Book", description = "Place an order of Book", tags = {"Order Service"})
@ApiResponse(responseCode = "201", description = "Order Created",
        content = @Content(schema = @Schema(implementation = Address.class)))
@ApiResponse(responseCode = "401", description = "Unauthorised",
        content = @Content(schema = @Schema()))
@ApiResponse(responseCode = "404", description = "Order Request is Invalid",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
public @interface OrderBookDocumentation {
}
