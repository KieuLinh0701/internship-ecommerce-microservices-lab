package com.teamsolution.lab.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Table(name = "m_brand")
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Brand extends BaseEntity {

  @Column(name = "name")
  private String name;

  @Column(name = "slug")
  private String slug;

  @Column(name = "logo_url")
  private String logoUrl;

  @Column(name = "status")
  @Builder.Default
  private Boolean status = true;

  @Column(name = "description")
  private String description;

  @OneToMany(mappedBy = "brand", fetch = FetchType.LAZY)
  private List<Product> products;
}
