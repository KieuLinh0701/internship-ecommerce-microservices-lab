package com.teamsolution.lab.service;

import com.teamsolution.lab.dto.AddressDto;
import com.teamsolution.lab.dto.request.AddressRequest;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public interface AddressService {
  List<AddressDto> getAddressesByAccountId(UUID accountId);

  AddressDto add(UUID accountId, AddressRequest request);

  AddressDto update(UUID accountId, UUID id, AddressRequest request);

  void delete(UUID accountId, UUID addressId);

  AddressDto setDefault(UUID accountId, UUID addressId);
}
