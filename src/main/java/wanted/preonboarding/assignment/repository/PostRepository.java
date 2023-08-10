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
import java.util.List;
import java.util.Optional;

@Repository
public class PostRepository {
  @PersistenceContext
  private EntityManager entityManager;

  /**
   * 게시글 엔티티 저장
   * @param post 저장할 게시글 엔티티
   */
  public void save(Post post) {
    entityManager.persist(post);
  }

  /**
   * 게시글 엔티티들을 커서 기반 페이징으로 조회 (내림차순)
   * @param cursorId 기준 엔티티 PK(ID)
   * @param size 페이지당 아이템 개수
   * @return 페이징된 게시글 엔티티들
   */
  public List<Post> findWithPagination(long cursorId, int size) {
    String jpql = """
                  select p from Post p
                   where p.id < :cursorId
                   order by p.id desc
                  """;
    List<Post> result = entityManager.createQuery(jpql, Post.class)
        .setParameter("cursorId", cursorId)
        .setMaxResults(size)
        .getResultList();
    return result;
  }

  /**
   * 가장 최근 게시글 엔티티들을 조회
   * @param size 페이지당 아이템 개수
   * @return 가장 최근 게시글 엔티티들
   */
  public List<Post> findRecency(int size) {
    String jpql = """
                  select p from Post p
                   order by p.id desc
                  """;
    List<Post> result = entityManager.createQuery(jpql, Post.class)
        .setMaxResults(size)
        .getResultList();
    return result;
  }

  /**
   * 게시글 ID(PK)으로 특정 게시글을 조회하는 메서드
   * @param pk 조회할 게시글 ID(PK)
   * @return 조회 결과
   */
  public Optional<Post> findById(long pk) {
    return Optional.ofNullable(entityManager.find(Post.class, pk));
  }
}
