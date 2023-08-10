/**
 * packageName    : wanted.preonboarding.assignment.interceptor
 * fileName       : AuthInterceptor
 * author         : 우태균
 * description    : 인가 처리 인터셉터
 */
package wanted.preonboarding.assignment.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import wanted.preonboarding.assignment.exception.ErrorCode;
import wanted.preonboarding.assignment.exception.InvalidValueException;
import wanted.preonboarding.assignment.service.TokenService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthInterceptor implements HandlerInterceptor {
  public static final String USER_PK_ATTRIBUTE_NAME = "userPk";
  private final TokenService tokenService;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    //Request의 Authorization 헤더 확인
    String authHeaderValue = request.getHeader("Authorization");
    if (authHeaderValue == null) { //Authorization 헤더에 아무런 값이 존재하지 않는 경우
      throw new InvalidValueException(ErrorCode.NO_AUTH_HEADER);
    }
    if (!authHeaderValue.startsWith("Bearer ")) { //Authorization 헤더 값이 올바르지 않는 경우
      throw new InvalidValueException(ErrorCode.INVALID_AUTH_VALUE_FORMAT);
    }
    String accessTokenValue = authHeaderValue.substring("Bearer ".length());

    //토큰 유효성 검증
    long userPk = tokenService.validateToken(accessTokenValue);

    //HttpServletRequest의 attribute에 User PK값 추가
    request.setAttribute(USER_PK_ATTRIBUTE_NAME, userPk);

    return true;
  }
}
