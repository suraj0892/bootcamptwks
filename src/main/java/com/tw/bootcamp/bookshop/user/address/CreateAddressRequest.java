package com.tw.bootcamp.bookshop.user.address;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class CreateAddressRequest {
    @NotBlank
    private String lineNoOne;
    private String lineNoTwo;
    @NotBlank
    private String city;
    private String state;
    @NotBlank
    private String pinCode;
    @NotBlank
    private String country;

    @JsonProperty("default")
    private boolean isDefault = false;
}
