package com.teamsolution.lab.mapper;

import com.teamsolution.lab.dto.CustomerDto;
import com.teamsolution.lab.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CustomerMapper extends BaseMapper<Customer, CustomerDto> {}
