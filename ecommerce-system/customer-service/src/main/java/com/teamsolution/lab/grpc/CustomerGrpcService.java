package com.teamsolution.lab.grpc;

import com.google.protobuf.Empty;
import com.teamsolution.lab.entity.Customer;
import com.teamsolution.lab.exception.ResourceNotFoundException;
import com.teamsolution.lab.grpc.customer.CreateRequest;
import com.teamsolution.lab.grpc.customer.CustomerProfileRequest;
import com.teamsolution.lab.grpc.customer.CustomerProfileResponse;
import com.teamsolution.lab.grpc.customer.CustomerServiceGrpc;
import com.teamsolution.lab.repository.CustomerRepository;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.UUID;

@GrpcService
@RequiredArgsConstructor
public class CustomerGrpcService extends CustomerServiceGrpc.CustomerServiceImplBase {

    private final CustomerRepository customerRepository;

    @Override
    public void createNewCustomer(CreateRequest request, StreamObserver<Empty> observer) {
            Customer customer = Customer.builder()
                    .accountId(UUID.fromString(request.getAccountId()))
                    .fullName(request.getFullName())
                    .phone(request.getPhone())
                    .avatarUrl(request.getAvatarUrl())
                    .build();

            customerRepository.save(customer);

            observer.onNext(Empty.getDefaultInstance());
            observer.onCompleted();
    }

    @Override
    public void getCustomerProfile(CustomerProfileRequest request,
                                   StreamObserver<CustomerProfileResponse> observer) {

        Customer customer = customerRepository
                .findByAccountId(UUID.fromString(request.getAccountId()))
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        CustomerProfileResponse response = CustomerProfileResponse.newBuilder()
                .setFullName(customer.getFullName())
                .setPhone(customer.getPhone())
                .setAvatarUrl(customer.getAvatarUrl() == null ? "" : customer.getAvatarUrl())
                .build();

        observer.onNext(response);
        observer.onCompleted();
    }

}
