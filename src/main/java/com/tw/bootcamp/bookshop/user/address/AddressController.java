package com.tw.bootcamp.bookshop.user.address;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/addresses")
@Tag(name = "Address Service", description = "APIs for address service")
public class AddressController {
    @Autowired
    private AddressService addressService;

    @Operation(summary = "Create address", description = "Create address for a user", tags = {"Address Service"})
    @PostMapping
    public ResponseEntity<Address> create(@RequestBody CreateAddressRequest createRequest, Principal principal) {
        Address address = addressService.create(createRequest, principal.getName());
        return new ResponseEntity<>(address, HttpStatus.CREATED);
    }
}
