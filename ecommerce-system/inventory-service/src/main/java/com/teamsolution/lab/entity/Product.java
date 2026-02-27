package com.teamsolution.lab.entity;

import com.teamsolution.lab.enums.ProductStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "m_product")
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@NoArgsConstructor(force = true)
@SuperBuilder
public class Product extends BaseEntity {

  @Column(name = "name")
  private String name;

  @Column(name = "description")
  private String description;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id")
  private Category category;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "brand_id")
  private Brand brand;

  @Column(name = "base_price")
  private long basePrice;

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  @Builder.Default
  private ProductStatus status = ProductStatus.ACTIVE;

  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<ProductVariant> variants;
}
