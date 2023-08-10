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

import java.util.List;
import java.util.stream.Collectors;

import static wanted.preonboarding.assignment.dto.PostDto.*;

@AllArgsConstructor
@Service
public class PostService {
  private final UserRepository userRepository;
  private final PostRepository postRepository;

  /**
   * 새 게시글을 저장하는 메서드
   * @param postRequest 새 게시글 정보
   * @param authorPk 작성자 PK(ID)
   */
  @Transactional
  public void saveNewPost(PostRequest postRequest, long authorPk) {
    User authorEntity = userRepository.findById(authorPk).orElseThrow(
        () -> new InvalidValueException(ErrorCode.NOT_FOUND_CURRENT_USER)
    );
    Post postEntity = PostMapper.INSTANCE.toEntity(postRequest, authorEntity);
    postRepository.save(postEntity);
  }

  /**
   * 게시글 목록을 조회하는 메서드
   * @param paginationRequest 페이징 처리 정보
   * @return 해당 페이지에 해당하는 게시글 List
   */
  @Transactional(readOnly = true)
  public List<PostSimpleResponse> inquiryPostList(PaginationRequest paginationRequest) {
    long cursorId = paginationRequest.getCursorId();
    int pageSize = paginationRequest.getPageSize();
    List<Post> postEntityList;

    if (cursorId == -1) { //가장 최근 게시글부터 조회한다면
      postEntityList = postRepository.findRecency(pageSize);
    } else { //페이징 기준 게시글이 있다면
      postEntityList = postRepository.findWithPagination(cursorId, pageSize);
    }

    //List<Post> -> List<PostSimpleResponse>
    List<PostSimpleResponse> responses = postEntityList.stream()
        .map(PostMapper.INSTANCE::toSimpleResponseDto)
        .collect(Collectors.toList());

    return responses;
  }

  /**
   * 특정 게시글을 조회하는 메서드
   * @param postId 조회할 게시글 ID(PK)
   * @return 조회된 게시글 정보
   */
  @Transactional(readOnly = true)
  public PostResponse inquiryPost(long postId) {
    Post postEntity = postRepository.findById(postId).orElseThrow(
        () -> new InvalidValueException(ErrorCode.NOT_FOUND_POST)
    );

    //Post -> PostResponse
    PostResponse response = PostMapper.INSTANCE.toResponseDto(postEntity);
    return response;
  }
}
