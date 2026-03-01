package com.teamsolution.lab.mapper;

import com.teamsolution.lab.dto.RoleDto;
import com.teamsolution.lab.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RoleMapper extends BaseMapper<Role, RoleDto> {

}
