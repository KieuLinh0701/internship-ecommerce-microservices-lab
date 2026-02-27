package com.teamsolution.lab.service.impl;

import com.teamsolution.lab.dto.CustomerDto;
import com.teamsolution.lab.entity.Customer;
import com.teamsolution.lab.exception.ResourceNotFoundException;
import com.teamsolution.lab.mapper.CustomerMapper;
import com.teamsolution.lab.repository.CustomerRepository;
import com.teamsolution.lab.service.CustomerService;
import java.util.UUID;
import org.springframework.stereotype.Service;

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
    Customer entity =
        customerRepository
            .findByAccountId(accountId)
            .orElseThrow(
                () -> new ResourceNotFoundException("Account not found with id: " + accountId));

    return customerMapper.toDto(entity);
  }
}
