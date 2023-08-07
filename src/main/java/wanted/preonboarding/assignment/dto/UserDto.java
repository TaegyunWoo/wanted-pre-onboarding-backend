/**
 * packageName    : wanted.preonboarding.assignment.dto
 * fileName       : UserDto
 * author         : 우태균
 * date           : 2023/08/07
 * description    : 사용자 DTO
 */
package wanted.preonboarding.assignment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;

public class UserDto {
  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class UserRequest {
    @Schema(description = "반드시 @가 포함되어야 함.", example = "myfoo@gmail.com")
    @Pattern(regexp = ".*@.*")
    private String accountId;
    @Schema(description = "반드시 8자 이상이어야 함.")
    @Length(min = 8)
    private String rawPassword;
  }
}
