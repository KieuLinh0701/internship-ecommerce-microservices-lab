package com.teamsolution.lab.grpc;

import com.teamsolution.lab.dto.response.CustomerProfileGrpcResponse;
import com.teamsolution.lab.grpc.customer.CreateRequest;
import com.teamsolution.lab.grpc.customer.CustomerProfileRequest;
import com.teamsolution.lab.grpc.customer.CustomerProfileResponse;
import com.teamsolution.lab.grpc.customer.CustomerServiceGrpc;
import com.teamsolution.lab.mapper.CustomerProfileMapper;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CustomerGrpcClient {
  private final GrpcServiceDiscovery grpcServiceDiscovery;
  private final CustomerProfileMapper customerProfileMapper;

  // Create Customer After creating account
  public void createNewCustomer(
          UUID accountId, String fullName, String phone, String avatarUrl) {

    ManagedChannel channel = buildChannel();

    try {
      // Create gRPC stub
      CustomerServiceGrpc.CustomerServiceBlockingStub stub = CustomerServiceGrpc.newBlockingStub(channel);

      CreateRequest request =
              CreateRequest.newBuilder()
                      .setAccountId(accountId.toString())
                      .setFullName(fullName)
                      .setPhone(phone)
                      .setAvatarUrl(avatarUrl)
                      .build();

      stub.createNewCustomer(request);
    } finally {
      // Shutdown channel
      channel.shutdown();
    }
  }

  public CustomerProfileGrpcResponse getCustomerProfile(UUID accountId) {
    ManagedChannel channel = buildChannel();
    try {
      CustomerServiceGrpc.CustomerServiceBlockingStub stub = CustomerServiceGrpc.newBlockingStub(channel);
      CustomerProfileRequest request = CustomerProfileRequest.newBuilder()
              .setAccountId(accountId.toString())
              .build();

      CustomerProfileResponse grpcResponse =
              stub.getCustomerProfile(request);

      return customerProfileMapper.toDto(grpcResponse);
    } finally {
      channel.shutdown();
    }
  }

  private ManagedChannel buildChannel() {
    String target = grpcServiceDiscovery.getGrpcTarget("customer-service");
    return ManagedChannelBuilder.forTarget(target).usePlaintext().build();
  }
}
