package wanted.preonboarding.assignment.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import wanted.preonboarding.assignment.domain.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Import({UserRepository.class})
@ExtendWith(SpringExtension.class)
@DataJpaTest
class UserRepositoryTest {
  @PersistenceContext
  private EntityManager entityManager;
  @Autowired
  private UserRepository userRepository;

  @Test
  void 저장() {
    //GIVEN
    //(User 엔티티)
    String accountId = "myfoo@gmail.com";
    String encodedPw = "myEncodedPassword";
    User userEntity = User.builder()
        .accountId(accountId)
        .password(encodedPw)
        .build();

    //WHEN
    try {
      userRepository.save(userEntity);

    //THEN
    } catch (Exception e) {
      fail("save() throws Exception");
    }
  }

  @Test
  void ID로_조회() {
    //GIVEN
    //(User 엔티티 저장)
    String accountId = "myfoo@gmail.com";
    String encodedPw = "myEncodedPassword";
    User userEntity = User.builder()
        .accountId(accountId)
        .password(encodedPw)
        .build();
    entityManager.persist(userEntity);

    long userId = userEntity.getId();

    //WHEN
    Optional<User> result = null;
    try {
      result = userRepository.findById(userId);

      //THEN
    } catch (Exception e) {
      fail("findById() throws Exception");
    }
    assertNotNull(result);
    assertTrue(result.isPresent());
    assertSame(userEntity, result.get());
  }

  @Test
  void 계정ID로_조회() {
    //GIVEN
    //(User 엔티티 저장)
    String accountId = "myfoo@gmail.com";
    String encodedPw = "myEncodedPassword";
    User userEntity = User.builder()
        .accountId(accountId)
        .password(encodedPw)
        .build();
    entityManager.persist(userEntity);

    //WHEN
    Optional<User> result = null;
    try {
      result = userRepository.findByAccountId(accountId);

    //THEN
    } catch (Exception e) {
      fail("findByAccountId() throws Exception");
    }
    assertNotNull(result);
    assertTrue(result.isPresent());
    assertSame(userEntity, result.get());
  }
}