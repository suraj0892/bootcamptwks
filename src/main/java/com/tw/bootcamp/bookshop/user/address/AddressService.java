package com.tw.bootcamp.bookshop.user.address;

import com.tw.bootcamp.bookshop.user.User;
import com.tw.bootcamp.bookshop.user.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.apache.commons.collections.CollectionUtils.isNotEmpty;
import java.util.Optional;

@Service
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public Address create(@Valid CreateAddressRequest createRequest, String email) {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        unMarkExistingDefaultAddress(createRequest, user);
        Address address = Address.create(createRequest, user);
        return addressRepository.save(address);
    }

    private void unMarkExistingDefaultAddress(CreateAddressRequest createRequest, User user) {
        if (createRequest.isDefault() && isNotEmpty(user.getAddresses())) {
            List<Address> existingDefaultAddress = user.getAddresses().stream()
                    .filter(Address::isDefault)
                    .map(address -> {
                        address.setDefault(false);
                        return address;
                    }).collect(Collectors.toList());
            addressRepository.saveAll(existingDefaultAddress);

        }
    }
    public Optional<Address> getById(long addressId) {
        return addressRepository.findById(addressId);
    }

    public List<Address> get(String email) {
        return new ArrayList<>();
    }
}
