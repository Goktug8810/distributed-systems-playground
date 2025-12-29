package com.fsk.ecommerce.mapper.dto;

import com.fsk.ecommerce.entity.Gender;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
public class UserDTO {

    UUID userId;
    String username;
    String email;
    String firstName;
    String lastName;
    String phoneNumber;
    String bio;
    String profileImageUrl;
    LocalDateTime createdAt;
    Gender gender;
    List<CreditCardDTO> creditCards;
    List<AddressDTO> addresses;
    Set<HobbyDTO> hobbies;

}
