/**
 * packageName    : wanted.preonboarding.assignment.domain
 * fileName       : Post
 * author         : 우태균
 * description    : 게시글 엔티티
 */
package wanted.preonboarding.assignment.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Table(name = "POST")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@SuperBuilder
public class Post extends BaseTimeEntity {
  @Id @GeneratedValue
  private Long id;
  @Column(length = 255, nullable = false)
  private String title;
  @Lob
  @Column(nullable = false)
  private String body;
  @Column(nullable = false)
  private Integer views;
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private User author;

  //[편의 메서드]
  public void setAuthor(User userEntity) {
    if (this.author != null) {
      if (this.author != userEntity) this.author.getPostList().remove(this);
      else return;
    }
    this.author = userEntity;
    userEntity.addPost(this);
  }

  /**
   * 조회수 증가 메서드
   */
  public void increaseViews() {
    this.views++;
  }
}
