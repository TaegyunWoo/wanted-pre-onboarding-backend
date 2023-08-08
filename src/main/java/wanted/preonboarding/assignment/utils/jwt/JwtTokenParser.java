/**
 * packageName    : wanted.preonboarding.assignment.utils.jwt
 * fileName       : JwtTokenParser
 * author         : 우태균
 * date           : 2023/08/08
 * description    : JWT Token 파싱 관련 클래스
 */
package wanted.preonboarding.assignment.utils.jwt;

import io.jsonwebtoken.Claims;
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
  public Claims getClaims(String token) throws JwtException {
    return Jwts.parser()
        .setSigningKey(secretKey)
        .parseClaimsJws(token)
        .getBody();
  }
}
