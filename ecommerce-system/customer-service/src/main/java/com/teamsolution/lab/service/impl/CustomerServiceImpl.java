package com.teamsolution.lab.service.impl;

import com.teamsolution.lab.dto.CustomerDto;
import com.teamsolution.lab.dto.request.CustomerRequest;
import com.teamsolution.lab.entity.Customer;
import com.teamsolution.lab.exception.ResourceNotFoundException;
import com.teamsolution.lab.mapper.CustomerMapper;
import com.teamsolution.lab.repository.CustomerRepository;
import com.teamsolution.lab.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CustomerServiceImpl extends BaseServiceImpl<Customer, CustomerDto, UUID>
    implements CustomerService {

  private final CustomerRepository customerRepository;
  private final CustomerMapper customerMapper;

  public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper) {
    super(customerRepository, customerMapper);
    this.customerMapper = customerMapper;
    this.customerRepository = customerRepository;
  }

  @Override
  public CustomerDto getByAccountId(UUID accountId) {
    Customer entity = findByAccountId(accountId);

    return customerMapper.toDto(entity);
  }

  @Override
  public CustomerDto updateByAccountId(UUID accountId, CustomerRequest request) {
    Customer customer = findByAccountId(accountId);

    customer.setFullName(request.getFullName());
    customer.setPhone(request.getPhone());
    customer.setAvatarUrl(request.getAvatarUrl());

    customerRepository.save(customer);

    return customerMapper.toDto(customer);
  }

  @Override
  public Customer findByAccountId(UUID accountId) {
    return customerRepository
        .findByAccountId(accountId)
        .orElseThrow(
            () -> new ResourceNotFoundException("Customer not found with accountId: " + accountId));
  }
}
