/**
 * packageName    : wanted.preonboarding.assignment.utils.jwt
 * fileName       : JwtTokenParser
 * author         : 우태균
 * description    : JWT Token 파싱 컴포넌트
 */
package wanted.preonboarding.assignment.utils.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenParser {
  private final String secretKey;

  /**
   * 생성자를 통해, auth-secret.properties 에 정의된 값을 final 필드에 바인딩
   * @param secretKey 비밀키
   */
  public JwtTokenParser(@Value("${jwt.secretKey}") String secretKey) {
    this.secretKey = secretKey;
  }

  /**
   * 유효한 JWT 토큰의 Claim을 추출하는 메서드
   * @param token 추출할 토큰
   * @return 추출한 Claim
   * @throws JwtException 토큰 처리 관련 예외
   */
  public Claims getValidClaims(String token) throws JwtException {
    return Jwts.parser()
        .setSigningKey(secretKey)
        .parseClaimsJws(token)
        .getBody();
  }

  /**
   * 서명 등은 모두 유효하지만, 만료된 토큰인지 확인하는 메서드
   * @param token 확인할 토큰값
   * @return true:만료, false:만료안됨
   * @throws JwtException 유효기간 조건 이외에서 유효하지 않은 경우 (서명이 다르거나 하는 등)
   */
  public boolean isExpired(String token) throws JwtException {
    try {
      getValidClaims(token);
    } catch (ExpiredJwtException e) {
      return true;
    }
    return false;
  }

  /**
   * 토큰 유효기간 관계없이 userPk 클레임 값을 구하는 메서드
   * @param token 확인할 토큰값
   * @return userPk
   * @throws JwtException 유효기간 조건 이외에서 유효하지 않은 경우 (서명이 다르거나 하는 등)
   */
  public long withdrawUserPk(String token) throws JwtException {
    long userPk;
    try {
      userPk = getValidClaims(token).get("userPk", Long.class);
    } catch (ExpiredJwtException e) {
      userPk = e.getClaims().get("userPk", Long.class);
    }
    return userPk;
  }
}
