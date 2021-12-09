package com.tw.bootcamp.bookshop.user.address;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tw.bootcamp.bookshop.user.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Getter
@AllArgsConstructor
@Builder
@Entity
@Table(name = "addresses")
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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

    @Setter
    private boolean isDefault;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Address(String lineNoOne, String lineNoTwo, String city, String state, String pinCode, String country,
                   boolean isDefault,User user) {
        this.lineNoOne = lineNoOne;
        this.lineNoTwo = lineNoTwo;
        this.city = city;
        this.state = state;
        this.pinCode = pinCode;
        this.country = country;
        this.user = user;
        this.isDefault = isDefault;
    }

    public static Address create(CreateAddressRequest createRequest, User user) {
        return new Address(createRequest.getLineNoOne(),
                createRequest.getLineNoTwo(),
                createRequest.getCity(),
                createRequest.getState(),
                createRequest.getPinCode(),
                createRequest.getCountry(),
                createRequest.isDefault(),
                user);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return  Objects.equals(id, address.id) && Objects.equals(lineNoOne, address.lineNoOne) && Objects.equals(lineNoTwo, address.lineNoTwo) && Objects.equals(city, address.city) && Objects.equals(state, address.state) && Objects.equals(pinCode, address.pinCode) && Objects.equals(country, address.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lineNoOne, lineNoTwo, city, state, pinCode, country);
    }
}
