package com.fsk.ecommerce.mapper.dto;

import com.fsk.ecommerce.entity.AddressType;
import lombok.Data;

import java.util.UUID;

@Data
public class AddressDTO {
    UUID addressId;
    String street;
    String city;
    String country;
    String postalCode;
    AddressType addressType;
    Boolean isDefault;
}

