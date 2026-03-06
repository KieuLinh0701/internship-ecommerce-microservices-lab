package com.teamsolution.lab.service;

import com.teamsolution.lab.dto.AddressDto;
import com.teamsolution.lab.dto.request.AddressAddRequest;
import com.teamsolution.lab.dto.request.AddressUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface AddressService {
  List<AddressDto> getAddressesByAccountId(UUID accountId);

  AddressDto addAddress(UUID accountId, AddressAddRequest request);

  AddressDto updateAddress(UUID accountId, UUID addressId, AddressUpdateRequest request);

  void deleteAddress(UUID accountId, UUID addressId);

  AddressDto setDefaultAddress(UUID accountId, UUID addressId);
}
