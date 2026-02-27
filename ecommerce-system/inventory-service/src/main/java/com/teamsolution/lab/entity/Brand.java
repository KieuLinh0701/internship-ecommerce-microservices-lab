package com.teamsolution.lab.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "m_brand")
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@NoArgsConstructor(force = true)
@SuperBuilder
public class Brand extends BaseEntity {

  @Column(name = "name")
  private String name;

  @Column(name = "slug")
  private String slug;

  @Column(name = "logo_url")
  private String logoUrl;

  @Column(name = "status")
  private boolean status;

  @Column(name = "description")
  private String description;

  @OneToMany(mappedBy = "brand", fetch = FetchType.LAZY)
  private List<Product> products;
}
