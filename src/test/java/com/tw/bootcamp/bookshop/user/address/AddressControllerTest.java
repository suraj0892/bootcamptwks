package com.tw.bootcamp.bookshop.user.address;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tw.bootcamp.bookshop.user.User;
import com.tw.bootcamp.bookshop.user.UserService;
import com.tw.bootcamp.bookshop.user.UserTestBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import javax.validation.ConstraintViolationException;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AddressController.class)
@WithMockUser
class AddressControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AddressService addressService;

    @MockBean
    UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateAddressWhenValid() throws Exception {
        CreateAddressRequest createRequest = createAddress();
        Address address = new AddressTestBuilder().build();
        when(addressService.create(eq(createRequest), any(String.class))).thenReturn(address);

        mockMvc.perform(post("/addresses")
                .content(objectMapper.writeValueAsString(createRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string(objectMapper.writeValueAsString(address)));

        verify(addressService, times(1)).create(eq(createRequest), any(String.class));
    }

    @Test
    void shouldNotCreateAddressWhenInValid() throws Exception {
        CreateAddressRequest createRequest = CreateAddressRequest.builder().city(null).build();
        when(addressService.create(any(), any())).thenThrow(new ConstraintViolationException(new HashSet<>()));

        mockMvc.perform(post("/addresses")
                .content(objectMapper.writeValueAsString(createRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"));

        verify(addressService, times(1)).create(any(), any());
    }

    @Test
    void shouldReturnUserAddressWhenPresent() throws Exception {
        List<Address> address = Collections.singletonList(new AddressTestBuilder().build());
        when(addressService.get(any(String.class))).thenReturn(address);

        mockMvc.perform(get("/addresses"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(address)));

        verify(addressService, times(1)).get(any(String.class));
    }

    @Test
    void shouldReturnEmptyWhenAddressNotPresent() throws Exception {
        List<Address> address = new ArrayList<>();
        when(addressService.get(any(String.class))).thenReturn(address);

        mockMvc.perform(get("/addresses"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(address)));

        verify(addressService, times(1)).get(any(String.class));
    }

    private CreateAddressRequest createAddress() {
        return CreateAddressRequest.builder()
                .lineNoOne("4 Privet Drive")
                .lineNoTwo("Little Whinging")
                .city("Godstone")
                .pinCode("A22 001")
                .country("Surrey")
                .build();
    }
}