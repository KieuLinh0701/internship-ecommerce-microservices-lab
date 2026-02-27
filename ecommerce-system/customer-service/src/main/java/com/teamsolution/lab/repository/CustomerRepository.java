package com.teamsolution.lab.repository;

import com.teamsolution.lab.entity.Customer;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends BaseRepository<Customer, UUID> {
  Optional<Customer> findByAccountId(UUID accountId);
}
