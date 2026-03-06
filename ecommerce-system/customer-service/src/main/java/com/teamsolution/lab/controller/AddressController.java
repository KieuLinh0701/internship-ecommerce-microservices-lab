package com.teamsolution.lab.controller;

import com.teamsolution.lab.dto.AddressDto;
import com.teamsolution.lab.dto.request.AddressAddRequest;
import com.teamsolution.lab.dto.request.AddressUpdateRequest;
import com.teamsolution.lab.response.ApiResponse;
import com.teamsolution.lab.security.SecurityUtils;
import com.teamsolution.lab.service.AddressService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/me/addresses")
@RequiredArgsConstructor
public class AddressController {

  private final AddressService addressService;

  @GetMapping
  public ResponseEntity<ApiResponse<List<AddressDto>>> getMyAddresses() {

    UUID currentAccountId = SecurityUtils.getCurrentAccountId();

    List<AddressDto> addressDto = addressService.getAddressesByAccountId(currentAccountId);
    return ResponseEntity.ok(ApiResponse.success(addressDto));
  }

  @PostMapping
  public ResponseEntity<ApiResponse<AddressDto>> addAddress(
      @Valid @RequestBody AddressAddRequest request) {

    UUID currentAccountId = SecurityUtils.getCurrentAccountId();

    AddressDto addressDto = addressService.addAddress(currentAccountId, request);
    return ResponseEntity.ok(ApiResponse.success(addressDto));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse<AddressDto>> updateAddress(
      @PathVariable UUID id, @Valid @RequestBody AddressUpdateRequest request) {

    UUID currentAccountId = SecurityUtils.getCurrentAccountId();

    AddressDto addressDto = addressService.updateAddress(currentAccountId, id, request);
    return ResponseEntity.ok(ApiResponse.success(addressDto));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<Void>> deleteAddress(@PathVariable UUID id) {

    UUID currentAccountId = SecurityUtils.getCurrentAccountId();

    addressService.deleteAddress(currentAccountId, id);
    return ResponseEntity.ok(ApiResponse.success(null));
  }

  @PutMapping("/{id}/set-default")
  public ResponseEntity<ApiResponse<AddressDto>> setDefault(@PathVariable UUID id) {

    UUID currentAccountId = SecurityUtils.getCurrentAccountId();

    AddressDto addressDto = addressService.setDefaultAddress(currentAccountId, id);
    return ResponseEntity.ok(ApiResponse.success(addressDto));
  }
}
