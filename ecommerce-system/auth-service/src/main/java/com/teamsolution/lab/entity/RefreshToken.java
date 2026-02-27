package com.teamsolution.lab.entity;

import com.teamsolution.lab.util.UuidGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "refresh_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {

  @Id
  @Column(name = "id")
  @Builder.Default
  private UUID id = UuidGenerator.generate();

  @ManyToOne
  @JoinColumn(name = "account_id")
  private Account account;

  @Column(name = "token")
  private String token;

  @Column(name = "is_used")
  @Builder.Default
  private boolean isUsed = false;

  @Column(name = "expires_at")
  private LocalDateTime expiresAt;

  @CreationTimestamp
  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;
}
