/**
 * packageName    : wanted.preonboarding.assignment.dto
 * fileName       : LoginUser
 * author         : 우태균
 * date           : 2023/08/08
 * description    : 로그인 사용자 정보 DTO
 */
package wanted.preonboarding.assignment.dto;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginUser {
  private long id;
}
