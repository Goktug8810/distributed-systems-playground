package com.fsk.ecommerce.mapper.dto;

import com.fsk.ecommerce.entity.HobbyCategory;
import lombok.Data;

import java.util.UUID;

@Data
public class HobbyDTO {
    UUID hobbyId;
    String name;
    HobbyCategory category;
}

