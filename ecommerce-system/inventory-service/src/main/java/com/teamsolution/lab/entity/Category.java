package com.teamsolution.lab.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "m_category")
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@NoArgsConstructor(force = true)
@SuperBuilder
public class Category extends BaseEntity {

  @Column(name = "name")
  private String name;

  @Column(name = "slug")
  private String slug;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_id")
  private Category parent;

  @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
  private List<Category> children;
}
