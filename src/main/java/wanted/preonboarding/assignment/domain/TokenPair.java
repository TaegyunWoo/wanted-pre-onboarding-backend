/**
 * packageName    : wanted.preonboarding.assignment.domain
 * fileName       : TokenPair
 * author         : 우태균
 * date           : 2023/08/06
 * description    : TokenPair 엔티티
 */
package wanted.preonboarding.assignment.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;


@Getter
@Setter
@AllArgsConstructor
@RedisHash("TokenPair") //key prefix
public class TokenPair {
  @Id
  private long userPk;
  private String accessToken;
  private String refreshToken;
}
