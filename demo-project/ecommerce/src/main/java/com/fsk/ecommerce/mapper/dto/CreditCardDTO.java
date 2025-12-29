package com.fsk.ecommerce.mapper.dto;

import com.fsk.ecommerce.entity.CardStatus;
import com.fsk.ecommerce.entity.CardType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CreditCardDTO {
    UUID cardId;
    String cardNumber;
    String cardHolderName;
    LocalDate expiryDate;
    CardType cardType;
    BigDecimal balance;
    CardStatus status;
    Boolean isDefault;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}

