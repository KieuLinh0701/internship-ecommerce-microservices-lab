package com.teamsolution.common.kafka.enums.audit;

public enum CommonAuditLogAction implements AuditLogAction {
  CREATE,
  UPDATE,
  DELETE,
  ;

  @Override
  public String getValue() {
    return name();
  }
}
