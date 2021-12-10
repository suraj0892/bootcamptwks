package com.tw.bootcamp.bookshop.user.address;

import com.tw.bootcamp.bookshop.user.User;
import com.tw.bootcamp.bookshop.user.UserRepository;
import com.tw.bootcamp.bookshop.user.UserTestBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.validation.ConstraintViolationException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class AddressServiceTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private AddressService addressService;

    @AfterEach
    void tearDown() {
        addressRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldCreateAddressWhenValid() {
        User user = userRepository.save(new UserTestBuilder().build());
        CreateAddressRequest createRequest = createAddress();

        Address address = addressService.create(createRequest, user.getEmail());

        assertNotNull(address);
        assertEquals("4 Privet Drive", address.getLineNoOne());
        assertEquals(user.getId(), address.getUser()
                .getId());
    }

    @Test
    void shouldNotCreateAddressWhenInValid() {
        User user = userRepository.save(new UserTestBuilder().build());
        CreateAddressRequest createRequest = invalidAddress();

        assertThrows(ConstraintViolationException.class,
                () -> addressService.create(createRequest, user.getEmail()));
    }

    @Test
    void shouldNotCreateAddressWhenUserIsNotValid() {
        CreateAddressRequest createRequest = createAddress();
        assertThrows(UsernameNotFoundException.class, () -> addressService.create(createRequest, null));
    }

    @Test
    void shouldUnMarkHomeAddressAsDefaultIfOfficeAddressIsDefault() {
        User user = userRepository.save(new UserTestBuilder().build());
        Address homeAddress = createHomeAddress(user.getEmail());
        Address officeAddress = createOfficeAddress(user.getEmail(), true);

        List<Address> userAddress = addressRepository.findAll();
        assertTrue(userAddress.stream()
                .filter(officeAddress::equals)
                .anyMatch(Address::isDefault), "Office address should be default");

        assertTrue(userAddress.stream()
                .filter(homeAddress::equals)
                .anyMatch(address -> !address.isDefault()), "Home address should not be default");
    }

    @Test
    void shouldNotUnMarkHomeAddressAsDefaultIfOfficeAddressIsNotDefault() {
        User user = userRepository.save(new UserTestBuilder().build());
        Address homeAddress = createHomeAddress(user.getEmail());
        Address officeAddress = createOfficeAddress(user.getEmail(), false);

        List<Address> userAddress = addressRepository.findAll();
        assertTrue(userAddress.stream()
                .filter(officeAddress::equals)
                .anyMatch(address -> !address.isDefault()), "Office address should not be default");

        assertTrue(userAddress.stream()
                .filter(homeAddress::equals)
                .anyMatch(Address::isDefault), "Home address should be default");
    }

    @Test
    void shouldReturnAddressListForUserWhenPresent() {
        User user = userRepository.save(new UserTestBuilder().build());
        Address homeAddress = createHomeAddress(user.getEmail());
        Address officeAddress = createOfficeAddress(user.getEmail(), false);

        List<Address> userAddressList = addressService.get(user.getEmail());
        assertIterableEquals(Arrays.asList(homeAddress, officeAddress), userAddressList);
    }

    @Test
    void shouldThrowErrorWhenUserNotPresent() {
        UsernameNotFoundException usernameNotFoundException = assertThrows(UsernameNotFoundException.class,
                () -> addressService.get(any(String.class)));
        assertEquals("User not found", usernameNotFoundException.getMessage());
    }

    private Address createOfficeAddress(String email, boolean isDefault) {
        CreateAddressRequest createOfficeRequest = createOfficeAddress(isDefault);
        return addressService.create(createOfficeRequest, email);
    }

    private Address createHomeAddress(String email) {
        CreateAddressRequest createHomeRequest = createAddress(true);
        return addressService.create(createHomeRequest, email);
    }

    private CreateAddressRequest invalidAddress() {
        return CreateAddressRequest.builder()
                .lineNoOne("4 Privet Drive")
                .lineNoTwo("Little Whinging")
                .city(null)
                .pinCode("A22 001")
                .country("Surrey")
                .build();
    }

    private CreateAddressRequest createAddress() {
        return createAddress(true);
    }

    private CreateAddressRequest createAddress(boolean isDefault) {
        return CreateAddressRequest.builder()
                .lineNoOne("4 Privet Drive")
                .lineNoTwo("Little Whinging")
                .city("Godstone")
                .pinCode("A22 001")
                .country("Surrey")
                .isDefault(isDefault)
                .build();
    }

    private CreateAddressRequest createOfficeAddress(boolean isDefault) {
        return CreateAddressRequest.builder()
                .lineNoOne("Earth office")
                .lineNoTwo("Zenith Building")
                .city("Chennai")
                .pinCode("B22 001")
                .country("India")
                .isDefault(isDefault)
                .build();
    }
}