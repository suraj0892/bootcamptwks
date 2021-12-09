package com.tw.bootcamp.bookshop.user.documenation;

import com.tw.bootcamp.bookshop.error.ErrorResponse;
import com.tw.bootcamp.bookshop.user.UserView;
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
@Operation(summary = "Create user", description = "Create user for book shop", tags = { "User Service" })
@ApiResponse(responseCode = "201", description = "User successfully created",
        content = @Content(schema = @Schema(implementation = UserView.class)))
@ApiResponse(responseCode = "400", description = "Bad request due to either of below scenarios.\n" +
        "1. User with same email already exists\n" +
        "2. Email/Password is empty\n" +
        "3. Email should follow the pattern abc@xyz.efg",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
public @interface UserCreationDocumentation {
}
