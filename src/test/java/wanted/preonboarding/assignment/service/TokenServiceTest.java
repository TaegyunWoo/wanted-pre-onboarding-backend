package wanted.preonboarding.assignment.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import wanted.preonboarding.assignment.domain.TokenPair;
import wanted.preonboarding.assignment.dto.TokenDto;
import wanted.preonboarding.assignment.exception.ErrorCode;
import wanted.preonboarding.assignment.exception.InvalidValueException;
import wanted.preonboarding.assignment.repository.TokenPairRepository;
import wanted.preonboarding.assignment.utils.jwt.JwtTokenIssuer;
import wanted.preonboarding.assignment.utils.jwt.JwtTokenParser;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static wanted.preonboarding.assignment.dto.TokenDto.*;

class TokenServiceTest {
  private TokenService tokenService;
  @Mock
  private TokenPairRepository tokenPairRepository;
  @Mock
  private JwtTokenIssuer jwtTokenIssuer;
  @Mock
  private JwtTokenParser jwtTokenParser;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    tokenService = new TokenService(tokenPairRepository, jwtTokenIssuer, jwtTokenParser);
  }

  @Test
  void 정상_토큰_검증() {
    //GIVEN
    String validAccessToken = "my valid token";
    long validUserId = 1;
    TokenPair tokenPairEntity = TokenPair.builder()
        .accessToken(validAccessToken)
        .build();

    //(mock)
    //Claims
    Claims claims = mock(Claims.class);
    given(claims.get(eq("userPk"), eq(Long.class))).willReturn(validUserId);
    //JwtTokenParser
    given(jwtTokenParser.getValidClaims(eq(validAccessToken))).willReturn(claims);
    //TokenPairRepository
    given(tokenPairRepository.findByUserId(validUserId)).willReturn(Optional.of(tokenPairEntity));

    //WHEN
    long result = -1;
    try {
      result = tokenService.validateToken(validAccessToken);

      //THEN
    } catch (Exception e) {
      fail("Not expected Exception is thrown\n" + e.getMessage());
    }
    assertSame(validUserId, result);
  }

  @Test
  void 만료된_토큰_검증() {
    //GIVEN
    String expiredAccessToken = "my expired token";

    //(mock)
    //JwtTokenParser
    given(jwtTokenParser.getValidClaims(eq(expiredAccessToken))).willThrow(new ExpiredJwtException(null, null, null));

    //WHEN
    try {
      tokenService.validateToken(expiredAccessToken);

      //THEN
      fail("Must throw InvalidValueException");
    } catch(InvalidValueException e) {
      assertSame(ErrorCode.EXPIRED_ACCESS_TOKEN, e.getErrorCode());
    } catch (Exception e) {
      fail("Not expected Exception is thrown\n" + e.getMessage());
    }
  }

  @Test
  void 시그니처_이상_토큰_검증() {
    //GIVEN
    String invalidSignatureAccessToken = "my invalid signature token";

    //(mock)
    //JwtTokenParser
    given(jwtTokenParser.getValidClaims(eq(invalidSignatureAccessToken))).willThrow(new SignatureException(null));

    //WHEN
    try {
      tokenService.validateToken(invalidSignatureAccessToken);

      //THEN
      fail("Must throw InvalidValueException");
    } catch(InvalidValueException e) {
      assertSame(ErrorCode.SIGNATURE_FAIL, e.getErrorCode());
    } catch (Exception e) {
      fail("Not expected Exception is thrown\n" + e.getMessage());
    }
  }

  @Test
  void 기타오류_토큰_검증() {
    //GIVEN
    String badAccessToken = "my bad token";

    //(mock)
    //JwtTokenParser
    given(jwtTokenParser.getValidClaims(eq(badAccessToken))).willThrow(new JwtException(null));

    //WHEN
    try {
      tokenService.validateToken(badAccessToken);

      //THEN
      fail("Must throw InvalidValueException");
    } catch(InvalidValueException e) {
      assertSame(ErrorCode.BAD_TOKEN, e.getErrorCode());
    } catch (Exception e) {
      fail("Not expected Exception is thrown\n" + e.getMessage());
    }
  }

  @Test
  void 조작된_userPK_클레임을_갖는_토큰_검증() {
    //GIVEN
    String badClaimsAccessToken = "my bad claims token";
    long notExistedUserId = 1;

    //(mock)
    //Claims
    Claims claims = mock(Claims.class);
    given(claims.get(eq("userPk"), eq(Long.class))).willReturn(notExistedUserId);
    //JwtTokenParser
    given(jwtTokenParser.getValidClaims(eq(badClaimsAccessToken))).willReturn(claims);
    //TokenPairRepository
    given(tokenPairRepository.findByUserId(notExistedUserId)).willReturn(Optional.empty());

    //WHEN
    try {
      tokenService.validateToken(badClaimsAccessToken);

      //THEN
      fail("Must throw InvalidValueException");
    } catch(InvalidValueException e) {
      assertSame(ErrorCode.NOT_ISSUED_TOKEN, e.getErrorCode());
    } catch (Exception e) {
      fail("Not expected Exception is thrown\n" + e.getMessage());
    }
  }

  @Test
  void DB에_저장된_토큰값과_일치하지_않는_토큰_검증() {
    //GIVEN
    String notExistedAccessToken = "my not existed token";
    String existedAccessToken = "my existed token";
    long validUserId = 1;
    TokenPair tokenPairEntity = TokenPair.builder()
        .accessToken(existedAccessToken)
        .build();

    //(mock)
    //Claims
    Claims claims = mock(Claims.class);
    given(claims.get(eq("userPk"), eq(Long.class))).willReturn(validUserId);
    //JwtTokenParser
    given(jwtTokenParser.getValidClaims(eq(notExistedAccessToken))).willReturn(claims);
    //TokenPairRepository
    given(tokenPairRepository.findByUserId(validUserId)).willReturn(Optional.of(tokenPairEntity));

    //WHEN
    try {
      tokenService.validateToken(notExistedAccessToken);

      //THEN
      fail("Must throw InvalidValueException");
    } catch(InvalidValueException e) {
      assertSame(ErrorCode.NOT_ISSUED_TOKEN, e.getErrorCode());
    } catch (Exception e) {
      fail("Not expected Exception is thrown\n" + e.getMessage());
    }
  }

  @Test
  void 정상_재발급() {
    //GIVEN
    long validUserId = 1;
    String expiredAccessToken = "my expired access token";
    String validRefreshToken = "my valid refresh token";
    TokenRequest requestDto = TokenRequest.builder()
        .expiredAccessToken(expiredAccessToken)
        .validRefreshToken(validRefreshToken)
        .build();
    TokenPair tokenPairEntity = TokenPair.builder()
        .accessToken(expiredAccessToken)
        .refreshToken(validRefreshToken)
        .build();
    String updatedAccessToken = "my updated access token";
    String updatedRefreshToken = "my updated refresh token";

    //(mock)
    //JwtTokenParser
    given(jwtTokenParser.withdrawUserPk(eq(expiredAccessToken))).willReturn(validUserId);
    given(jwtTokenParser.isExpired(eq(expiredAccessToken))).willReturn(true);
    given(jwtTokenParser.isExpired(eq(validRefreshToken))).willReturn(false);
    //TokenPairRepository
    given(tokenPairRepository.findByUserId(validUserId)).willReturn(Optional.of(tokenPairEntity));
    //JwtTokenIssuer
    given(jwtTokenIssuer.createToken(validUserId, false)).willReturn(updatedAccessToken);
    given(jwtTokenIssuer.createToken(validUserId, true)).willReturn(updatedRefreshToken);

    //WHEN
    TokenResponse result = null;
    try {
      result = tokenService.reissue(requestDto);

      //THEN
    } catch (Exception e) {
      fail("not expected Exception is thrown\n" + e.getMessage());
    }
    assertSame(updatedAccessToken, result.getAccessToken());
    assertSame(updatedRefreshToken, result.getRefreshToken());
  }

  @Test
  void 만료조건_외의_조건이_유효하지_않은_AccessToken으로_재발급() {
    //GIVEN
    long validUserId = 1;
    String badAccessToken = "my bad access token";
    String validRefreshToken = "my valid refresh token";
    TokenRequest requestDto = TokenRequest.builder()
        .expiredAccessToken(badAccessToken)
        .validRefreshToken(validRefreshToken)
        .build();

    //(mock)
    //JwtTokenParser
    given(jwtTokenParser.withdrawUserPk(eq(badAccessToken))).willReturn(validUserId);
    given(jwtTokenParser.isExpired(eq(badAccessToken))).willThrow(new JwtException(null));
    given(jwtTokenParser.isExpired(eq(validRefreshToken))).willReturn(false);

    //WHEN
    try {
      tokenService.reissue(requestDto);

      //THEN
      fail("Must throw InvalidValueException");
    } catch(InvalidValueException e) {
      assertSame(ErrorCode.BAD_TOKEN, e.getErrorCode());
    } catch (Exception e) {
      fail("not expected Exception is thrown\n" + e.getMessage());
    }
  }

  @Test
  void 만료조건_외의_조건이_유효하지_않은_RefreshToken으로_재발급() {
    //GIVEN
    long validUserId = 1;
    String expiredAccessToken = "my expired access token";
    String badRefreshToken = "my bad refresh token";
    TokenRequest requestDto = TokenRequest.builder()
        .expiredAccessToken(expiredAccessToken)
        .validRefreshToken(badRefreshToken)
        .build();

    //(mock)
    //JwtTokenParser
    given(jwtTokenParser.withdrawUserPk(eq(expiredAccessToken))).willReturn(validUserId);
    given(jwtTokenParser.isExpired(eq(expiredAccessToken))).willReturn(true);
    given(jwtTokenParser.isExpired(eq(badRefreshToken))).willThrow(new JwtException(null));

    //WHEN
    try {
      tokenService.reissue(requestDto);

      //THEN
      fail("Must throw InvalidValueException");
    } catch(InvalidValueException e) {
      assertSame(ErrorCode.BAD_TOKEN, e.getErrorCode());
    } catch (Exception e) {
      fail("not expected Exception is thrown\n" + e.getMessage());
    }
  }

  @Test
  void 존재하지_않는_userPK값을_클레임으로_갖는_AccessToken으로_재발급() {
    //GIVEN
    long notExistedUserId = 1;
    String expiredAccessToken = "my expired access token";
    String validRefreshToken = "my valid refresh token";
    TokenRequest requestDto = TokenRequest.builder()
        .expiredAccessToken(expiredAccessToken)
        .validRefreshToken(validRefreshToken)
        .build();

    //(mock)
    //JwtTokenParser
    given(jwtTokenParser.withdrawUserPk(eq(expiredAccessToken))).willReturn(notExistedUserId);
    given(jwtTokenParser.isExpired(eq(expiredAccessToken))).willReturn(true);
    given(jwtTokenParser.isExpired(eq(validRefreshToken))).willReturn(false);
    //TokenPairRepository
    given(tokenPairRepository.findByUserId(notExistedUserId)).willReturn(Optional.empty());

    //WHEN
    try {
      tokenService.reissue(requestDto);

      //THEN
      fail("Must throw InvalidValueException");
    } catch(InvalidValueException e) {
      assertSame(ErrorCode.NOT_ISSUED_TOKEN, e.getErrorCode());
    } catch (Exception e) {
      fail("not expected Exception is thrown\n" + e.getMessage());
    }
  }

  @Test
  void 아직_유효한_AccessToken으로_재발급() {
    //GIVEN
    long validUserId = 1;
    String notExpiredAccessToken = "my not expired access token";
    String validRefreshToken = "my valid refresh token";
    TokenRequest requestDto = TokenRequest.builder()
        .expiredAccessToken(notExpiredAccessToken)
        .validRefreshToken(validRefreshToken)
        .build();
    TokenPair tokenPairEntity = TokenPair.builder()
        .accessToken(notExpiredAccessToken)
        .refreshToken(validRefreshToken)
        .build();

    //(mock)
    //JwtTokenParser
    given(jwtTokenParser.withdrawUserPk(eq(notExpiredAccessToken))).willReturn(validUserId);
    given(jwtTokenParser.isExpired(eq(notExpiredAccessToken))).willReturn(false);
    given(jwtTokenParser.isExpired(eq(validRefreshToken))).willReturn(false);
    //TokenPairRepository
    given(tokenPairRepository.findByUserId(validUserId)).willReturn(Optional.of(tokenPairEntity));

    //WHEN
    try {
      tokenService.reissue(requestDto);

      //THEN
      fail("Must throw InvalidValueException");
    } catch(InvalidValueException e) {
      assertSame(ErrorCode.IS_NOT_EXPIRED_YET, e.getErrorCode());
    } catch (Exception e) {
      fail("not expected Exception is thrown\n" + e.getMessage());
    }
    then(tokenPairRepository).should(times(1)).delete(tokenPairEntity);
  }

  @Test
  void 모두_만료된_토큰쌍으로_재발급() {
    //GIVEN
    long validUserId = 1;
    String expiredAccessToken = "my expired access token";
    String expiredRefreshToken = "my expired refresh token";
    TokenRequest requestDto = TokenRequest.builder()
        .expiredAccessToken(expiredAccessToken)
        .validRefreshToken(expiredRefreshToken)
        .build();
    TokenPair tokenPairEntity = TokenPair.builder()
        .accessToken(expiredAccessToken)
        .refreshToken(expiredRefreshToken)
        .build();

    //(mock)
    //JwtTokenParser
    given(jwtTokenParser.withdrawUserPk(eq(expiredAccessToken))).willReturn(validUserId);
    given(jwtTokenParser.isExpired(eq(expiredAccessToken))).willReturn(true);
    given(jwtTokenParser.isExpired(eq(expiredRefreshToken))).willReturn(true);
    //TokenPairRepository
    given(tokenPairRepository.findByUserId(validUserId)).willReturn(Optional.of(tokenPairEntity));

    //WHEN
    try {
      tokenService.reissue(requestDto);

      //THEN
      fail("Must throw InvalidValueException");
    } catch(InvalidValueException e) {
      assertSame(ErrorCode.EXPIRED_REFRESH_TOKEN, e.getErrorCode());
    } catch (Exception e) {
      fail("not expected Exception is thrown\n" + e.getMessage());
    }
  }
}