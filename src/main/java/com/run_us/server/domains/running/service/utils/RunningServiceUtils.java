package com.run_us.server.domains.running.service.utils;

import com.run_us.server.domains.running.domain.RunningConstants;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RunningServiceUtils {
  public static String createLiveKey(String runningId, String userId, String suffix) {
    return RunningConstants.RUNNING_PREFIX + runningId + ":" + userId + suffix;
  }

  public static String extractUserIdFromKey(String key) {
    String[] parts = key.split(":");
    return parts.length >= 3 ? parts[2] : null;
  }
}
