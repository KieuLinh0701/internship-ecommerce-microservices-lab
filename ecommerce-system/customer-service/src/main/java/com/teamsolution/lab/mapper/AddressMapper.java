package com.teamsolution.lab.mapper;

import com.teamsolution.lab.dto.AddressDto;
import com.teamsolution.lab.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AddressMapper extends BaseMapper<Address, AddressDto> {
  @Override
  AddressDto toDto(Address entity);

  @Override
  Address toEntity(AddressDto dto);

  @Override
  void updateEntityFromDto(AddressDto dto, @MappingTarget Address entity);
}
