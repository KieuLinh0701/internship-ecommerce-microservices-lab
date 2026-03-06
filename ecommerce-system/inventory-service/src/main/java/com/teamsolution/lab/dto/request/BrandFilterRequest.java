package com.teamsolution.lab.dto.request;

import com.teamsolution.lab.request.BaseFilterRequest;
import java.util.UUID;
import lombok.Data;

@Data
public class ProductFilterRequest extends BaseFilterRequest {
  private String keyword;
  private String categorySlug;
  private String brandSlug;
  private Long minPrice;
  private Long maxPrice;
  private UUID sizeId;
}
