package com.tw.bootcamp.bookshop.documentation;

import com.tw.bootcamp.bookshop.error.ErrorResponse;
import com.tw.bootcamp.bookshop.user.address.Address;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Operation(summary = "List address", description = "List address for user", tags = {"User Service"})
@ApiResponse(responseCode = "200", description = "Address retrieved for user successfully",
        content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = Address.class))
        ))
@ApiResponse(responseCode = "401", description = "Unauthorised",
        content = @Content(schema = @Schema()))
@ApiResponse(responseCode = "204", description = "Address Not Found",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
@ApiResponse(responseCode = "404", description = "User Not Found",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
public @interface GetAddressDocumentation {
}
