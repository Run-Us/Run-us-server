package com.run_us.server.domains.running.controller;

import com.run_us.server.domains.user.domain.User;
import com.run_us.server.domains.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.run_us.server.global.common.GlobalConsts.SESSION_ATTRIBUTE_USER;
import static com.run_us.server.global.common.GlobalConsts.WS_USER_AUTH_HEADER;


/**
 * STOMP 인터셉터로 추후 인증, 권한 체크에 활용할 클래스.
 * Message의 분기를 통해 CONNECT, SUBSCRIBE, SEND, DISCONNECT 등의 이벤트를 처리할 수 있다.
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class StompInterceptor implements ChannelInterceptor {
  private final UserService userService;

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {

    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

    switch (accessor.getCommand()) { // TODO: 인증, 인가, 권한 체크
      case CONNECT -> {
        log.info("CONNECT");
        setUserInfoInSession(accessor);
      }
      case SUBSCRIBE -> {
        log.info("SUBSCRIBE : {} by {}", accessor.getDestination(), accessor.getSubscriptionId());
        validateSubscription(accessor);
      }
    }
    return message;
  }

  /**
   * stomp 요청 헤더로부터 user public id 값을 가져와 세션에 User 정보를 저장
   * @param accessor
   */
  private void setUserInfoInSession(StompHeaderAccessor accessor) {
    String userPublicId = accessor.getFirstNativeHeader(WS_USER_AUTH_HEADER);
    User user = userService.getUserByPublicId(userPublicId);
    Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
    sessionAttributes.put(SESSION_ATTRIBUTE_USER, user);
    accessor.setSessionAttributes(sessionAttributes);
//    log.info("CONNECT setUserInfoInSession : {}", accessor.getSessionAttributes().get(SESSION_ATTRIBUTE_USER));
  }

  private void validateSubscription(StompHeaderAccessor accessor) {
    if (accessor.getDestination().startsWith("/topic")) {
      log.info("Validated subscription to topic {}", accessor.getDestination());
    } else {
      log.warn("Invalid subscription to topic {}", accessor.getDestination());
    }
  }
}
