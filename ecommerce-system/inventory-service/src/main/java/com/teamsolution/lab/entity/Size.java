package com.teamsolution.lab.entity;

import com.teamsolution.lab.enums.SizeStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "m_size")
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@NoArgsConstructor(force = true)
@SuperBuilder
public class Size extends BaseEntity {
  @Column(name = "name", nullable = false, length = 50)
  private String name;

  @Column(name = "code", nullable = false, length = 20)
  private String code;

  @Column(name = "sort_order", nullable = false)
  @Builder.Default
  private Integer sortOrder = 0;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 20)
  @Builder.Default
  private SizeStatus status = SizeStatus.ACTIVE;
}
