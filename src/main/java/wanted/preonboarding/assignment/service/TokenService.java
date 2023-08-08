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
import wanted.preonboarding.assignment.mapper.TokenPairMapper;
import wanted.preonboarding.assignment.repository.TokenPairRedisRepository;
import wanted.preonboarding.assignment.utils.jwt.JwtTokenIssuer;
import wanted.preonboarding.assignment.utils.jwt.JwtTokenParser;

import java.util.Objects;

import static wanted.preonboarding.assignment.dto.TokenDto.TokenRequest;
import static wanted.preonboarding.assignment.dto.TokenDto.TokenResponse;

@RequiredArgsConstructor
@Service
public class TokenService {
  private final TokenPairRedisRepository tokenPairRedisRepository;
  private final JwtTokenIssuer jwtTokenIssuer;
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
      claims = jwtTokenParser.getValidClaims(token);
    } catch (ExpiredJwtException e) {
      throw new InvalidValueException(ErrorCode.EXPIRED_ACCESS_TOKEN);
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

  /**
   * 토큰 재발급 메서드
   * @param request 만료된 AccessToken, 유효한 RefreshToken 쌍
   * @return 새로 발급된 토큰 쌍
   */
  public TokenResponse reissue(TokenRequest request) {
    long userPk;
    boolean isExpiredAccessToken;
    boolean isExpiredRefreshToken;

    try {
      userPk = jwtTokenParser.withdrawUserPk(request.getExpiredAccessToken());
    } catch (JwtException e) {
      throw new InvalidValueException(ErrorCode.BAD_TOKEN);
    }

    //유효기간 외의 조건이 유효하지 않은 경우
    try {
      isExpiredAccessToken = jwtTokenParser.isExpired(request.getExpiredAccessToken());
      isExpiredRefreshToken = jwtTokenParser.isExpired(request.getValidRefreshToken());
    } catch (JwtException e) {
      throw new InvalidValueException(ErrorCode.BAD_TOKEN);
    }

    //아직 Access Token이 만료되지 않은 경우
    if (!isExpiredAccessToken) {
      //토큰 쌍 Redis에서 제거 (비정상 접근으로 판단)
      tokenPairRedisRepository.deleteById(userPk);
      throw new InvalidValueException(ErrorCode.IS_NOT_EXPIRED_YET);
    }

    //Refresh Token까지 만료된 경우
    if (isExpiredRefreshToken) {
      throw new InvalidValueException(ErrorCode.EXPIRED_REFRESH_TOKEN);
    }

    //재발급
    String accessToken = jwtTokenIssuer.createToken(userPk, false);
    String refreshToken = jwtTokenIssuer.createToken(userPk, true);
    TokenPair entity = TokenPairMapper.INSTANCE.toEntity(accessToken, refreshToken, userPk);

    return TokenPairMapper.INSTANCE.toResponseDto(entity);
  }

}
