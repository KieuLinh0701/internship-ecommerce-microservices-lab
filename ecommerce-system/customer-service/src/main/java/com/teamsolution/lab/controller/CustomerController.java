package com.teamsolution.lab.controller;

import com.teamsolution.lab.dto.CustomerDto;
import com.teamsolution.lab.response.ApiResponse;
import com.teamsolution.lab.service.CustomerService;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
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
  public ResponseEntity<ApiResponse<CustomerDto>> getMe(@AuthenticationPrincipal Jwt jwt) {

    String id = jwt.getClaim("account_id");
    UUID accountId = UUID.fromString(id);

    return ResponseEntity.ok(ApiResponse.success(customerService.getByAccountId(accountId)));
  }

  @GetMapping("/test")
  public ResponseEntity<ApiResponse<String>> test() {
    return ResponseEntity.ok(ApiResponse.success("Hi, this is a test endpoint!"));
  }
}
