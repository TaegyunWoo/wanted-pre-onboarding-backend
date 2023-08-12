package wanted.preonboarding.assignment.utils.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenIssuerTest {
  private JwtTokenIssuer jwtTokenIssuer;
  private String secretKey = "XJ8uzrdtfRiazK5NavvtaGuPn/c+2PxuqGdABWt/EU0AuGa+wqBqqveoVu6Mpjt15EVeLDy9wi/iNicfOeSayXo0QqTKkclFYsacsmwW+LunIGjJytZP5TZJY0AIvjYuhxfaNMG0DazjRFtOF0X/lP+wCbtmdieYySUUYP9NKnrAwxpaC2c5psQEcdx/wqLZHA3+CFmL1NEsa4dS3Y0qiHAq/JPcu8YAMEeh4w==%";
  private long accessTokenValidMilliSec = 360000;
  private long refreshTokenValidMilliSec = 18000000;

  @BeforeEach
  void setUp() {
    jwtTokenIssuer = new JwtTokenIssuer(secretKey, accessTokenValidMilliSec, refreshTokenValidMilliSec);
  }

  @Test
  void AccessToken_발급() {
    //GIVEN
    long userPk = 1;
    boolean isRefresh = false;

    //WHEN
    String accessToken = jwtTokenIssuer.createToken(userPk, isRefresh);

    //THEN
    long resultUserPk = -1;
    Claims claims = null;
    try {
      claims = Jwts.parser()
          .setSigningKey(secretKey)
          .parseClaimsJws(accessToken)
          .getBody();
    } catch(ExpiredJwtException e) {
      resultUserPk = e.getClaims().get("userPk", Long.class);
      assertSame(userPk, resultUserPk);
    }
    if (claims == null) return;
    resultUserPk = claims.get("userPk", Long.class);
    assertSame(userPk, resultUserPk);
  }

  @Test
  void RefreshToken_발급() {
    //GIVEN
    long userPk = 1;
    boolean isRefresh = true;

    //WHEN
    String refreshToken = jwtTokenIssuer.createToken(userPk, isRefresh);

    //THEN
    long resultUserPk = -1;
    Claims claims = null;
    try {
      claims = Jwts.parser()
          .setSigningKey(secretKey)
          .parseClaimsJws(refreshToken)
          .getBody();
    } catch(ExpiredJwtException e) {
      resultUserPk = e.getClaims().get("userPk", Long.class);
      assertSame(userPk, resultUserPk);
    }
    if (claims == null) return;
    resultUserPk = claims.get("userPk", Long.class);
    assertSame(userPk, resultUserPk);
  }
}