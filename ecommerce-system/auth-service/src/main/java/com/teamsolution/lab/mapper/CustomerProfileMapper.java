package com.teamsolution.lab.mapper;

import com.teamsolution.lab.dto.response.CustomerProfileGrpcResponse;
import com.teamsolution.lab.grpc.customer.CustomerProfileResponse;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CustomerProfileMapper extends BaseMapper<
        CustomerProfileResponse,
        CustomerProfileGrpcResponse> {
}
