package wanted.preonboarding.assignment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import wanted.preonboarding.assignment.dto.PostDto;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class PostController implements PostApi {
  @Override
  public void postNewPost(PostDto.PostRequest postRequest) {

  }

  @Override
  public List<PostDto.PostResponse> getPostsList(long cursorId, int pageSize) {
    return null;
  }

  @Override
  public PostDto.PostResponse getPost(long postId) {
    return null;
  }

  @Override
  public void patchPost(long postId, PostDto.PostRequest postRequest) {

  }

  @Override
  public void deletePost(long postId) {

  }
}
