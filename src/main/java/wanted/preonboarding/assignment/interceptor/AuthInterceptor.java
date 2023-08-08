/**
 * packageName    : wanted.preonboarding.assignment.interceptor
 * fileName       : AuthInterceptor
 * author         : 우태균
 * date           : 2023/08/08
 * description    : 인가 처리 인터셉터
 */
package wanted.preonboarding.assignment.interceptor;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.servlet.HandlerInterceptor;
import wanted.preonboarding.assignment.domain.TokenPair;
import wanted.preonboarding.assignment.exception.ErrorCode;
import wanted.preonboarding.assignment.exception.InvalidValueException;
import wanted.preonboarding.assignment.repository.TokenPairRedisRepository;
import wanted.preonboarding.assignment.utils.jwt.JwtTokenParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthInterceptor implements HandlerInterceptor {
  private final TokenPairRedisRepository tokenPairRedisRepository;
  private final JwtTokenParser jwtTokenParser;

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
    Claims claims;
    try {
      claims = jwtTokenParser.getClaims(accessTokenValue);
    } catch (ExpiredJwtException e) {
      throw new InvalidValueException(ErrorCode.EXPIRED_TOKEN);
    } catch (SignatureException e) {
      throw new InvalidValueException(ErrorCode.SIGNATURE_FAIL);
    } catch (JwtException e) {
      throw new InvalidValueException(ErrorCode.BAD_TOKEN);
    }

    //DB에 저장된 토큰인지 확인
    long userPk = claims.get("userPk", Long.class);
    TokenPair savedTokenPair = tokenPairRedisRepository.findById(userPk)
        .orElseThrow(() -> new InvalidValueException(ErrorCode.NOT_ISSUED_TOKEN));
    if (Objects.equals(accessTokenValue, savedTokenPair.getAccessToken())) {
      throw new InvalidValueException(ErrorCode.NOT_ISSUED_TOKEN);
    }

    //HttpServletRequest의 attribute에 User PK값 추가
    request.setAttribute("userPk", userPk);

    return true;
  }
}
