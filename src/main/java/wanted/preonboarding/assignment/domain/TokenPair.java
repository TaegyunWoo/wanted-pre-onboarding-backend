/**
 * packageName    : wanted.preonboarding.assignment.domain
 * fileName       : TokenPair
 * author         : 우태균
 * description    : TokenPair 엔티티
 */
package wanted.preonboarding.assignment.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "TOKEN_PAIR")
@Entity
public class TokenPair extends BaseTimeEntity {
  @Id @GeneratedValue
  private Long id;
  @Column(nullable = false)
  private String accessToken;
  @Column(nullable = false)
  private String refreshToken;
  @OneToOne(optional = false)
  private User user;

  //[편의 메서드]
  public void setUser(User userEntity) {
    if (this.user == userEntity) return;
    this.user = userEntity;
    userEntity.setTokenPair(this);
  }
}
