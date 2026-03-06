package com.teamsolution.lab.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "product_images")
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class ProductImage extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id")
  private Product product;

  @Column(name = "image_url")
  private String imageUrl;

  @Column(name = "sort_order")
  @Builder.Default
  private int sortOrder = 0;

  @Column(name = "is_thumbnail")
  @Builder.Default
  private Boolean isThumbnail = false;
}
