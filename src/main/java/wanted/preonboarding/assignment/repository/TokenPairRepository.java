/**
 * packageName    : wanted.preonboarding.assignment.repository
 * fileName       : TokenPairRedisRepository
 * author         : 우태균
 * description    : 토큰 Redis Repository
 */
package wanted.preonboarding.assignment.repository;

import org.springframework.stereotype.Repository;
import wanted.preonboarding.assignment.domain.TokenPair;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Repository
public class TokenPairRepository {
  @PersistenceContext
  private EntityManager entityManager;

  /**
   * 토큰쌍 저장 메서드
   * @param tokenPair 저장할 토큰쌍 엔티티
   */
  public void save(TokenPair tokenPair) {
    entityManager.persist(tokenPair);
  }

  /**
   * 사용자 ID(PK)로 토큰쌍 조회 메서드
   * @param userPk 사용자 ID(PK)
   * @return
   */
  public Optional<TokenPair> findByUserId(long userPk) {
    String jpql = """
                  select t from TokenPair t
                   where t.user.id = :userPk
        """;

    TypedQuery<TokenPair> query = entityManager.createQuery(jpql, TokenPair.class)
        .setParameter("userPk", userPk);

    try {
      return Optional.of(query.getSingleResult());
    } catch (NoResultException e) {
      return Optional.empty();
    }
  }

  /**
   * 토큰쌍 제거 메서드
   * @param tokenPair 제거할 토큰쌍 엔티티
   */
  public void delete(TokenPair tokenPair) {
    entityManager.remove(tokenPair);
  }
}
