package com.fsk.ecommerce.mapper;

import com.fsk.ecommerce.entity.CreditCard;
import com.fsk.ecommerce.mapper.dto.CreditCardDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CreditCardMapper {
    
    @Mapping(source = "id", target = "cardId")
    CreditCardDTO toDTO(CreditCard creditCard);
}

