/**
 * packageName    : wanted.preonboarding.assignment.domain
 * fileName       : User
 * author         : 우태균
 * description    : 사용자 엔티티
 */
package wanted.preonboarding.assignment.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "MEMBER")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@SuperBuilder
public class User extends BaseTimeEntity {
  @Id @GeneratedValue
  private Long id;
  @Column(nullable = false, unique = true)
  private String accountId;
  @Column(nullable = false)
  private String password;

  //양방향 설정
  @Builder.Default
  @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Post> postList = new ArrayList<>();

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private TokenPair tokenPair;

  //[편의 메서드]
  public void addPost(Post postEntity) {
    postEntity.setAuthor(this);
    if (postList.contains(postEntity)) return;
    else postList.add(postEntity);
  }

  public void setTokenPair(TokenPair tokenPairEntity) {
    if (this.tokenPair == tokenPairEntity) return;
    this.tokenPair = tokenPairEntity;
    tokenPairEntity.setUser(this);
  }
}
