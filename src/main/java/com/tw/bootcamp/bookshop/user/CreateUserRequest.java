package com.tw.bootcamp.bookshop.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
public class CreateUserRequest {
    @Schema(description = "Email address of the user and should be unique",
            example = "abc@xyz.efg", required = true)
    private final String email;
    @Schema(description = "Password for login",
            example = "<Some strong password>", required = true)
    private final String password;
}
