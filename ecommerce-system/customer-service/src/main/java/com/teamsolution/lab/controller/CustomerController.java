package com.teamsolution.lab.controller;

import com.teamsolution.lab.dto.CustomerDto;
import com.teamsolution.lab.dto.request.CustomerRequest;
import com.teamsolution.lab.response.ApiResponse;
import com.teamsolution.lab.service.CustomerService;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/me")
public class CustomerController extends BaseController<CustomerDto, UUID> {

  private final CustomerService customerService;

  protected CustomerController(CustomerService customerService) {
    super(customerService);
    this.customerService = customerService;
  }

  @GetMapping
  public ResponseEntity<ApiResponse<CustomerDto>> getMe(@AuthenticationPrincipal String accountId
      //          @RequestHeader("X-Account-id") String accountId
      ) {

    return ResponseEntity.ok(
        ApiResponse.success(customerService.getByAccountId(UUID.fromString(accountId))));
  }

  @PutMapping
  public ResponseEntity<ApiResponse<CustomerDto>> updateMe(
      @RequestHeader("X-Account-id") String accountId,
      @Valid @RequestBody CustomerRequest request) {
    return ResponseEntity.ok(
        ApiResponse.success(
            customerService.updateByAccountId(UUID.fromString(accountId), request)));
  }
}
