package wanted.preonboarding.assignment.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import wanted.preonboarding.assignment.domain.Post;
import wanted.preonboarding.assignment.domain.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Import({PostRepository.class})
@ExtendWith(SpringExtension.class)
@DataJpaTest
class PostRepositoryTest {
  @PersistenceContext
  private EntityManager entityManager;
  @Autowired
  private PostRepository postRepository;

  @Test
  void 저장() {
    //GIVEN
    //(User 작성자 엔티티 저장)
    String accountId = "myfoo@gmail.com";
    String encodedPw = "myEncodedPassword";
    User authorEntity = User.builder()
        .accountId(accountId)
        .password(encodedPw)
        .build();
    entityManager.persist(authorEntity);

    //(새 Post 엔티티)
    String title = "my title of a post";
    String body = "my body of a post";
    Post postEntity = Post.builder()
        .title(title)
        .body(body)
        .views(0)
        .build();
    postEntity.setAuthor(authorEntity);

    //WHEN
    try {
      postRepository.save(postEntity);

    //THEN
    } catch (Exception e) {
      fail("save() throws Exception");
    }
  }

  @Test
  void 목록조회와_페이징() {
    //GIVEN
    //(User 작성자 엔티티 저장)
    String accountId = "myfoo@gmail.com";
    String encodedPw = "myEncodedPassword";
    User authorEntity = User.builder()
        .accountId(accountId)
        .password(encodedPw)
        .build();
    entityManager.persist(authorEntity);

    //(새 Post 엔티티 1 저장)
    String title = "my title of a post";
    String body = "my body of a post";
    Post postEntity1 = Post.builder()
        .title(title)
        .body(body)
        .views(0)
        .build();
    postEntity1.setAuthor(authorEntity);
    entityManager.persist(postEntity1);

    //(새 Post 엔티티 2 저장)
    Post postEntity2 = Post.builder()
        .title(title)
        .body(body)
        .views(0)
        .build();
    postEntity2.setAuthor(authorEntity);
    entityManager.persist(postEntity2);

    long cursorId = postEntity2.getId();
    int pageSize = 1;

    //WHEN
    List<Post> result = null;
    try {
      result = postRepository.findWithPagination(cursorId, pageSize);

      //THEN
    } catch (Exception e) {
      fail("findWithPagination() throws Exception");
    }
    assertNotNull(result);
    assertSame(pageSize, result.size());
    assertSame(postEntity1, result.get(0));
  }

  @Test
  void 최근_목록조회() {
    //GIVEN
    //(User 작성자 엔티티 저장)
    String accountId = "myfoo@gmail.com";
    String encodedPw = "myEncodedPassword";
    User authorEntity = User.builder()
        .accountId(accountId)
        .password(encodedPw)
        .build();
    entityManager.persist(authorEntity);

    //(새 Post 엔티티 1 저장)
    String title = "my title of a post";
    String body = "my body of a post";
    Post postEntity1 = Post.builder()
        .title(title)
        .body(body)
        .views(0)
        .build();
    postEntity1.setAuthor(authorEntity);
    entityManager.persist(postEntity1);

    //(새 Post 엔티티 2 저장)
    Post postEntity2 = Post.builder()
        .title(title)
        .body(body)
        .views(0)
        .build();
    postEntity2.setAuthor(authorEntity);
    entityManager.persist(postEntity2);

    int pageSize = 1;

    //WHEN
    List<Post> result = null;
    try {
      result = postRepository.findRecency(pageSize);

      //THEN
    } catch (Exception e) {
      fail("findRecency() throws Exception");
    }
    assertNotNull(result);
    assertSame(pageSize, result.size());
    assertSame(postEntity2, result.get(0));
  }

  @Test
  void ID로_조회() {
    //GIVEN
    //(User 작성자 엔티티 저장)
    String accountId = "myfoo@gmail.com";
    String encodedPw = "myEncodedPassword";
    User authorEntity = User.builder()
        .accountId(accountId)
        .password(encodedPw)
        .build();
    entityManager.persist(authorEntity);

    //(Post 엔티티 저장)
    String title = "my title of a post";
    String body = "my body of a post";
    Post postEntity = Post.builder()
        .title(title)
        .body(body)
        .views(0)
        .build();
    postEntity.setAuthor(authorEntity);
    entityManager.persist(postEntity);

    long postId = postEntity.getId();

    //WHEN
    Optional<Post> result = null;
    try {
      result = postRepository.findById(postId);

    //THEN
    } catch (Exception e) {
      fail("findById() throws Exception");
    }
    assertNotNull(result);
    assertTrue(result.isPresent());
    assertSame(postEntity, result.get());
  }

  @Test
  void 삭제() {
    //GIVEN
    //(User 작성자 엔티티 저장)
    String accountId = "myfoo@gmail.com";
    String encodedPw = "myEncodedPassword";
    User authorEntity = User.builder()
        .accountId(accountId)
        .password(encodedPw)
        .build();
    entityManager.persist(authorEntity);

    //(Post 엔티티 저장)
    String title = "my title of a post";
    String body = "my body of a post";
    Post postEntity = Post.builder()
        .title(title)
        .body(body)
        .views(0)
        .build();
    postEntity.setAuthor(authorEntity);
    entityManager.persist(postEntity);

    //WHEN
    try {
      postRepository.delete(postEntity);

      //THEN
    } catch (Exception e) {
      fail("delete() throws Exception");
    }
  }
}