package com.teamsolution.lab.service;

import com.teamsolution.lab.dto.CustomerDto;
import com.teamsolution.lab.dto.request.CustomerRequest;
import com.teamsolution.lab.entity.Customer;
import java.util.UUID;

public interface CustomerService {
  CustomerDto getByAccountId(UUID accountId);

  CustomerDto updateByAccountId(UUID accountId, CustomerRequest request);

  Customer findByAccountId(UUID accountId);
}
