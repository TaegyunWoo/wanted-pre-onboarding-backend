package wanted.preonboarding.assignment.mapper;

import org.junit.jupiter.api.Test;
import wanted.preonboarding.assignment.domain.TokenPair;
import wanted.preonboarding.assignment.domain.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static wanted.preonboarding.assignment.dto.TokenDto.TokenResponse;

class TokenPairMapperTest {
  @Test
  void 엔티티로_ResponseDTO_만들기() {
    //GIVEN
    String accessToken = "my access token value";
    String refreshToken = "my refresh token value";
    TokenPair tokenPairEntity = TokenPair.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();

    //WHEN
    TokenResponse result = TokenPairMapper.INSTANCE.toResponseDto(tokenPairEntity);

    //THEN
    assertEquals(accessToken, result.getAccessToken());
    assertEquals(refreshToken, result.getRefreshToken());
  }

  @Test
  void 토큰값으로_엔티티_만들기() {
    //GIVEN
    String accessToken = "my access token value";
    String refreshToken = "my refresh token value";
    User tokenOwnerEntity = new User();

    //WHEN
    TokenPair result = TokenPairMapper.INSTANCE.toEntity(accessToken, refreshToken, tokenOwnerEntity);

    //THEN
    assertEquals(accessToken, result.getAccessToken());
    assertEquals(refreshToken, result.getRefreshToken());
    assertSame(tokenOwnerEntity, result.getUser());
  }

  @Test
  void 토큰값으로_엔티티_업데이트() {
    //GIVEN
    String oldAccessToken = "my old access token value";
    String oldRefreshToken = "my old refresh token value";
    TokenPair tokenPairEntity = TokenPair.builder()
        .accessToken(oldAccessToken)
        .refreshToken(oldRefreshToken)
        .build();
    String newAccessToken = "my new access token value";
    String newRefreshToken = "my new refresh token value";

    //WHEN
    TokenPairMapper.INSTANCE.updateEntity(newAccessToken, newRefreshToken, tokenPairEntity);

    //THEN
    assertEquals(newAccessToken, tokenPairEntity.getAccessToken());
    assertEquals(newRefreshToken, tokenPairEntity.getRefreshToken());
  }
}