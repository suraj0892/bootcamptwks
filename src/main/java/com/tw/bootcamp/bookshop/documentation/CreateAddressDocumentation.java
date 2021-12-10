package com.tw.bootcamp.bookshop.documentation;

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
@Operation(summary = "Create address", description = "Create address for user", tags = {"User Service"})
@ApiResponse(responseCode = "200", description = "Address created successfully",
        content = @Content(schema = @Schema(implementation = Address.class)))
@ApiResponse(responseCode = "401", description = "Unauthorised",
        content = @Content(schema = @Schema()))
public @interface CreateAddressDocumentation {
}
