package wanted.preonboarding.assignment.utils.jwt;

import io.jsonwebtoken.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class JwtTokenParserTest {
  private JwtTokenParser jwtTokenParser;
  private String secretKey = "XJ8uzrdtfRiazK5NavvtaGuPn/c+2PxuqGdABWt/EU0AuGa+wqBqqveoVu6Mpjt15EVeLDy9wi/iNicfOeSayXo0QqTKkclFYsacsmwW+LunIGjJytZP5TZJY0AIvjYuhxfaNMG0DazjRFtOF0X/lP+wCbtmdieYySUUYP9NKnrAwxpaC2c5psQEcdx/wqLZHA3+CFmL1NEsa4dS3Y0qiHAq/JPcu8YAMEeh4w==%";

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    jwtTokenParser = new JwtTokenParser(secretKey);
  }

  @Test
  void 만료된_토큰_확인() {
    //GIVEN
    Date now = new Date();
    String expiredToken = Jwts.builder()
        .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
        .setIssuer("wanted.preonboarding.assignment")
        .setIssuedAt(now)
        .setExpiration(new Date(now.getTime() - 10000000))
        .signWith(SignatureAlgorithm.HS512, secretKey)
        .compact();


    //WHEN
    boolean result = false;
    try {
      result = jwtTokenParser.isExpired(expiredToken);

      //THEN
    } catch (Exception e) {
      fail("not expected Exception is thrown\n" + e.getMessage());
    }
    assertTrue(result);
  }

  @Test
  void 만료되지_않은_토큰_확인() {
    //GIVEN
    int infiniteMilliSec = Integer.MAX_VALUE;
    Date now = new Date();
    String notExpiredToken = Jwts.builder()
        .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
        .setIssuer("wanted.preonboarding.assignment")
        .setIssuedAt(now)
        .setExpiration(new Date(now.getTime() + infiniteMilliSec))
        .signWith(SignatureAlgorithm.HS512, secretKey)
        .compact();

    //WHEN
    boolean result = true;
    try {
      result = jwtTokenParser.isExpired(notExpiredToken);

      //THEN
    } catch (Exception e) {
      fail("not expected Exception is thrown\n" + e.getMessage());
    }
    assertFalse(result);
  }

  @Test
  void 만료된_토큰에서_userPk_클레임_추출() {
    //GIVEN
    long claimedUserPk = 1;
    Date now = new Date();
    String expiredToken = Jwts.builder()
        .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
        .setIssuer("wanted.preonboarding.assignment")
        .setIssuedAt(now)
        .claim("userPk", claimedUserPk)
        .setExpiration(new Date(now.getTime() - 100000))
        .signWith(SignatureAlgorithm.HS512, secretKey)
        .compact();

    //WHEN
    long result = -1;
    try {
      result = jwtTokenParser.withdrawUserPk(expiredToken);

      //THEN
    } catch (Exception e) {
      fail("not expected Exception is thrown\n" + e.getMessage());
    }
    assertSame(claimedUserPk, result);
  }

  @Test
  void 만료되지_않은_토큰에서_userPk_클레임_추출() {
    //GIVEN
    long claimedUserPk = 1;
    int infiniteMilliSec = Integer.MAX_VALUE;
    Date now = new Date();
    String expiredToken = Jwts.builder()
        .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
        .setIssuer("wanted.preonboarding.assignment")
        .setIssuedAt(now)
        .claim("userPk", claimedUserPk)
        .setExpiration(new Date(now.getTime() + infiniteMilliSec))
        .signWith(SignatureAlgorithm.HS512, secretKey)
        .compact();

    //WHEN
    long result = -1;
    try {
      result = jwtTokenParser.withdrawUserPk(expiredToken);

      //THEN
    } catch (Exception e) {
      fail("not expected Exception is thrown\n" + e.getMessage());
    }
    assertSame(claimedUserPk, result);
  }
}