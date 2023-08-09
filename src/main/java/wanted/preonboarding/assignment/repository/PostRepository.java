/**
 * packageName    : wanted.preonboarding.assignment.repository
 * fileName       : PostRepository
 * author         : 우태균
 * date           : 2023/08/09
 * description    : 게시글 Repository
 */
package wanted.preonboarding.assignment.repository;

import org.springframework.stereotype.Repository;
import wanted.preonboarding.assignment.domain.Post;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class PostRepository {
  @PersistenceContext
  private EntityManager entityManager;

  public void save(Post post) {
    entityManager.persist(post);
  }
}
