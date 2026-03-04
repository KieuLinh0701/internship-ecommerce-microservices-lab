package com.teamsolution.lab.controller;

import com.teamsolution.lab.dto.AddressDto;
import com.teamsolution.lab.dto.request.AddressRequest;
import com.teamsolution.lab.response.ApiResponse;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/me/addresses")
@RequiredArgsConstructor
public class AddressController {

  private final AddressService addressService;

  @GetMapping
  public ResponseEntity<ApiResponse<List<AddressDto>>> getMyAddresses(
      @RequestHeader("X-Account-id") String accountId) {
    return ResponseEntity.ok(
        ApiResponse.success(addressService.getAddressesByAccountId(UUID.fromString(accountId))));
  }

  @PostMapping
  public ResponseEntity<ApiResponse<AddressDto>> add(
      @RequestHeader("X-Account-id") String accountId, @Valid @RequestBody AddressRequest request) {

    AddressDto data = addressService.add(UUID.fromString(accountId), request);
    return ResponseEntity.ok(ApiResponse.success(data));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse<AddressDto>> update(
      @RequestHeader("X-Account-id") String accountId,
      @PathVariable UUID id,
      @Valid @RequestBody AddressRequest request) {

    AddressDto data = addressService.update(UUID.fromString(accountId), id, request);
    return ResponseEntity.ok(ApiResponse.success(data));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<Void>> delete(
      @RequestHeader("X-Account-id") String accountId, @PathVariable UUID id) {

    addressService.delete(UUID.fromString(accountId), id);
    return ResponseEntity.ok(ApiResponse.success(null));
  }

  @PutMapping("/{id}/set-default")
  public ResponseEntity<ApiResponse<AddressDto>> setDefault(
      @RequestHeader("X-Account-id") String accountId, @PathVariable UUID id) {

    AddressDto data = addressService.setDefault(UUID.fromString(accountId), id);
    return ResponseEntity.ok(ApiResponse.success(data));
  }
}
