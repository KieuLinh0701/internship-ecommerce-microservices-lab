package com.teamsolution.lab.dto.request;

import com.teamsolution.lab.request.BaseFilterRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductFilterRequest extends BaseFilterRequest {
  private String keyword;
  private String categorySlug;
  private String brandSlug;
  private Long minPrice;
  private Long maxPrice;
}
