package com.teamsolution.lab.grpc;

import com.teamsolution.lab.grpc.customer.CreateRequest;
import com.teamsolution.lab.grpc.customer.CustomerServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerGrpcClient {
  private final GrpcServiceDiscovery grpcServiceDiscovery;

  // Create Customer After creating account
  public void createNewCustomer(UUID accountId, String fullName, String phone, String avatarUrl) {

    ManagedChannel channel = buildChannel();

    try {
      CustomerServiceGrpc.CustomerServiceBlockingStub stub =
          CustomerServiceGrpc.newBlockingStub(channel);

      CreateRequest request =
          CreateRequest.newBuilder()
              .setAccountId(accountId.toString())
              .setFullName(fullName)
              .setPhone(phone)
              .setAvatarUrl(avatarUrl)
              .build();

      stub.createNewCustomer(request);
    } finally {
      channel.shutdown();
    }
  }

  private ManagedChannel buildChannel() {
    String target = grpcServiceDiscovery.getGrpcTarget("customer-service");
    return ManagedChannelBuilder.forTarget(target).usePlaintext().build();
  }
}
