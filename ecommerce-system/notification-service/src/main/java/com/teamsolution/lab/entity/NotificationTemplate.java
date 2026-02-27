package com.teamsolution.lab.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "m_notification_template")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(force = true)
@Getter
@Setter
@SuperBuilder
public class NotificationTemplate extends BaseEntity {

  @Column(name = "event_type")
  private String eventType;

  @Column(name = "channel")
  private String channel;

  @Column(name = "subject")
  private String subject;

  @Column(name = "body_template")
  private String bodyTemplate;

  @Column(name = "is_active")
  private boolean isActive;
}
