package com.teamsolution.lab.entity;

import com.teamsolution.lab.util.UuidGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {
  @Id private UUID id = UuidGenerator.generate();

  @Column(name = "user_id")
  private UUID userId;

  @Column(name = "role_id")
  private UUID roleId;

  @Column(name = "type")
  private String type;

  @Column(name = "channel")
  private String channel;

  @Column(name = "title")
  private String title;

  @Column(name = "body")
  private String body;

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private String status;

  @Column(name = "retry_count")
  private int retryCount;

  @Column(name = "sent_at")
  private LocalDateTime sentAt;

  @Column(name = "read_at")
  private LocalDateTime readAt;

  @Column(name = "created_at")
  private LocalDateTime createdAt;
}
