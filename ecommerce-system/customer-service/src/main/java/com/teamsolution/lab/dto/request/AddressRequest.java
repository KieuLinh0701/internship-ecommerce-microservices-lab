package com.teamsolution.lab.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddressRequest(
    @NotBlank(message = "Name is required") String name,
    @NotBlank(message = "Phone is required") String phone,
    @NotNull(message = "City code is required") Integer cityCode,
    @NotBlank(message = "City name is required") String cityName,
    @NotNull(message = "Ward code is required") Integer wardCode,
    @NotBlank(message = "Ward name is required") String wardName,
    @NotBlank(message = "Detail is required") String detail,
    Boolean isDefault) {
  public AddressRequest {
    if (isDefault == null) {
      isDefault = false;
    }
  }
}
