/**
 * packageName    : wanted.preonboarding.assignment.service
 * fileName       : PostService
 * author         : 우태균
 * date           : 2023/08/09
 * description    : 게시 Service
 */
package wanted.preonboarding.assignment.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanted.preonboarding.assignment.domain.Post;
import wanted.preonboarding.assignment.domain.User;
import wanted.preonboarding.assignment.exception.ErrorCode;
import wanted.preonboarding.assignment.exception.InvalidValueException;
import wanted.preonboarding.assignment.mapper.PostMapper;
import wanted.preonboarding.assignment.repository.PostRepository;
import wanted.preonboarding.assignment.repository.UserRepository;

import static wanted.preonboarding.assignment.dto.PostDto.PostRequest;

@AllArgsConstructor
@Service
public class PostService {
  private final UserRepository userRepository;
  private final PostRepository postRepository;

  @Transactional
  public void saveNewPost(PostRequest postRequest, long authorPk) {
    User authorEntity = userRepository.findById(authorPk).orElseThrow(
        () -> new InvalidValueException(ErrorCode.NOT_FOUND_CURRENT_USER)
    );
    Post postEntity = PostMapper.INSTANCE.toEntity(postRequest, authorEntity);
    postRepository.save(postEntity);
  }
}
