package com.teamsolution.lab.entity;

import com.teamsolution.lab.util.UuidGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@MappedSuperclass
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class BaseEntity {

  @Id
  @Column(columnDefinition = "uuid")
  @EqualsAndHashCode.Include
  @Builder.Default
  private UUID id = UuidGenerator.generate();

  @CreationTimestamp
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Column(name = "created_by", updatable = false)
  private UUID createdBy;

  @Column(name = "updated_by")
  private UUID updatedBy;

  @Column(name = "is_delete")
  @Builder.Default
  private Boolean isDelete = false;

  @Version
  @Column(name = "version")
  private Long version;
}
