package wanted.preonboarding.assignment.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import wanted.preonboarding.assignment.domain.TokenPair;
import wanted.preonboarding.assignment.domain.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Import({TokenPairRepository.class})
@ExtendWith(SpringExtension.class)
@DataJpaTest
class TokenPairRepositoryTest {
  @PersistenceContext
  private EntityManager entityManager;
  @Autowired
  private TokenPairRepository tokenPairRepository;

  @Test
  void 저장() {
    //GIVEN
    //(User 엔티티 저장)
    String accountId = "myfoo@gmail.com";
    String encodedPw = "myEncodedPassword";
    User userEntity = User.builder()
        .accountId(accountId)
        .password(encodedPw)
        .build();
    entityManager.persist(userEntity);

    //(TokenPair 엔티티)
    String accessToken = "my access token value";
    String refreshToken = "my refresh token value";
    TokenPair tokenPairEntity = TokenPair.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();
    tokenPairEntity.setUser(userEntity);

    //WHEN
    try {
      tokenPairRepository.save(tokenPairEntity);
    } catch (Exception e) {
    //THEN
      fail("save() throws Exception");
    }
  }

  @Test
  void 사용자ID로_조회() {
    //GIVEN
    //(User 엔티티 저장)
    String accountId = "myfoo@gmail.com";
    String encodedPw = "myEncodedPassword";
    User userEntity = User.builder()
        .accountId(accountId)
        .password(encodedPw)
        .build();
    entityManager.persist(userEntity);

    //(TokenPair 엔티티 저장)
    String accessToken = "my access token value";
    String refreshToken = "my refresh token value";
    TokenPair tokenPairEntity = TokenPair.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();
    tokenPairEntity.setUser(userEntity);
    entityManager.persist(tokenPairEntity);

    long userId = userEntity.getId();

    //WHEN
    Optional<TokenPair> result = null;
    try {
      result = tokenPairRepository.findByUserId(userId);
    } catch (Exception e) {
    //THEN
      fail("findByUserId() throws Exception");
    }

    //THEN
    assertNotNull(result);
    assertTrue(result.isPresent());
    assertSame(tokenPairEntity, result.get());
  }

  @Test
  void 삭제() {
    //GIVEN
    //(User 엔티티 저장)
    String accountId = "myfoo@gmail.com";
    String encodedPw = "myEncodedPassword";
    User userEntity = User.builder()
        .accountId(accountId)
        .password(encodedPw)
        .build();
    entityManager.persist(userEntity);

    //(TokenPair 엔티티 저장)
    String accessToken = "my access token value";
    String refreshToken = "my refresh token value";
    TokenPair tokenPairEntity = TokenPair.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();
    tokenPairEntity.setUser(userEntity);
    entityManager.persist(tokenPairEntity);

    //WHEN
    try {
      tokenPairRepository.delete(tokenPairEntity);
    } catch (Exception e) {
      //THEN
      fail("delete() throws Exception");
    }
  }
}