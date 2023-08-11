package wanted.preonboarding.assignment.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import wanted.preonboarding.assignment.domain.Post;
import wanted.preonboarding.assignment.domain.User;
import wanted.preonboarding.assignment.exception.ErrorCode;
import wanted.preonboarding.assignment.exception.InvalidValueException;
import wanted.preonboarding.assignment.repository.PostRepository;
import wanted.preonboarding.assignment.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static wanted.preonboarding.assignment.dto.PostDto.*;

class PostServiceTest {
  private PostService postService;
  @Mock
  private UserRepository userRepository;
  @Mock
  private PostRepository postRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    postService = new PostService(userRepository, postRepository);
  }

  @Test
  void 새_게시글_저장_성공() {
    //GIVEN
    long authorPk = 1;
    PostRequest requestDto = PostRequest.builder().build();
    User author = User.builder()
            .id(authorPk)
            .accountId("myfoo@gmail.com")
            .password("my encoded password")
            .build();

    //(mock)
    given(userRepository.findById(authorPk)).willReturn(Optional.of(author));

    //WHEN
    try {
      postService.saveNewPost(requestDto, authorPk);

    //THEN
    } catch (Exception e) {
      fail("saveNewPost() throws Exception\n"+e.getMessage());
    }

    //THEN
    then(userRepository).should().findById(authorPk);
    then(postRepository).should().save(any(Post.class));
  }

  @Test
  void 새_게시글_저장시_전달받은_작성자_ID를_찾을_수_없는_경우() {
    //GIVEN
    long invalidAuthorPk = 2;
    PostRequest requestDto = PostRequest.builder().build();

    //(mock)
    given(userRepository.findById(invalidAuthorPk)).willReturn(Optional.empty());

    //WHEN
    try {
      postService.saveNewPost(requestDto, invalidAuthorPk);

    //THEN
      fail("saveNewPost() must throw InvalidValueException");
    } catch(InvalidValueException e) {
      assertEquals(ErrorCode.NOT_FOUND_CURRENT_USER, e.getErrorCode());
    } catch (Exception e) {
      fail("saveNewPost() throws not expected Exception\n"+e.getMessage());
    }
    then(userRepository).should().findById(invalidAuthorPk);
    then(postRepository).shouldHaveNoInteractions();
  }

  @Test
  void 특정_페이지의_게시글_목록_조회() {
    //GIVEN
    long cursorId = 5;
    int pageSize = 2;
    PaginationRequest paginationRequest = PaginationRequest.builder()
        .cursorId(cursorId)
        .pageSize(pageSize)
        .build();
    List<Post> foundPostList = new ArrayList<>();
    for (int i = 0; i < pageSize; i++)
      foundPostList.add(new Post());

    //(mock)
    given(postRepository.findWithPagination(cursorId, pageSize)).willReturn(foundPostList);

    //WHEN
    List<PostSimpleResponse> result = null;
    try {
      result = postService.inquiryPostList(paginationRequest);

    //THEN
    } catch (Exception e) {
      fail("inquiryPostList() throws Exception\n"+e.getMessage());
    }
    assertNotNull(result);
    assertSame(pageSize, result.size());
    then(postRepository).should(never()).findRecency(anyInt());
    then(postRepository).should().findWithPagination(cursorId, pageSize);
  }

  @Test
  void 가장_최근_게시글_목록_조회() {
    //GIVEN
    int pageSize = 2;
    PaginationRequest paginationRequest = PaginationRequest.builder()
        .cursorId(-1)
        .pageSize(pageSize)
        .build();
    List<Post> foundPostList = new ArrayList<>();
    for (int i = 0; i < pageSize; i++)
      foundPostList.add(new Post());

    //(mock)
    given(postRepository.findRecency(pageSize)).willReturn(foundPostList);

    //WHEN
    List<PostSimpleResponse> result = null;
    try {
      result = postService.inquiryPostList(paginationRequest);

      //THEN
    } catch (Exception e) {
      fail("inquiryPostList() throws Exception\n"+e.getMessage());
    }
    assertNotNull(result);
    assertSame(pageSize, result.size());
    then(postRepository).should().findRecency(anyInt());
    then(postRepository).should(never()).findWithPagination(anyLong(), anyInt());
  }

  @Test
  void 특정_게시글_조회_성공() {
    //GIVEN
    long postId = 1;
    int views = 10;
    Post postEntity = Post.builder()
        .views(views)
        .build();

    //(mock)
    given(postRepository.findById(postId)).willReturn(Optional.of(postEntity));

    //WHEN
    PostResponse result = null;
    try {
      result = postService.inquiryPost(postId);

      //THEN
    } catch (Exception e) {
      fail("inquiryPost() throws Exception\n"+e.getMessage());
    }

    //THEN
    assertNotNull(result);
    assertSame(views+1, result.getViews());
  }

  @Test
  void 특정_게시글_조회시_찾을_수_없는_게시글ID인_경우() {
    //GIVEN
    long invalidPostId = 1;

    //(mock)
    given(postRepository.findById(invalidPostId)).willReturn(Optional.empty());

    //WHEN
    PostResponse result = null;
    try {
      result = postService.inquiryPost(invalidPostId);

      //THEN
      fail("inquiryPost() must throw InvalidValueException");
    } catch(InvalidValueException e) {
      assertSame(ErrorCode.NOT_FOUND_POST, e.getErrorCode());
    } catch (Exception e) {
      fail("inquiryPost() throws not excepted Exception\n"+e.getMessage());
    }
  }

  @Test
  void 게시글_수정_성공() {
    //GIVEN
    //(작성자 사용자 엔티티)
    long authorUserId = 1;
    User authorEntity = User.builder()
        .id(authorUserId)
        .build();
    //(기존 게시글 엔티티)
    long targetPostId = 2;
    String oldTitle = "my old title";
    String oldBody = "my old body";
    Post postEntity = Post.builder()
        .id(targetPostId)
        .title(oldTitle)
        .body(oldBody)
        .build();
    postEntity.setAuthor(authorEntity);
    //(업데이트할 게시글 정보)
    String newTitle = "my new title";
    String newBody = "my new body";
    PostRequest newPostDto = PostRequest.builder()
        .title(newTitle)
        .body(newBody)
        .build();
    long loginUserId = authorUserId;

    //(mock)
    given(postRepository.findById(targetPostId)).willReturn(Optional.of(postEntity));
    given(userRepository.findById(loginUserId)).willReturn(Optional.of(authorEntity));

    //WHEN
    try {
      postService.updatePost(targetPostId, newPostDto, loginUserId);

    //THEN
    } catch (Exception e) {
      fail("updatePost() throws not excepted Exception\n"+e.getMessage());
    }
    assertSame(newTitle, postEntity.getTitle());
    assertSame(newBody, postEntity.getBody());
  }

  @Test
  void 게시글_수정시_찾을_수_없는_게시글ID인_경우() {
    //GIVEN
    long notExistPostId = 1;

    //(mock)
    given(postRepository.findById(notExistPostId)).willReturn(Optional.empty());

    //WHEN
    try {
      postService.updatePost(notExistPostId, null, 0);

      //THEN
      fail("updatePost() must throw InvalidValueException");
    } catch(InvalidValueException e) {
      assertSame(ErrorCode.NOT_FOUND_POST, e.getErrorCode());
    } catch (Exception e) {
      fail("updatePost() throws not excepted Exception\n"+e.getMessage());
    }
  }

  @Test
  void 게시글_수정시_해당_글_작성자가_아닌_경우() {
    //GIVEN
    //(작성자 사용자 엔티티)
    long authorUserId = 1;
    User authorEntity = User.builder()
        .id(authorUserId)
        .build();
    //(게시글 엔티티)
    long targetPostId = 2;
    Post postEntity = Post.builder()
        .id(targetPostId)
        .build();
    postEntity.setAuthor(authorEntity);
    //로그인 사용자
    long loginUserId = authorUserId + 1;
    User loginUser = User.builder()
        .id(loginUserId)
        .build();

    //(mock)
    given(postRepository.findById(targetPostId)).willReturn(Optional.of(postEntity));
    given(userRepository.findById(loginUserId)).willReturn(Optional.of(loginUser));

    //WHEN
    try {
      postService.updatePost(targetPostId, null, loginUserId);

      //THEN
      fail("updatePost() must throw InvalidValueException");
    } catch(InvalidValueException e) {
      assertSame(ErrorCode.AUTHOR_NOT_MATCHED, e.getErrorCode());
    } catch (Exception e) {
      fail("updatePost() throws not excepted Exception\n"+e.getMessage());
    }
  }

  @Test
  void 게시글_수정시_전달받은_작성자_ID를_찾을_수_없는_경우() {
    //GIVEN
    //(작성자 사용자 엔티티)
    long authorUserId = 1;
    User authorEntity = User.builder()
        .id(authorUserId)
        .build();
    //(게시글 엔티티)
    long targetPostId = 2;
    Post postEntity = Post.builder()
        .id(targetPostId)
        .build();
    postEntity.setAuthor(authorEntity);
    //로그인 사용자 ID
    long notExistLoginUserId = authorUserId + 1;

    //(mock)
    given(postRepository.findById(targetPostId)).willReturn(Optional.of(postEntity));
    given(userRepository.findById(notExistLoginUserId)).willReturn(Optional.empty());

    //WHEN
    try {
      postService.updatePost(targetPostId, null, notExistLoginUserId);

      //THEN
      fail("updatePost() must throw InvalidValueException");
    } catch(InvalidValueException e) {
      assertSame(ErrorCode.NOT_FOUND_CURRENT_USER, e.getErrorCode());
    } catch (Exception e) {
      fail("updatePost() throws not excepted Exception\n"+e.getMessage());
    }
  }

  @Test
  void 게시글_삭제_성공() {
    //GIVEN
    //(작성자 사용자 엔티티)
    long authorUserId = 1;
    User authorEntity = User.builder()
        .id(authorUserId)
        .build();
    //(기존 게시글 엔티티)
    long postId = 2;
    Post postEntity = Post.builder()
        .id(postId)
        .build();
    postEntity.setAuthor(authorEntity);
    //로그인 사용자
    long loginUserId = authorUserId;

    //(mock)
    given(postRepository.findById(postId)).willReturn(Optional.of(postEntity));
    given(userRepository.findById(loginUserId)).willReturn(Optional.of(authorEntity));

    //WHEN
    try {
      postService.deletePost(postId, loginUserId);

      //THEN
    } catch (Exception e) {
      fail("deletePost() throws not excepted Exception\n"+e.getMessage());
    }
    then(postRepository).should(times(1)).delete(postEntity);
  }

  @Test
  void 게시글_삭제시_찾을_수_없는_게시글ID인_경우() {
    //GIVEN
    //(기존 게시글 엔티티)
    long notExistPostId = 1;

    //(mock)
    given(postRepository.findById(notExistPostId)).willReturn(Optional.empty());

    //WHEN
    try {
      postService.deletePost(notExistPostId, -1);

      //THEN
      fail("deletePost() must throw InvalidValueException");
    } catch(InvalidValueException e) {
      assertSame(ErrorCode.NOT_FOUND_POST, e.getErrorCode());
    } catch (Exception e) {
      fail("deletePost() throws not excepted Exception\n"+e.getMessage());
    }
    then(postRepository).should(times(1)).findById(notExistPostId);
  }

  @Test
  void 게시글_삭제시_해당_글_작성자가_아닌_경우() {
    //GIVEN
    //(작성자 사용자 엔티티)
    long authorUserId = 1;
    User authorEntity = User.builder()
        .id(authorUserId)
        .build();
    //(기존 게시글 엔티티)
    long postId = 2;
    Post postEntity = Post.builder()
        .id(postId)
        .build();
    postEntity.setAuthor(authorEntity);
    //로그인 사용자
    long notMatchedLoginUserId = authorUserId + 1;
    User notMatchedLoginUser = User.builder()
        .id(notMatchedLoginUserId)
        .build();

    //(mock)
    given(postRepository.findById(postId)).willReturn(Optional.of(postEntity));
    given(userRepository.findById(notMatchedLoginUserId)).willReturn(Optional.of(notMatchedLoginUser));

    //WHEN
    try {
      postService.deletePost(postId, notMatchedLoginUserId);

      //THEN
      fail("deletePost() must throw InvalidValueException");
    } catch(InvalidValueException e) {
      assertSame(ErrorCode.AUTHOR_NOT_MATCHED, e.getErrorCode());
    } catch (Exception e) {
      fail("deletePost() throws not excepted Exception\n"+e.getMessage());
    }
    then(userRepository).should(times(1)).findById(notMatchedLoginUserId);
  }

  @Test
  void 게시글_삭제시_전달받은_작성자_ID를_찾을_수_없는_경우() {
    //GIVEN
    //(작성자 사용자 엔티티)
    long authorUserId = 1;
    User authorEntity = User.builder()
        .id(authorUserId)
        .build();
    //(기존 게시글 엔티티)
    long postId = 2;
    Post postEntity = Post.builder()
        .id(postId)
        .build();
    postEntity.setAuthor(authorEntity);
    //로그인 사용자
    long loginUserId = authorUserId;

    //(mock)
    given(postRepository.findById(postId)).willReturn(Optional.of(postEntity));
    given(userRepository.findById(loginUserId)).willReturn(Optional.empty());

    //WHEN
    try {
      postService.deletePost(postId, loginUserId);

      //THEN
      fail("deletePost() must throw InvalidValueException");
    } catch(InvalidValueException e) {
      assertSame(ErrorCode.NOT_FOUND_CURRENT_USER, e.getErrorCode());
    } catch (Exception e) {
      fail("deletePost() throws not excepted Exception\n"+e.getMessage());
    }
    then(userRepository).should(times(1)).findById(loginUserId);
  }
}