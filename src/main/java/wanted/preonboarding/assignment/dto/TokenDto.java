package wanted.preonboarding.assignment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class TokenDto {
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
