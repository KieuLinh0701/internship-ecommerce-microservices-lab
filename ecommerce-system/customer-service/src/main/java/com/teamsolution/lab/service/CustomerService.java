package com.teamsolution.lab.service;

import com.teamsolution.lab.dto.CustomerDto;
import com.teamsolution.lab.dto.request.UpdateCustomerRequest;

import java.util.UUID;

public interface CustomerService extends BaseService<CustomerDto, UUID> {
  CustomerDto getByAccountId(UUID accountId);
  CustomerDto updateByAccountId(UUID accountId, UpdateCustomerRequest request);
}
