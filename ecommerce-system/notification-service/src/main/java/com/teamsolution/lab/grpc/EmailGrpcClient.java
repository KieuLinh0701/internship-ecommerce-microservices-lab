package com.teamsolution.lab.grpc;

import com.teamsolution.lab.grpc.email.EmailRequest;
import com.teamsolution.lab.grpc.email.EmailResponse;
import com.teamsolution.lab.grpc.email.EmailServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailGrpcClient {

  private final GrpcServiceDiscovery grpcServiceDiscovery;

  // Send verification email
  public boolean sendEmail(String to, String subject, String body) {
    // Get Ip and gRPC port of Email Service from Eureka
    String target = grpcServiceDiscovery.getGrpcTarget("email-service"); // ví dụ: "127.0.0.1:9098"

    // Create gRPC channel
    ManagedChannel channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();

    try {
      // Create gRPC stub
      EmailServiceGrpc.EmailServiceBlockingStub stub = EmailServiceGrpc.newBlockingStub(channel);

      // Build request
      EmailRequest request =
          EmailRequest.newBuilder().setTo(to).setSubject(subject).setBody(body).build();

      // Email gRPC call
      EmailResponse response = stub.sendEmail(request);

      return response.getSuccess();
    } finally {
      // Shutdown channel
      channel.shutdown();
    }
  }
}
