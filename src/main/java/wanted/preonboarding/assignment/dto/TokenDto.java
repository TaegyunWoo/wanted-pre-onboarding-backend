/**
 * packageName    : wanted.preonboarding.assignment.dto
 * fileName       : TokenDto
 * author         : 우태균
 * date           : 2023/08/07
 * description    : 인증 통큰 DTO
 */
package wanted.preonboarding.assignment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class TokenDto {
  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class TokenRequest {
    @Schema(description = "만료된 Access Token 값")
    private String expiredAccessToken;
    @Schema(description = "만료되지 않은 Refresh Token 값")
    private String validRefreshToken;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class TokenResponse {
    @Schema(description = "Access Token 값 (1시간마다 만료됨)")
    private String accessToken;
    @Schema(description = "Refresh Token 값 (2주마다 만료됨)")
    private String refreshToken;
  }
}
