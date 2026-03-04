package com.teamsolution.lab.repository;

import com.teamsolution.lab.entity.Address;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AddressRepository extends BaseRepository<Address, UUID> {

  // Get non-deleted addresses (default first)
  List<Address> findByCustomerIdAndIsDeleteFalseOrderByIsDefaultDesc(UUID customerId);

  // Count non-deleted addresses
  long countByCustomerIdAndIsDeleteFalse(UUID customerId);

  // Find non-deleted address by id
  Optional<Address> findByIdAndIsDeleteFalse(UUID id);

  // Find first non-deleted address (excluding id)
  Optional<Address> findFirstByCustomerIdAndIsDeleteFalseAndIdNotOrderByCreatedAtAsc(
      UUID customerId, UUID excludeId);

  // Unset default addresses
  @Modifying
  @Query(
      "UPDATE Address a SET a.isDefault = false WHERE a.customer.id = :customerId AND a.isDefault = true")
  void unsetDefaultByCustomerId(@Param("customerId") UUID customerId);
}
