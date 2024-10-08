package com.run_us.server.global.common;

import static com.run_us.server.global.common.GlobalConst.TIME_ZONE_ID;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import lombok.Getter;

@MappedSuperclass
@Getter
public abstract class DateAudit implements Serializable {

  protected ZonedDateTime createdAt;

  protected ZonedDateTime updatedAt;

  @PrePersist
  protected void prePersist() {
    this.createdAt = ZonedDateTime.now(ZoneId.of(TIME_ZONE_ID));
    this.updatedAt = ZonedDateTime.now(ZoneId.of(TIME_ZONE_ID));
  }

  @PreUpdate
  protected void preUpdate() {
    this.updatedAt = ZonedDateTime.now(ZoneId.of(TIME_ZONE_ID));
  }
}
