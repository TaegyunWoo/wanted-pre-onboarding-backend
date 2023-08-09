/**
 * packageName    : wanted.preonboarding.assignment.controller
 * fileName       : PostController
 * author         : 우태균
 * date           : 2023/08/09
 * description    : 게시글 컨트롤러
 */
package wanted.preonboarding.assignment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import wanted.preonboarding.assignment.dto.LoginUser;
import wanted.preonboarding.assignment.service.PostService;

import java.util.List;

import static wanted.preonboarding.assignment.dto.PostDto.PostRequest;
import static wanted.preonboarding.assignment.dto.PostDto.PostResponse;

@RequiredArgsConstructor
@RestController
public class PostController implements PostApi {
  private final PostService postService;

  /**
   * 새로운 게시글 작성 핸들러
   * @param postRequest 작성할 게시글 내용
   * @param loginUser 요청한 사용자
   */
  @Override
  public void postNewPost(PostRequest postRequest, LoginUser loginUser) {
    postService.saveNewPost(postRequest, loginUser.getId());
  }

  @Override
  public List<PostResponse> getPostsList(long cursorId, int pageSize) {
    return null;
  }

  @Override
  public PostResponse getPost(long postId) {
    return null;
  }

  @Override
  public void patchPost(long postId, PostRequest postRequest, LoginUser loginUser) {

  }

  @Override
  public void deletePost(long postId, LoginUser loginUser) {

  }
}
