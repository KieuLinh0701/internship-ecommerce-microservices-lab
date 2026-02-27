package com.teamsolution.lab.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "product_variant_inventory")
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@NoArgsConstructor(force = true)
@SuperBuilder
public class ProductVariantInventory extends BaseEntity {
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "variant_id")
  private ProductVariant variant;

  @Column(name = "quantity")
  @Builder.Default
  private Integer quantity = 0;

  @Column(name = "reserved_quantity")
  @Builder.Default
  private Integer reservedQuantity = 0;

  @Column(name = "available_quantity")
  @Builder.Default
  private Integer availableQuantity = 0;

  @Column(name = "low_stock_threshold")
  @Builder.Default
  private Integer lowStockThreshold = 10;

  public void recalculateAvailable() {
    this.availableQuantity = this.quantity - this.reservedQuantity;
  }
}
