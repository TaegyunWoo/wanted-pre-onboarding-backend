/**
 * packageName    : wanted.preonboarding.assignment.service
 * fileName       : TokenService
 * author         : 우태균
 * date           : 2023/08/08
 * description    : 토큰 Service
 */
package wanted.preonboarding.assignment.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wanted.preonboarding.assignment.domain.TokenPair;
import wanted.preonboarding.assignment.exception.ErrorCode;
import wanted.preonboarding.assignment.exception.InvalidValueException;
import wanted.preonboarding.assignment.repository.TokenPairRedisRepository;
import wanted.preonboarding.assignment.utils.jwt.JwtTokenParser;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class TokenService {
  private final TokenPairRedisRepository tokenPairRedisRepository;
  private final JwtTokenParser jwtTokenParser;

  /**
   * 토큰 검증
   * @param token 검증할 토큰
   * @return 유효한 토큰인 경우, 사용자 PK 값
   */
  public long validateToken(String token) {
    //토큰 유효성 검증
    Claims claims;
    try {
      claims = jwtTokenParser.getClaims(token);
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
    if (!Objects.equals(token, savedTokenPair.getAccessToken())) {
      throw new InvalidValueException(ErrorCode.NOT_ISSUED_TOKEN);
    }

    return userPk;
  }

}
