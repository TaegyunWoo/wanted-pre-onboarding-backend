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

  public void save(TokenPair tokenPair) {
    entityManager.persist(tokenPair);
  }


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

  public void delete(TokenPair tokenPair) {
    entityManager.remove(tokenPair);
  }
}
