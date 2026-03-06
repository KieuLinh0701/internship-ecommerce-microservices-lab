package com.teamsolution.lab.dto;

import com.teamsolution.lab.enums.CartItemStatus;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemDto {
  private UUID id;

  private int quantity;

  private String productSlug;

  private String productName;

  private String variantPrice;

  private String variantImageUrl;

  private CartItemStatus status;
}
