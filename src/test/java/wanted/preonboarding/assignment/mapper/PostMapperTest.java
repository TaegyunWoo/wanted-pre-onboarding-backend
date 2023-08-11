package wanted.preonboarding.assignment.mapper;

import org.junit.jupiter.api.Test;
import wanted.preonboarding.assignment.domain.Post;
import wanted.preonboarding.assignment.domain.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static wanted.preonboarding.assignment.dto.PostDto.*;

class PostMapperTest {
  @Test
  void 요청DTO와_사용자엔티티로_엔티티_만들기() {
    //GIVEN
    String title = "my title of a post";
    String body = "my body of a post";
    PostRequest requestDto = PostRequest.builder()
        .title(title)
        .body(body)
        .build();
    long userId = 1;
    String accountId = "myfoo@gmail.com";
    String encodedPw = "myEncodedPassword";
    User authorEntity = User.builder()
        .id(userId)
        .accountId(accountId)
        .password(encodedPw)
        .build();

    //WHEN
    Post result = PostMapper.INSTANCE.toEntity(requestDto, authorEntity);

    //THEN
    assertNull(result.getId());
    assertEquals(title, result.getTitle());
    assertEquals(body, result.getBody());
    assertEquals(0, result.getViews());
    assertEquals(authorEntity, result.getAuthor());
    assertTrue(authorEntity.getPostList().contains(result));
  }

  @Test
  void 엔티티로_SimpleResponseDTO_만들기() {
    //GIVEN
    long userId = 1;
    String accountId = "myfoo@gmail.com";
    String encodedPw = "myEncodedPassword";
    User authorEntity = User.builder()
        .id(userId)
        .accountId(accountId)
        .password(encodedPw)
        .build();
    long postId = 2;
    String title = "my title of a post";
    LocalDateTime dateTime = LocalDateTime.now();
    Post postEntity = Post.builder()
        .id(postId)
        .title(title)
        .views(0)
        .createdDate(dateTime)
        .modifiedDate(dateTime)
        .build();
    postEntity.setAuthor(authorEntity);

    //WHEN
    PostSimpleResponse result = PostMapper.INSTANCE.toSimpleResponseDto(postEntity);

    //THEN
    assertAll( //validate post data
        () -> assertEquals(postId, result.getId()),
        () -> assertEquals(title, result.getTitle()),
        () -> assertEquals(0, result.getViews()),
        () -> assertEquals(dateTime, result.getCreatedDate()),
        () -> assertEquals(dateTime, result.getModifiedDate())
    );
    assertAll( //validate author data
        () -> assertEquals(authorEntity.getId(), result.getAuthor().getId()),
        () -> assertEquals(accountId, result.getAuthor().getAccountId())
    );
  }

  @Test
  void 엔티티로_ResponseDTO_만들기() {
    //GIVEN
    long userId = 1;
    String accountId = "myfoo@gmail.com";
    String encodedPw = "myEncodedPassword";
    User authorEntity = User.builder()
        .id(userId)
        .accountId(accountId)
        .password(encodedPw)
        .build();
    long postId = 2;
    String title = "my title of a post";
    String body = "my body of a post";
    LocalDateTime dateTime = LocalDateTime.now();
    Post postEntity = Post.builder()
        .id(postId)
        .title(title)
        .body(body)
        .views(0)
        .createdDate(dateTime)
        .modifiedDate(dateTime)
        .build();
    postEntity.setAuthor(authorEntity);

    //WHEN
    PostResponse result = PostMapper.INSTANCE.toResponseDto(postEntity);

    //THEN
    assertAll( //validate post data
        () -> assertEquals(postId, result.getId()),
        () -> assertEquals(title, result.getTitle()),
        () -> assertEquals(body, result.getBody()),
        () -> assertEquals(0, result.getViews()),
        () -> assertEquals(dateTime, result.getCreatedDate()),
        () -> assertEquals(dateTime, result.getModifiedDate())
    );
    assertAll( //validate author data
        () -> assertEquals(authorEntity.getId(), result.getAuthor().getId()),
        () -> assertEquals(accountId, result.getAuthor().getAccountId())
    );
  }

  @Test
  void 요청DTO로_엔티티_업데이트() {
    //GIVEN
    long userId = 1;
    String accountId = "myfoo@gmail.com";
    String encodedPw = "myEncodedPassword";
    User authorEntity = User.builder()
        .id(userId)
        .accountId(accountId)
        .password(encodedPw)
        .build();
    long postId = 2;
    String title = "my title of a post";
    String body = "my body of a post";
    LocalDateTime dateTime = LocalDateTime.now();
    Post postEntity = Post.builder()
        .id(postId)
        .title(title)
        .body(body)
        .views(0)
        .createdDate(dateTime)
        .modifiedDate(dateTime)
        .build();
    postEntity.setAuthor(authorEntity);
    String updatedTitle = "my new title of the post";
    String updatedBody = "my new body of the post";
    PostRequest updateRequestDto = PostRequest.builder()
        .title(updatedTitle)
        .body(updatedBody)
        .build();

    //WHEN
    PostMapper.INSTANCE.updateEntity(updateRequestDto, postEntity);

    //THEN
    assertAll( //validate post data
        () -> assertEquals(postId, postEntity.getId()),
        () -> assertEquals(updatedTitle, postEntity.getTitle()),
        () -> assertEquals(updatedBody, postEntity.getBody()),
        () -> assertEquals(0, postEntity.getViews()),
        () -> assertEquals(dateTime, postEntity.getCreatedDate()),
        () -> assertEquals(dateTime, postEntity.getModifiedDate())
    );
    assertAll( //validate author data
        () -> assertEquals(authorEntity.getId(), postEntity.getAuthor().getId()),
        () -> assertEquals(accountId, postEntity.getAuthor().getAccountId())
    );
  }
}