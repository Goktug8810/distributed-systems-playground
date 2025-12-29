package com.fsk.ecommerce.service;

import com.fsk.ecommerce.mapper.UserMapper;
import com.fsk.ecommerce.mapper.dto.UserDTO;
import com.fsk.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public List<UserDTO> userDTOList() {
        return userRepository.findAllUserDetails().stream().map(userMapper::toDTO).toList();
    }

}
