package com.tw.bootcamp.bookshop.user.address;

import com.tw.bootcamp.bookshop.user.User;
import com.tw.bootcamp.bookshop.user.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

@Service
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public Address create(@Valid CreateAddressRequest createRequest, String email) {
        User user = findUser(email);
        unMarkExistingDefaultAddress(createRequest, user);
        Address address = Address.create(createRequest, user);
        return addressRepository.save(address);
    }

    private User findUser(String email) {
        return userService.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    private void unMarkExistingDefaultAddress(CreateAddressRequest createRequest, User user) {
        if (createRequest.isDefault() && isNotEmpty(user.getAddresses())) {
            List<Address> existingDefaultAddress = getExistingDefaultAddress(user);
            existingDefaultAddress
                    .forEach(address -> address.setDefault(false));
            addressRepository.saveAll(existingDefaultAddress);
        }
    }

    private List<Address> getExistingDefaultAddress(User user) {
        return user.getAddresses()
                .stream()
                .filter(Address::isDefault)
                .collect(Collectors.toList());
    }

    public Optional<Address> getById(long addressId) {
        return addressRepository.findById(addressId);
    }

    public List<Address> get(String email) throws NoAddressFoundException {
        List<Address> addresses = findUser(email).getAddresses();
        if(CollectionUtils.isEmpty(addresses)) {
            throw new NoAddressFoundException("No address found for user");
        }
        return addresses;
    }
}
