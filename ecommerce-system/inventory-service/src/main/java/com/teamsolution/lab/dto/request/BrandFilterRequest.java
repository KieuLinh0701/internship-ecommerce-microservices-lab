package com.teamsolution.lab.dto.request;

import com.teamsolution.lab.request.BaseFilterRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BrandFilterRequest
        extends BaseFilterRequest {
  private String keyword;
}
