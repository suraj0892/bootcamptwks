package com.tw.bootcamp.bookshop.user.address;

import com.tw.bootcamp.bookshop.documentation.CreateAddressDocumentation;
import com.tw.bootcamp.bookshop.documentation.GetAddressDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/addresses")
@Tag(name = "User Service", description = "APIs for user service")
public class AddressController {
    @Autowired
    private AddressService addressService;

    @CreateAddressDocumentation
    @PostMapping
    public ResponseEntity<Address> create(@RequestBody CreateAddressRequest createRequest, Principal principal) {
        Address address = addressService.create(createRequest, principal.getName());
        return new ResponseEntity<>(address, HttpStatus.CREATED);
    }

    @GetAddressDocumentation
    @GetMapping
    public ResponseEntity<List<Address>> get(Principal principal) throws NoAddressFoundException {
        return new ResponseEntity<>(addressService.get(principal.getName()), HttpStatus.OK);
    }
}
