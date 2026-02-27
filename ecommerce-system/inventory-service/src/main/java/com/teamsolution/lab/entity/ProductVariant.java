package com.teamsolution.lab.entity;

import com.teamsolution.lab.enums.ProductVariantStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "m_product_variant")
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@NoArgsConstructor(force = true)
@SuperBuilder
public class ProductVariant extends BaseEntity {
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  @Column(name = "sku")
  private String sku;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "size_id")
  private Size size;

  @Column(name = "color")
  private String color;

  @Column(name = "price")
  private Long price;

  @Column(name = "image_url")
  private String imageUrl;

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  @Builder.Default
  private ProductVariantStatus status = ProductVariantStatus.ACTIVE;

  @OneToOne(mappedBy = "variant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private ProductVariantInventory inventory;
}
