package com.fsk.ecommerce.mapper;

import com.fsk.ecommerce.entity.User;
import com.fsk.ecommerce.mapper.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CreditCardMapper.class, AddressMapper.class, HobbyMapper.class})
public interface UserMapper {

    @Mapping(source = "id", target = "userId")
    UserDTO toDTO(User user);

}
