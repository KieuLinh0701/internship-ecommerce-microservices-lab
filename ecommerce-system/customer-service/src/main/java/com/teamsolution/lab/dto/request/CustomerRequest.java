package com.teamsolution.lab.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerRequest {
  @NotBlank(message = "FullName is required")
  private String fullName;

  @Pattern(
      regexp = "^0\\d{9}$",
      message = "Phone number must start with 0 and contain exactly 10 digits")
  private String phone;

  private String avatarUrl;
}
