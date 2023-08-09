/**
 * packageName    : wanted.preonboarding.assignment.repository
 * fileName       : UserRepository
 * author         : 우태균
 * date           : 2023/08/06
 * description    : 사용자 Repository
 */
package wanted.preonboarding.assignment.repository;

import org.springframework.stereotype.Repository;
import wanted.preonboarding.assignment.domain.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Repository
public class UserRepository {
  @PersistenceContext
  private EntityManager entityManager;

  /**
   * User 엔티티를 DB에 저장하는 메서드
   * @param user 저장 Target 엔티티
   */
  public void save(User user) {
    entityManager.persist(user);
  }

  /**
   * User의 pk(id)로 조회하는 메서드
   * @param pk 조회할 pk
   * @return 조회 결과
   */
  public Optional<User> findById(long pk) {
    User user = entityManager.find(User.class, pk);
    return Optional.ofNullable(user);
  }

  /**
   * User의 accountId로 조회하는 메서드
   * @param accountId 조회할 accountId
   * @return 조회 결과
   */
  public Optional<User> findByAccountId(String accountId) {
    String jpql = """
                  select u from User u
                   where u.accountId = :accountId
                  """;
    TypedQuery<User> query = entityManager.createQuery(jpql, User.class)
        .setParameter("accountId", accountId);

    try {
      return Optional.of(query.getSingleResult());
    } catch (NoResultException e) {
      return Optional.empty();
    }
  }
}
