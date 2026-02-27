package com.teamsolution.lab.mapper;

import com.teamsolution.lab.dto.CustomerDto;
import com.teamsolution.lab.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CustomerMapper extends BaseMapper<Customer, CustomerDto> {

  @Override
  @Mapping(target = "addresses", source = "addresses")
  CustomerDto toDto(Customer entity);

  @Override
  @Mapping(target = "addresses", source = "addresses")
  Customer toEntity(CustomerDto dto);

  @Override
  @Mapping(target = "addresses", ignore = true)
  void updateEntityFromDto(CustomerDto dto, @MappingTarget Customer entity);
}
