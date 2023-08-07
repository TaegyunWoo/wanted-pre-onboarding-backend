/**
 * packageName    : wanted.preonboarding.assignment.utils.jwt
 * fileName       : JwtTokenIssuer
 * author         : 우태균
 * date           : 2023/08/07
 * description    : JWT Token 발급자
 */
package wanted.preonboarding.assignment.utils.jwt;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenIssuer {
  private final String secretKey; //서버측 비밀키
  private final long accessTokenValidMilliSec; //Access Token 유효시간
  private final long refreshTokenValidMilliSec; //Refresh Token 유효시간

  public JwtTokenIssuer(@Value("${jwt.secretKey}") String secretKey,
                        @Value("${jwt.accessTokenValidMilliSec}") long accessMilliSec,
                        @Value("${jwt.refreshTokenValidMilliSec}") long refreshMilliSec) {
    this.secretKey = secretKey;
    this.accessTokenValidMilliSec = accessMilliSec;
    this.refreshTokenValidMilliSec = refreshMilliSec;
  }

  /**
   * Access·Refresh 토큰 쌍을 발급받는 메서드
   * @param userPk 발급받은 사용자의 pk 값
   * @return 토큰쌍
   */
  public TokenPair createTokenPair(long userPk) {
    String accessToken = createToken(userPk, false); //Access Token 발급
    String refreshToken = createToken(userPk, true); //Refresh Token 발급
    return new TokenPair(accessToken, refreshToken);
  }

  /**
   * Token 발급 메서드
   * @param userPk 사용자 계정 pk값
   * @param isRefresh true: Refresh Token 발급, false: Access Token 발급
   * @return jwt Access Token
   */
  public String createToken(long userPk, boolean isRefresh) {
    Date now = new Date();
    long validMilliSec = isRefresh ? refreshTokenValidMilliSec : accessTokenValidMilliSec;

    return Jwts.builder()
        .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // jwt를 사용한다고 헤더에 명시
        .setIssuer("wanted.preonboarding.assignment") //토큰 발급자(iss) 설정
        .setIssuedAt(now) //토큰 발급 시간(iat) 설정
        .setExpiration(new Date(now.getTime() + validMilliSec)) // 만료시간 설정
        .claim("userPk", userPk) //토큰을 받을 사용자 pk를 비공개 클레임으로 설정
        .signWith(SignatureAlgorithm.HS512, secretKey) //해싱 알고리즘으로 HS512를 사용하기 때문에, secretKey가 512비트 이상이어야 함
        .compact();
  }

  @Getter
  @Setter
  @AllArgsConstructor
  public static class TokenPair {
    private String accessToken;
    private String refreshToken;
  }

}
