package com.teamsolution.lab.controller;

import com.teamsolution.lab.dto.CustomerDto;
import com.teamsolution.lab.dto.request.CustomerRequest;
import com.teamsolution.lab.response.ApiResponse;
import com.teamsolution.lab.security.SecurityUtils;
import com.teamsolution.lab.service.CustomerService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/me")
@RequiredArgsConstructor
public class CustomerController {

  private final CustomerService customerService;

  @GetMapping
  public ResponseEntity<ApiResponse<CustomerDto>> getMe() {

    UUID currentAccountId = SecurityUtils.getCurrentAccountId();

    CustomerDto customerDto = customerService.getByAccountId(currentAccountId);
    return ResponseEntity.ok(ApiResponse.success(customerDto));
  }

  @PutMapping
  public ResponseEntity<ApiResponse<CustomerDto>> updateMe(
      @Valid @RequestBody CustomerRequest request) {

    UUID currentAccountId = SecurityUtils.getCurrentAccountId();

    CustomerDto customerDto = customerService.updateByAccountId(currentAccountId, request);
    return ResponseEntity.ok(ApiResponse.success(customerDto));
  }
}
