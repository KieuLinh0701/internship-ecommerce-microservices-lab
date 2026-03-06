package com.teamsolution.lab.service.impl;

import com.teamsolution.lab.dto.AddressDto;
import com.teamsolution.lab.dto.request.AddressAddRequest;
import com.teamsolution.lab.dto.request.AddressUpdateRequest;
import com.teamsolution.lab.entity.Address;
import com.teamsolution.lab.entity.Customer;
import com.teamsolution.lab.exception.AddressAccessDeniedException;
import com.teamsolution.lab.exception.DefaultAddressDeletionException;
import com.teamsolution.lab.exception.ResourceNotFoundException;
import com.teamsolution.lab.mapper.AddressMapper;
import com.teamsolution.lab.repository.AddressRepository;
import com.teamsolution.lab.service.AddressService;
import com.teamsolution.lab.service.CustomerService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

  private final AddressRepository addressRepository;
  private final CustomerService customerService;
  private final AddressMapper addressMapper;

  @Override
  public List<AddressDto> getAddressesByAccountId(UUID accountId) {
    Customer customer = customerService.findByAccountId(accountId);

    List<Address> addresses =
        addressRepository.findByCustomerIdAndIsDeleteFalseOrderByIsDefaultDesc(customer.getId());

    return addressMapper.toDtoList(addresses);
  }

  @Override
  @Transactional
  public AddressDto addAddress(UUID accountId, AddressAddRequest request) {
    Customer customer = customerService.findByAccountId(accountId);

    long count = addressRepository.countByCustomerIdAndIsDeleteFalse(customer.getId());
    boolean shouldBeDefault = request.isDefault() || count == 0;

    if (shouldBeDefault) {
      addressRepository.unsetDefaultByCustomerId(customer.getId());
    }

    Address address =
        Address.builder()
            .customer(customer)
            .name(request.name())
            .phone(request.phone())
            .cityCode(request.cityCode())
            .cityName(request.cityName())
            .wardCode(request.wardCode())
            .wardName(request.wardName())
            .detail(request.detail())
            .isDefault(shouldBeDefault)
            .build();

    return addressMapper.toDto(addressRepository.save(address));
  }

  @Override
  @Transactional
  public AddressDto updateAddress(UUID accountId, UUID addressId, AddressUpdateRequest request) {
    Customer customer = customerService.findByAccountId(accountId);

    Address address = findById(addressId);

    assertOwnership(address, customer);

    address.setName(request.name());
    address.setPhone(request.phone());
    address.setCityCode(request.cityCode());
    address.setCityName(request.cityName());
    address.setWardCode(request.wardCode());
    address.setWardName(request.wardName());
    address.setDetail(request.detail());

    return addressMapper.toDto(addressRepository.save(address));
  }

  @Override
  @Transactional
  public void deleteAddress(UUID accountId, UUID addressId) {
    Customer customer = customerService.findByAccountId(accountId);
    Address address = findById(addressId);
    assertOwnership(address, customer);

    // Cannot delete the default address if it is the only remaining address
    if (address.getIsDefault()) {
      long remaining = addressRepository.countByCustomerIdAndIsDeleteFalse(customer.getId());
      if (remaining <= 1) {
        throw new DefaultAddressDeletionException("Cannot delete the only default address");
      }

      addressRepository
          .findFirstByCustomerIdAndIsDeleteFalseAndIdNotOrderByCreatedAtAsc(
              customer.getId(), addressId)
          .ifPresent(
              other -> {
                other.setIsDefault(true);
                addressRepository.save(other);
              });
    }

    address.setIsDelete(true);

    addressRepository.save(address);
  }

  @Override
  @Transactional
  public AddressDto setDefaultAddress(UUID accountId, UUID addressId) {
    Customer customer = customerService.findByAccountId(accountId);
    Address address = findById(addressId);
    assertOwnership(address, customer);

    // Unset old default
    addressRepository.unsetDefaultByCustomerId(customer.getId());

    // Set new default
    address.setIsDefault(true);
    return addressMapper.toDto(addressRepository.save(address));
  }

  private Address findById(UUID id) {
    return addressRepository
        .findByIdAndIsDeleteFalse(id)
        .orElseThrow(() -> new ResourceNotFoundException("Address not found or deleted"));
  }

  private void assertOwnership(Address address, Customer customer) {
    if (!address.getCustomer().getId().equals(customer.getId())) {
      throw new AddressAccessDeniedException("You do not have permission to access this address");
    }
  }
}
