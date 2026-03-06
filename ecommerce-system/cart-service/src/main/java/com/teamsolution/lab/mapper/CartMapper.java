package com.teamsolution.lab.mapper;

import com.teamsolution.lab.dto.CartDto;
import com.teamsolution.lab.entity.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CartMapper extends BaseMapper<Cart, CartDto> {}
