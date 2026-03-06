package com.teamsolution.lab.grpc;

import com.google.protobuf.Empty;
import com.teamsolution.lab.entity.Customer;
import com.teamsolution.lab.grpc.customer.CreateRequest;
import com.teamsolution.lab.grpc.customer.CustomerServiceGrpc;
import com.teamsolution.lab.repository.CustomerRepository;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class CustomerGrpcService extends CustomerServiceGrpc.CustomerServiceImplBase {

  private final CustomerRepository customerRepository;

  @Override
  @Transactional
  public void createNewCustomer(CreateRequest request, StreamObserver<Empty> observer) {
    UUID accountId;
    try {
      accountId = UUID.fromString(request.getAccountId());
    } catch (IllegalArgumentException e) {
      log.warn("Invalid accountId received: {}", request.getAccountId());
      observer.onError(
          Status.INVALID_ARGUMENT.withDescription("Invalid accountId").asRuntimeException());
      return;
    }

    try {
      if (customerRepository.existsByAccountId(accountId)) {
        log.warn("Customer already exists for accountId: {}", accountId);
      } else {
        Customer customer =
            Customer.builder()
                .accountId(accountId)
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .avatarUrl(request.getAvatarUrl())
                .build();
        customerRepository.save(customer);
        log.info("Customer created for accountId: {}", accountId);
      }

      observer.onNext(Empty.getDefaultInstance());
      observer.onCompleted();

    } catch (Exception e) {
      log.error(
          "Failed to create customer for accountId: {}, error: {}", accountId, e.getMessage(), e);
      observer.onError(Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
    }
  }
}
