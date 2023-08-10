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

import static wanted.preonboarding.assignment.dto.PostDto.*;

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

  /**
   * 페이징된 게시글 목록 조회 핸들러
   * @param paginationRequest 페이징 정보
   * @return 게시글 목록
   */
  @Override
  public List<PostSimpleResponse> getPostsList(PaginationRequest paginationRequest) {
    List<PostSimpleResponse> responses = postService.inquiryPostList(paginationRequest);
    return responses;
  }

  /**
   * 특정 게시글 조회 핸들러
   * @param postId 조회할 게시글의 ID(PK)
   * @return 조회된 게시글 정보
   */
  @Override
  public PostResponse getPost(long postId) {
    PostResponse response = postService.inquiryPost(postId);
    return response;
  }

  @Override
  public void patchPost(long postId, PostRequest postRequest, LoginUser loginUser) {

  }

  @Override
  public void deletePost(long postId, LoginUser loginUser) {

  }
}
