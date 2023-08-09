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
