package com.teamsolution.lab.grpc;

import com.teamsolution.lab.grpc.email.EmailRequest;
import com.teamsolution.lab.grpc.email.EmailServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailGrpcClient {
  private final GrpcServiceDiscovery grpcServiceDiscovery;

  // Send verification email
  public com.teamsolution.lab.dto.response.EmailResponse sendVerificationEmail(
      String to, String subject, String body) {
    // Get Ip and gRPC port of Email Service from Eureka
    String target = grpcServiceDiscovery.getGrpcTarget("email-service");

    // Create gRPC channel
    ManagedChannel channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();

    try {
      // Create gRPC stub
      EmailServiceGrpc.EmailServiceBlockingStub stub = EmailServiceGrpc.newBlockingStub(channel);

      EmailRequest request =
          EmailRequest.newBuilder().setTo(to).setSubject(subject).setBody(body).build();

      com.teamsolution.lab.grpc.email.EmailResponse grpcResponse = stub.sendEmail(request);

      return new com.teamsolution.lab.dto.response.EmailResponse(
          grpcResponse.getSuccess(), grpcResponse.getMessage());
    } finally {
      // Shutdown channel
      channel.shutdown();
    }
  }
}
