/**
 * packageName    : wanted.preonboarding.assignment.domain
 * fileName       : User
 * author         : 우태균
 * date           : 2023/08/06
 * description    : 사용자 엔티티
 */
package wanted.preonboarding.assignment.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "USER")
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
}
